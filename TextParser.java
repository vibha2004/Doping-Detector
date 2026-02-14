package com.example.smartfoods;

import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.example.smartfoods.data.WadaSearch;
import com.example.smartfoods.data.entities.Substance;

public class TextParser {

    public TextParser() {
        // No cosmetic initialization
    }

    // ------------------------
    // WADA-based detection API
    // ------------------------

    public static class WadaDetectionResult {
        public final ArrayList<String> allTimes;           // S0-S5
        public final ArrayList<String> inCompetition;      // S6-S9
        public final ArrayList<String> sportSpecific;      // P1 (and future P*)
        public WadaDetectionResult(ArrayList<String> allTimes,
                                   ArrayList<String> inCompetition,
                                   ArrayList<String> sportSpecific) {
            this.allTimes = allTimes;
            this.inCompetition = inCompetition;
            this.sportSpecific = sportSpecific;
        }
    }

    private static final Map<String, Set<String>> P_CATEGORY_SPORTS = buildSportSpecificCategoryMap();

    private static Map<String, Set<String>> buildSportSpecificCategoryMap() {
        Map<String, Set<String>> map = new HashMap<>();
        // Database-driven sport bans are used; keep empty map as fallback
        return map;
    }

    private boolean appliesToSport(Context context, String category, String sport) {
        if (category == null || sport == null) return false;
        String cat = category.toUpperCase(Locale.US);
        if (!cat.startsWith("P")) return false;
        try {
            List<com.example.smartfoods.data.entities.SportBan> bans = com.example.smartfoods.data.AppDatabase
                    .getInstance(context)
                    .sportBanDao()
                    .getByCategory(cat);
            String s = sport.toLowerCase(Locale.US).trim();
            for (com.example.smartfoods.data.entities.SportBan b : bans) {
                if (s.equals(b.sportLower) || s.contains(b.sportLower)) return true;
            }
        } catch (Exception ignored) { }
        return false;
    }

    public WadaDetectionResult detectWadaSubstances(Context context, ArrayList<String> ocrLines, String sport) {
        WadaSearch search = new WadaSearch(context);
        ArrayList<Substance> matched = new ArrayList<>();

        // Build candidate queries from OCR lines
        ArrayList<String> candidates = new ArrayList<>();
        for (String line : ocrLines) {
            if (line == null) continue;
            // First split into ingredient-like phrases by common delimiters
            String[] parts = line.split("[,:;()\\[\\]\\n\\r\\t]");
            for (String p : parts) {
                String phrase = p.trim();
                if (phrase.length() >= 3) {
                    // Avoid extremely long phrases which won't match
                    if (phrase.length() > 80) phrase = phrase.substring(0, 80);
                    candidates.add(phrase);
                }
                // Also add word tokens stripped of punctuation
                String lower = p.toLowerCase(Locale.US);
                String[] words = lower.split("[^a-zA-Z0-9+-]+");
                for (String w : words) {
                    String token = w.trim();
                    if (token.length() >= 3) candidates.add(token);
                }
            }
        }
        // Dedupe candidates while preserving order
        java.util.LinkedHashSet<String> uniqueCand = new java.util.LinkedHashSet<>(candidates);
        for (String q : uniqueCand) {
            matched.addAll(search.searchFuzzy(q, 5));
        }
        Map<String, Substance> unique = new HashMap<>();
        for (Substance s : matched) if (!unique.containsKey(s.name)) unique.put(s.name, s);
        ArrayList<String> allTimes = new ArrayList<>();
        ArrayList<String> inCompetition = new ArrayList<>();
        ArrayList<String> sportSpecific = new ArrayList<>();
        for (Substance s : unique.values()) {
            String cat = s.category == null ? "" : s.category.toUpperCase(Locale.US);
            String label = s.name + " [" + cat + "]";
            if (cat.startsWith("P")) {
                // Sport-specific
                if (appliesToSport(context, cat, sport)) sportSpecific.add(label);
            } else if (cat.matches("S[0-5].*")) {
                allTimes.add(label);
            } else if (cat.matches("S[6-9].*")) {
                inCompetition.add(label);
            } else {
                allTimes.add(label);
            }
        }
        return new WadaDetectionResult(allTimes, inCompetition, sportSpecific);
    }

    public ArrayList<String> processInput(ArrayList<String> ingredients) {
        ArrayList<String> allIngredients = new ArrayList<>();
        for (String str : ingredients) {
            String line = str.toLowerCase();
            String[] linePieces = line.split(" ");
            for (String str2 : linePieces) allIngredients.add(str2.replaceAll("[^a-zA-Z]", ""));
        }
        return allIngredients;
    }
}