package com.example.smartfoods.chat;

import android.content.Context;
import android.text.TextUtils;

import com.example.smartfoods.data.AppDatabase;
import com.example.smartfoods.data.RiskAssessor;
import com.example.smartfoods.data.WadaSearch;
import com.example.smartfoods.data.entities.ConsumptionLog;
import com.example.smartfoods.data.entities.Profile;
import com.example.smartfoods.data.entities.Substance;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChatbotEngine {

    private final Context context;

    // Minimal conversational state for in-competition follow-ups
    private static String lastRiskLevel;
    private static long lastRiskAtMs;

    public ChatbotEngine(Context context) {
        this.context = context.getApplicationContext();
    }

    public String respond(String userMessage) {
        if (userMessage == null) return "I didn't catch that. Can you rephrase?";
        String msg = userMessage.trim();
        if (msg.isEmpty()) return "Please type a question or say what you consumed.";

        // Basic intents
        if (containsAny(msg, new String[]{"help", "what can you do"})) {
            return "I can: 1) check ingredients/substances against WADA, 2) assess risk based on days to competition, 3) log your consumption with dosage, and 4) suggest alternatives.";
        }

        if (startsWithAny(msg, new String[]{"log ", "i took ", "i consumed ", "add log ", "record "})) {
            return handleLog(msg);
        }

        // Follow-up: user reports weekly consumption count
        if (msg.matches("(?i)^(consumed|took|used)\\s+\\d+\\s+times(\\s+this\\s+week)?\\.?$") || msg.matches("(?i)^\\d+\\s*(x|times)$")) {
            return handleConsumptionFollowUp(msg);
        }

        if (startsWithAny(msg, new String[]{"is ", "check ", "can i take ", "safe to take ", "is it safe ", "is this safe ", "risk of ", "risk "})) {
            return handleRiskQuery(msg);
        }

        // Default: try search and assess briefly
        return handleRiskQuery("check " + msg);
    }

    private String handleLog(String msg) {
        ParsedLog parsed = parseLog(msg);
        if (parsed == null || TextUtils.isEmpty(parsed.substanceName)) {
            return "To log, try: 'log 50 mg tramadol' or 'I took 2 tabs salbutamol'.";
        }
        long now = System.currentTimeMillis();
        Profile profile = AppDatabase.getInstance(context).profileDao().getPrimaryProfile();
        ConsumptionLog log = new ConsumptionLog(profile == null ? 0 : profile.id,
                now,
                parsed.productName == null ? parsed.substanceName : parsed.productName,
                parsed.substanceName,
                parsed.amount,
                parsed.unit);
        AppDatabase.getInstance(context).consumptionLogDao().insert(log);
        return "Logged: " + humanizeAmount(parsed.amount, parsed.unit) + " " + parsed.substanceName + ".";
    }

    private String handleRiskQuery(String msg) {
        String query = msg
                .replaceFirst("(?i)^(is|check|can i take|safe to take|is it safe|is this safe|risk of|risk)\\s+", "")
                .trim();
        if (query.isEmpty()) return "Tell me a substance or ingredient to check.";

        WadaSearch search = new WadaSearch(context);
        List<Substance> results = search.searchFuzzy(query, 5);
        if (results.isEmpty()) return "I didn't find a prohibited substance match for '" + query + "'.";
        Profile profile = AppDatabase.getInstance(context).profileDao().getPrimaryProfile();
        int days = RiskAssessor.daysToNextCompetition(profile);
        String risk = RiskAssessor.assessRiskForSubstances(results, days);

        StringBuilder sb = new StringBuilder();
        sb.append("Risk: ").append(risk);
        if (days != Integer.MAX_VALUE) sb.append(" (" + days + " days to competition)");
        sb.append("\nMatches: ");
        for (int i = 0; i < results.size(); i++) {
            Substance s = results.get(i);
            if (i > 0) sb.append(", ");
            sb.append(s.name).append(" [").append(s.category).append("]");
        }
        // Alternatives: naive - if prohibited, suggest non-prohibited categories placeholder
        if ("prohibited".equals(risk)) {
            sb.append("\nAlternative suggestion: consult a team physician. Consider category-compliant options.");
        } else if ("caution".equals(risk)) {
            sb.append("\nCaution: In-competition risk. Verify dosage and TUE as applicable.");
        }
        // Store minimal state for follow-up
        lastRiskLevel = risk;
        lastRiskAtMs = System.currentTimeMillis();

        // If user is in-competition, prompt a follow-up question
        if (profile != null && profile.isInCompetition()) {
            sb.append("\nSince you're in competition: how many times this week? Reply like 'consumed 2 times'.");
        }
        return sb.toString();
    }

    private String handleConsumptionFollowUp(String msg) {
        // Require a recent risk context
        if (lastRiskLevel == null || (System.currentTimeMillis() - lastRiskAtMs) > 10 * 60 * 1000L) {
            return "Please ask a risk question first (e.g., 'check tramadol'), then tell me how many times you consumed it this week.";
        }
        Profile profile = AppDatabase.getInstance(context).profileDao().getPrimaryProfile();
        if (profile == null || !profile.isInCompetition()) {
            return "Follow-up not needed: you're not marked as in-competition. You can still ask risk checks.";
        }
        int times = extractCount(msg);
        if (times < 0) return "Tell me like: 'consumed 2 times'.";
        boolean permissible = evaluatePermissible(lastRiskLevel, times);
        return (permissible ? "Within permissible limit." : "Not within permissible limit.") +
                " (risk: " + lastRiskLevel + ", weekly count: " + times + ")";
    }

    private int extractCount(String msg) {
        String lower = msg.toLowerCase(Locale.US).replaceAll("[^0-9 ]", " ").replaceAll("\\s+", " ").trim();
        String[] parts = lower.split(" ");
        for (String p : parts) {
            try { return Integer.parseInt(p); } catch (NumberFormatException ignored) {}
        }
        return -1;
    }

    private boolean evaluatePermissible(String risk, int weeklyCount) {
        if ("prohibited".equalsIgnoreCase(risk)) return false;
        if ("caution".equalsIgnoreCase(risk)) return weeklyCount <= 2;
        return true;
    }

    private static boolean containsAny(String text, String[] terms) {
        String lower = text.toLowerCase(Locale.US);
        for (String t : terms) if (lower.contains(t)) return true;
        return false;
    }

    private static boolean startsWithAny(String text, String[] terms) {
        String lower = text.toLowerCase(Locale.US);
        for (String t : terms) if (lower.startsWith(t)) return true;
        return false;
    }

    private static class ParsedLog {
        String substanceName;
        double amount;
        String unit;
        String productName;
    }

    private static String humanizeAmount(double amount, String unit) {
        if (amount <= 0) return "unspecified";
        return ((amount == Math.rint(amount)) ? String.valueOf((int)amount) : String.valueOf(amount)) + " " + unit;
    }

    private ParsedLog parseLog(String msg) {
        // crude parse: look for number + unit + name
        String lower = msg.toLowerCase(Locale.US).replaceAll("[^a-z0-9. ]", " ").replaceAll("\\s+", " ").trim();
        String[] parts = lower.split(" ");
        ParsedLog p = new ParsedLog();
        for (int i = 0; i < parts.length; i++) {
            try {
                double value = Double.parseDouble(parts[i]);
                String unit = (i + 1 < parts.length) ? parts[i + 1] : "";
                if (isUnit(unit)) {
                    p.amount = value;
                    p.unit = normalizeUnit(unit);
                    // remaining words form the substance/product name
                    StringBuilder sb = new StringBuilder();
                    for (int j = i + 2; j < parts.length; j++) {
                        if (sb.length() > 0) sb.append(' ');
                        sb.append(parts[j]);
                    }
                    p.substanceName = sb.toString().trim();
                    if (TextUtils.isEmpty(p.substanceName)) p.substanceName = "unknown";
                    return p;
                }
            } catch (NumberFormatException ignored) { }
        }
        // fallback: no dosage
        p.substanceName = lower.replaceFirst("^(log|i took|i consumed|add log|record) ", "").trim();
        p.amount = 0;
        p.unit = "unit";
        return p;
    }

    private static boolean isUnit(String u) {
        String n = normalizeUnit(u);
        return n.equals("mg") || n.equals("g") || n.equals("ml") || n.equals("tabs") || n.equals("puffs") || n.equals("units");
    }

    private static String normalizeUnit(String u) {
        if (u == null) return "unit";
        String x = u.toLowerCase(Locale.US);
        if (x.equals("tablet") || x.equals("tab") || x.equals("tabs")) return "tabs";
        if (x.equals("puff") || x.equals("puffs")) return "puffs";
        return x;
    }
}


