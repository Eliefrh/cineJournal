package com.example.cinejournal.alfriehalhelou

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.OnClickAction
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar


class APropos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activite_apropos)

        //SupportActionBar avec un fleche pour revenir a la page principale
        //parentActivityName est ajouté dans AndroidManifest.xml pour retourner a la page parente
        var toolbar : Toolbar = findViewById(R.id.include)
        setSupportActionBar(toolbar)
        supportActionBar!!.title="À PROPOS"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        val retour: Button = findViewById(R.id.retour)

        retour.setOnClickListener{
           val intent = Intent(this, ListeDeFilms::class.java)
            startActivity(intent)
        }

    }
}