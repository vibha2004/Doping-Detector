package com.example.smartfoods.data;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import com.example.smartfoods.data.entities.SportBan;
import com.example.smartfoods.data.entities.Substance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Imports WADA 2025 CSV from res/raw/wada_2025.csv.
 * Headers expected (from current file):
 * - category_name, substance_name, alternative_names, prohibition_scope, threshold_limit, notes, sports
 */
class WadaCsvImporter {

    static class ImportResult {
        final List<Substance> substances;
        final List<SportBan> sportBans;
        ImportResult(List<Substance> substances, List<SportBan> sportBans) {
            this.substances = substances;
            this.sportBans = sportBans;
        }
    }

    static ImportResult importFromRaw(Context context) {
        try {
            Resources res = context.getResources();
            int id = res.getIdentifier("wada_2025", "raw", context.getPackageName());
            if (id == 0) return new ImportResult(new ArrayList<Substance>(), new ArrayList<SportBan>());
            InputStream is = res.openRawResource(id);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            String header = br.readLine();
            if (header == null) return new ImportResult(new ArrayList<Substance>(), new ArrayList<SportBan>());

            String[] heads = splitCsv(header);
            Map<String, Integer> idx = new HashMap<String, Integer>();
            for (int i = 0; i < heads.length; i++) {
                String key = heads[i] == null ? "" : heads[i];
                // Remove potential UTF-8 BOM and normalize
                key = key.replace("\ufeff", "").toLowerCase(Locale.US).trim();
                idx.put(key, i);
            }

            int iCategoryName = firstIndex(idx, new String[]{"category_name"});
            int iName = firstIndex(idx, new String[]{"substance_name"});
            int iAliases = firstIndex(idx, new String[]{"alternative_names"});
            int iDesc = firstIndex(idx, new String[]{"notes"});
            int iSports = firstIndex(idx, new String[]{"sports"});

            // Fallback to known positions if headers were not resolved
            if (iCategoryName < 0) iCategoryName = 0;           // category_name
            if (iName < 0) iName = 1;                            // substance_name
            if (iAliases < 0) iAliases = 2;                      // alternative_names
            // prohibition_scope is at 3 (not used here for mapping category)
            // threshold_limit at 4 (unused)
            if (iDesc < 0) iDesc = 5;                            // notes
            if (iSports < 0) iSports = 6;                        // sports

            List<Substance> substances = new ArrayList<Substance>();
            List<SportBan> sportBans = new ArrayList<SportBan>();
            int countS0=0,countS1=0,countS2=0,countS3=0,countS4=0,countS5=0,countS6=0,countS7=0,countS8=0,countS9=0,countP=0;
            int debugRowsLogged = 0;

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] cols = splitCsv(line);
                String categoryName = get(cols, iCategoryName);
                String name = get(cols, iName);
                if (TextUtils.isEmpty(name)) continue;
                String cat = WadaCsvImporter.mapCategoryCode(categoryName);
                String aliases = get(cols, iAliases);
                String desc = get(cols, iDesc);
                substances.add(new Substance(name.trim(), cat.trim(), safe(aliases), safe(desc)));
                if (debugRowsLogged < 10) {
                    Log.d("WadaCsvImporter", "row: category_name='"+categoryName+"' -> cat='"+cat+"' name='"+name+"'");
                    debugRowsLogged++;
                }
                // Counters for quick verification in Logcat
                if (cat.startsWith("S0")) countS0++;
                else if (cat.startsWith("S1")) countS1++;
                else if (cat.startsWith("S2")) countS2++;
                else if (cat.startsWith("S3")) countS3++;
                else if (cat.startsWith("S4")) countS4++;
                else if (cat.startsWith("S5")) countS5++;
                else if (cat.startsWith("S6")) countS6++;
                else if (cat.startsWith("S7")) countS7++;
                else if (cat.startsWith("S8")) countS8++;
                else if (cat.startsWith("S9")) countS9++;
                else if (cat.startsWith("P")) countP++;

                // Capture sport-specific bans from CSV (e.g., Beta-Blockers -> P1)
                if (cat.startsWith("P")) {
                    String sportsList = get(cols, iSports);
                    if (!TextUtils.isEmpty(sportsList)) {
                        for (String token : WadaCsvImporter.splitSports(sportsList)) {
                            String s = token.trim().toLowerCase(Locale.US);
                            if (!TextUtils.isEmpty(s)) {
                                sportBans.add(new SportBan(cat.trim(), s));
                            }
                        }
                    }
                }
            }
            br.close();

            Log.i("WadaCsvImporter", "Import summary: S0=" + countS0 + ", S1=" + countS1 + ", S2=" + countS2 + ", S3=" + countS3 + ", S4=" + countS4
                    + ", S5=" + countS5 + ", S6=" + countS6 + ", S7=" + countS7 + ", S8=" + countS8 + ", S9=" + countS9 + ", P*=" + countP);
            return new ImportResult(substances, sportBans);
        } catch (Exception e) {
            Log.e("WadaCsvImporter", "Failed to import CSV: " + e.getMessage());
            return new ImportResult(new ArrayList<Substance>(), new ArrayList<SportBan>());
        }
    }

    private static String mapCategoryCode(String categoryName) {
        if (categoryName == null) return "S0";
        String c = categoryName.trim().toLowerCase(Locale.US);
        // Direct codes like "S6", "S6: stimulants", "p1" etc.
        if (c.length() >= 2 && (c.charAt(0) == 's' || c.charAt(0) == 'p')) {
            String code = c.substring(0, Math.min(2, c.length())).toUpperCase(Locale.US);
            if (code.matches("S[0-9]")) return code;
            if (code.matches("P[0-9]")) return code;
        }
        if (c.startsWith("non-approved")) return "S0";
        if (c.startsWith("anabolic")) return "S1";
        if (c.startsWith("peptide")) return "S2";
        if (c.startsWith("beta-2")) return "S3";
        if (c.startsWith("hormone and metabolic") || c.startsWith("hormone & metabolic") || c.equals("hormone and metabolic modulators")) return "S4";
        if (c.startsWith("diuretics") || c.contains("masking")) return "S5";
        if (c.startsWith("stimulant")) return "S6";
        if (c.startsWith("narcotic")) return "S7";
        if (c.startsWith("cannabinoid")) return "S8";
        if (c.startsWith("glucocorticoid")) return "S9";
        if (c.startsWith("beta-blocker") || c.startsWith("beta blockers") || c.startsWith("beta blockers")) return "P1";
        // M classes
        if (c.startsWith("enhanced oxygen transfer") || c.contains("blood doping")) return "M1";
        if (c.startsWith("chemical and physical manipulation") || c.startsWith("tampering")) return "M2";
        if (c.startsWith("gene and cell doping") || c.startsWith("gene doping")) return "M3";
        // Map other categories if needed (M1-M3 etc.). Default fallback:
        Log.w("WadaCsvImporter", "Unmapped category '" + categoryName + "' -> defaulting to S0");
        return "S0";
    }

    private static List<String> splitSports(String sportsRaw) {
        ArrayList<String> out = new ArrayList<String>();
        StringBuilder cur = new StringBuilder();
        int paren = 0;
        for (int i = 0; i < sportsRaw.length(); i++) {
            char ch = sportsRaw.charAt(i);
            if (ch == '(') paren++;
            if (ch == ')') { if (paren > 0) paren--; continue; }
            if (ch == ',' && paren == 0) {
                out.add(cleanSport(cur.toString()));
                cur.setLength(0);
            } else {
                if (paren == 0) cur.append(ch); // drop parenthetical org codes
            }
        }
        out.add(cleanSport(cur.toString()));
        return out;
    }

    private static String cleanSport(String s) {
        return s == null ? "" : s.replace('"',' ').replace('\n',' ').trim();
    }

    private static int firstIndex(Map<String, Integer> idx, String[] keys) {
        for (String k : keys) {
            Integer i = idx.get(k);
            if (i != null) return i;
        }
        return -1;
    }

    private static String get(String[] arr, int i) {
        if (i < 0 || i >= arr.length) return "";
        return arr[i];
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }

    private static String[] splitCsv(String line) throws IOException {
        // Minimal CSV splitter supporting quoted commas
        ArrayList<String> out = new ArrayList<String>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                out.add(cur.toString().trim().replaceAll("^\"|\"$", ""));
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        out.add(cur.toString().trim().replaceAll("^\"|\"$", ""));
        return out.toArray(new String[out.size()]);
    }
}


