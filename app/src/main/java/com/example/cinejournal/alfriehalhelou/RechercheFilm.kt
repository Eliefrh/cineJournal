package com.example.cinejournal.alfriehalhelou

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RechercheFilm : AppCompatActivity() {

    lateinit var db: AppDatabase
    lateinit var adapteur: FilmAdapteur
    lateinit var filmDao: FilmDao
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recherche_film)

        var toolbar: Toolbar = findViewById(R.id.toolbar_recherche)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Ciné Journal"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val butounRechercher: ImageButton = findViewById(R.id.imageButtonRechercher)
        val barDeRecherche: EditText = findViewById(R.id.editTextRecherche)

        // le jeton de l'API
        val cle = BuildConfig.API_KEY_TMBD

        butounRechercher.setOnClickListener {
            val chercheFilm = barDeRecherche.text.toString()
            lifecycleScope.launch(Dispatchers.IO) {
                val database: AppDatabase = AppDatabase.getDatabase(applicationContext)
                val filmsTrouves = database.FilmDao().findByName(chercheFilm, chercheFilm)
                //    adapteur = FilmAdapteur(applicationContext, ListeDeFilms(), filmsTrouves)
                //    recyclerView.adapter = adapteur
            }


        }

        lifecycleScope.launch {
            val reponse = withContext(Dispatchers.IO) {
                ApiClient.apiService.getMovieById(11)
            }
            Log.d("Elie",ApiClient.apiService.getMovieById(11).toString())

            if (!reponse.isSuccessful) return@launch

            if (reponse.body() == null)
                return@launch

            val Film = reponse.body()!!
            Log.d("Film", Film.toString())

//              recyclerView.adapter = listOf<String>(ApiClient.apiService.getMovieById(11))
        }
    }

}