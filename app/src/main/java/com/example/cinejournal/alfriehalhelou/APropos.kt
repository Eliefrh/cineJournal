package com.example.cinejournal.alfriehalhelou

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


class APropos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activite_apropos)

        //SupportActionBar avec un fleche pour revenir a la page principale
        //parentActivityName est ajouté dans AndroidManifest.xml pour retourner a la page parente

        var toolbar: Toolbar = findViewById(R.id.toolbar_apropos)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "À propos"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


    }
}