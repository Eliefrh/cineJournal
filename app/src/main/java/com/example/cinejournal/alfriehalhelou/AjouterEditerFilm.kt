package com.example.cinejournal.alfriehalhelou

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar

class AjouterEditerFilm : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajouter_editer_film)

        var toolbar: Toolbar = findViewById(R.id.toolbar_ajouter_editer)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Ajouter/Éditer un film"
    }
}