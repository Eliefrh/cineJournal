package com.example.cinejournal.alfriehalhelou

import android.content.Context

class preferencesTri(context: Context) {

    private val prefs = context.getSharedPreferences("prefsTri", Context.MODE_PRIVATE)

    fun getTriPreference(): String {
        return prefs.getString("triPreference", "Trié par l'ordre d'ajout")
            ?: "Trié par l'ordre d'ajout"
    }

    fun setTriPreference(tri: String) {
        prefs.edit().putString("triPreference", tri).apply()
    }


}
