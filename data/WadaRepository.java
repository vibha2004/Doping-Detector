package com.example.smartfoods.data;

import android.content.Context;

import com.example.smartfoods.data.dao.SubstanceDao;
import com.example.smartfoods.data.dao.SportBanDao;
import com.example.smartfoods.data.entities.Substance;
import com.example.smartfoods.data.entities.SportBan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WadaRepository {

    private final SubstanceDao dao;
    private final SportBanDao sportBanDao;
    private final Context appContext;

    public WadaRepository(Context context) {
        this.appContext = context.getApplicationContext();
        AppDatabase db = AppDatabase.getInstance(appContext);
        this.dao = db.substanceDao();
        this.sportBanDao = db.sportBanDao();
    }

    public void seedIfEmpty() {
        if (dao.count() > 0) return;
        WadaCsvImporter.ImportResult imported = WadaCsvImporter.importFromRaw(appContext);
        if (!imported.substances.isEmpty()) {
            dao.insertAll(imported.substances);
        } else {
            List<Substance> seed = new ArrayList<Substance>(Arrays.asList(
                    new Substance("S1 Anabolic Agents (e.g., Stanozolol)", "S1", "stanozolol,winstrol,oxandrolone,anavar", "Anabolic agents prohibited at all times."),
                    new Substance("Clenbuterol", "S1", "clen,clenbuterol hydrochloride", "Anabolic agent with beta-2 agonist properties."),
                    new Substance("Erythropoietin (EPO)", "S2", "epo,erythropoiesis-stimulating agent", "Peptide hormone increasing red blood cells."),
                    new Substance("Growth Hormone (hGH)", "S2", "hgh,somatropin", "Peptide hormone affecting metabolism and growth."),
                    new Substance("Salbutamol", "S3", "albuterol,ventolin", "Beta-2 agonist with dose-based thresholds."),
                    new Substance("Meldonium", "S4", "mildronate,3-(2,2,2-trimethylhydrazinium) propionate", "Metabolic modulator prohibited at all times."),
                    new Substance("Furosemide", "S5", "lasix,diuretic", "Diuretic and masking agent."),
                    new Substance("Non-approved substances", "S0", "research chemical,designer drug", "Any pharmacological substance not approved for human use."),
                    new Substance("Tramadol", "S7", "", "Prohibited in-competition (as of 2024+; verify 2025)."),
                    new Substance("Cannabinoids (THC)", "S8", "tetrahydrocannabinol,marijuana,cannabis", "In-competition prohibition with thresholds."),
                    new Substance("Beta-Blockers (e.g., Propranolol)", "P2", "propranolol,atenolol,metoprolol", "Prohibited in certain precision sports in-competition.")
            ));
            dao.insertAll(seed);
        }
        if (!imported.sportBans.isEmpty()) {
            for (SportBan b : imported.sportBans) sportBanDao.insert(b);
        } else {
            seedSportSpecificIfEmpty();
        }

        // Debug: log distribution of categories after seed
        try {
            int total = dao.count();
            int cS0 = dao.getByCategory("S0").size();
            int cS1 = dao.getByCategory("S1").size();
            int cS2 = dao.getByCategory("S2").size();
            int cS3 = dao.getByCategory("S3").size();
            int cS4 = dao.getByCategory("S4").size();
            int cS5 = dao.getByCategory("S5").size();
            int cS6 = dao.getByCategory("S6").size();
            int cS7 = dao.getByCategory("S7").size();
            int cS8 = dao.getByCategory("S8").size();
            int cS9 = dao.getByCategory("S9").size();
            int cP1 = dao.getByCategory("P1").size();
            android.util.Log.i("WadaRepository", "Seeded substances total="+total+" dist: S0="+cS0+", S1="+cS1+", S2="+cS2+", S3="+cS3+", S4="+cS4+", S5="+cS5+", S6="+cS6+", S7="+cS7+", S8="+cS8+", S9="+cS9+", P1="+cP1);
        } catch (Exception ignored) {}
    }

    

    public void seedSportSpecificIfEmpty() {
        if (sportBanDao.count() > 0) return;
        // Seed P1 and P2 sport-specific bans (extendable)
        // P1: Prohibited in specific sports (as per list provided)
        List<String> p1Sports = Arrays.asList(
                "archery",
                "automobile", "auto racing", "motorsport",
                "billiards",
                "darts",
                "golf",
                "mini-golf", "minigolf",
                "shooting",
                "underwater", "freediving", "spearfishing", "target shooting"
        );
        for (String s : p1Sports) sportBanDao.insert(new SportBan("P1", s));

        // P2: Beta-blockers – precision/skill sports
        List<String> p2Sports = Arrays.asList(
                "archery","shooting","billiards","snooker","pool","darts","golf",
                "motorsport","auto racing","karting","skiing","snowboarding","underwater",
                "finswimming","spearfishing"
        );
        for (String s : p2Sports) sportBanDao.insert(new SportBan("P2", s));
    }

    public List<Substance> search(String query, int limit) {
        return dao.searchByNameOrAlias(query, limit);
    }

    public List<Substance> getByCategory(String category) {
        return dao.getByCategory(category);
    }
}


