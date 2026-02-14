package com.example.smartfoods.data;

import android.text.TextUtils;

import com.example.smartfoods.data.entities.Profile;
import com.example.smartfoods.data.entities.Substance;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class RiskAssessor {

    public static int daysToNextCompetition(Profile profile) {
        if (profile == null || TextUtils.isEmpty(profile.nextCompetitionDate)) {
            return Integer.MAX_VALUE;
        }
        return daysBetweenTodayAnd(profile.nextCompetitionDate);
    }

    private static int daysBetweenTodayAnd(String yyyyMmDd) {
        if (yyyyMmDd == null || yyyyMmDd.length() < 10) return Integer.MAX_VALUE;
        try {
            int y = Integer.parseInt(yyyyMmDd.substring(0, 4));
            int m = Integer.parseInt(yyyyMmDd.substring(5, 7));
            int d = Integer.parseInt(yyyyMmDd.substring(8, 10));
            Calendar a = Calendar.getInstance();
            Calendar b = Calendar.getInstance();
            b.set(Calendar.YEAR, y);
            b.set(Calendar.MONTH, m - 1);
            b.set(Calendar.DAY_OF_MONTH, d);
            b.set(Calendar.HOUR_OF_DAY, 0);
            b.set(Calendar.MINUTE, 0);
            b.set(Calendar.SECOND, 0);
            b.set(Calendar.MILLISECOND, 0);
            a.set(Calendar.HOUR_OF_DAY, 0);
            a.set(Calendar.MINUTE, 0);
            a.set(Calendar.SECOND, 0);
            a.set(Calendar.MILLISECOND, 0);
            long diffMs = b.getTimeInMillis() - a.getTimeInMillis();
            return (int)Math.floor(diffMs / (1000.0 * 60 * 60 * 24));
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }

    public static String assessRiskForSubstances(List<Substance> substances, int daysToCompetition) {
        if (substances == null || substances.isEmpty()) return "safe";
        int level = 0; // 0 safe, 1 caution, 2 prohibited
        for (Substance s : substances) {
            String cat = s.category == null ? "" : s.category.toUpperCase(Locale.US);
            if (isAlwaysProhibited(cat)) {
                level = 2; // any always-prohibited immediately sets prohibited
                break;
            } else if (isInCompetitionProhibited(cat)) {
                level = Math.max(level, 1); // mark caution regardless of days
            }
        }
        if (level == 2) return "prohibited";
        if (level == 1) return "caution";
        return "safe";
    }

    private static boolean isAlwaysProhibited(String cat) {
        return cat.startsWith("S0") || cat.startsWith("S1") || cat.startsWith("S2") ||
                cat.startsWith("S4") || cat.startsWith("S5") ||
                cat.startsWith("M1") || cat.startsWith("M2") || cat.startsWith("M3");
    }

    private static boolean isInCompetitionProhibited(String cat) {
        return cat.startsWith("S6") || cat.startsWith("S7") || cat.startsWith("S8") || cat.startsWith("S9") ||
                cat.startsWith("P1") || cat.startsWith("P2") || cat.startsWith("P3");
    }

    public static String toJsonArrayOfNames(List<Substance> substances) {
        List<String> names = new ArrayList<String>();
        Set<String> seen = new HashSet<String>();
        for (Substance s : substances) {
            if (!seen.contains(s.name)) {
                names.add("\"" + s.name.replace("\"", "\\\"") + "\"");
                seen.add(s.name);
            }
        }
        return "[" + TextUtils.join(",", names) + "]";
    }
}


