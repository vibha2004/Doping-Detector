package com.example.smartfoods.data;

import android.content.Context;
import android.content.SharedPreferences;

public class ProfileManager {
    private static final String PREFS = "profiles_prefs";
    private static final String KEY_ACTIVE_ID = "active_profile_id";

    public static void setActiveProfileId(Context context, long id) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sp.edit().putLong(KEY_ACTIVE_ID, id).apply();
    }

    public static long getActiveProfileId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return sp.getLong(KEY_ACTIVE_ID, 0);
    }
}


