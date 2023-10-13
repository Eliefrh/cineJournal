package com.example.cinejournal.alfriehalhelou

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RechercheFilm : AppCompatActivity() {

    lateinit var db: AppDatabase
    lateinit var adapteur: FilmAdapteur
    lateinit var filmDao: FilmDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recherche_film)

        var toolbar: Toolbar = findViewById(R.id.toolbar_recherche)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Ciné Journal"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val butounRechercher: ImageButton = findViewById(R.id.imageButtonRechercher)
        val barDeRecherche: EditText = findViewById(R.id.editTextRecherche)


        butounRechercher.setOnClickListener {
            val chercheFilm = barDeRecherche.text.toString()
            lifecycleScope.launch(Dispatchers.IO) {
                val database: AppDatabase = AppDatabase.getDatabase(applicationContext)
                val filmsTrouves = database.FilmDao().findByName(chercheFilm, chercheFilm)
                //    adapteur = FilmAdapteur(applicationContext, ListeDeFilms(), filmsTrouves)
                //    recyclerView.adapter = adapteur
            }


        }
    }

}