package com.example.cinejournal.alfriehalhelou

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar

class ListeDeFilms : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activite_liste_film)

        var toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.aPropos -> {
                Log.d("MonTag", "Clic sur A Propos")
                val intent = Intent(this, APropos::class.java);
                startActivity(intent)
                return true
            }
            R.id.trouverUnFilm -> {

            }
            R.id.toutSupprimer -> {

            }
            R.id.titre -> {

            }
            R.id.note -> {

            }
            R.id.annee -> {

            }

        }
        return super.onOptionsItemSelected(item)
    }
}