package com.example.smartfoods.data;

import android.content.Context;

import com.example.smartfoods.data.entities.Substance;

import java.util.ArrayList;
import java.util.List;

public class WadaSearch {
    private final WadaRepository repository;

    public WadaSearch(Context context) {
        this.repository = new WadaRepository(context);
        // Ensure seeds exist for WADA and sport-specific bans
        this.repository.seedIfEmpty();
        this.repository.seedSportSpecificIfEmpty();
    }

    public List<Substance> searchFuzzy(String input, int limit) {
        if (input == null || input.trim().isEmpty()) return new ArrayList<Substance>();
        String query = input.trim();
        List<Substance> primary = repository.search(query, limit);
        if (!primary.isEmpty() || query.length() < 3) return primary;
        // naive fuzzy: try without hyphens/spaces
        query = query.replace("-", " ");
        return repository.search(query, limit);
    }

    public List<Substance> getByCategory(String category) {
        return repository.getByCategory(category);
    }
}


