package com.example.cinejournal.alfriehalhelou

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
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

    //    lateinit var filmDao: FilmDao
    lateinit var recyclerView: RecyclerView


    private fun mapApiResultToItemFilms(apiResponse: ApiResponse): List<ItemFilm> {
        return apiResponse.results.map { film ->
            ItemFilm(
                uid = film.id,
                titre = film.title,
                slogan = film.overview ?: "",
                annee = film.release_date?.substringBefore("-")?.toIntOrNull() ?: 0,
                note = film.vote_average.toFloat(),
                image = film.poster_path ?: ""
            )
        }
    }

    //    private fun getYearFromReleaseDate(releaseDate: String): Int {
//        return try {
//            releaseDate.substring(0, 4).toInt()
//        } catch (e: Exception) {
//            0
//        }
//    }
//
//    // Fonction pour obtenir le chemin d'image complet
//    private fun getFullImagePath(posterPath: String?): String {
//        return if (!posterPath.isNullOrBlank()) {
//            "https://image.tmdb.org/t/p/w500$posterPath"
//        } else {
//            ""
//        }
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recherche_film)

        var toolbar: Toolbar = findViewById(R.id.toolbar_recherche)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Ciné Journal"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val butounRechercher: ImageButton = findViewById(R.id.imageButtonRechercher)
        val barDeRecherche: EditText = findViewById(R.id.editTextRecherche)
        var pageVide: TextView = findViewById(R.id.pageVide)
        adapteur = FilmAdapteur(applicationContext, RechercheFilm(), listOf())


        // le jeton de l'API
        val cle = BuildConfig.API_KEY_TMBD

        butounRechercher.setOnClickListener {
            val chercheFilm = barDeRecherche.text.toString()

            lifecycleScope.launch {
                val reponse = withContext(Dispatchers.IO) {
                    ApiClient.apiService.getFilmBySearch(chercheFilm)
                }
                Log.d("Elie recerche de film", reponse.toString())

                if (!reponse.isSuccessful) return@launch

                if (reponse.body() == null)
                    return@launch

                // Mappez la réponse de l'API vers la liste d'ItemFilm
                val listeFilms = mapApiResultToItemFilms(reponse.body()!!)
                Log.d("Liste des films", listeFilms.toString())

                val recycleThread = (lifecycleScope.launch {
                    recyclerView = findViewById(R.id.listeFilms)
                    var liste = listeFilms

                    if (liste.isEmpty()) {
                        pageVide.visibility = View.VISIBLE
                    } else {
                        pageVide.visibility = View.INVISIBLE
                    }
                    Log.d("Elie liste", liste.toString())
                    adapteur = FilmAdapteur(applicationContext, RechercheFilm(), liste)
                    recyclerView.adapter = adapteur
                })
            }
        }


    }

//        lifecycleScope.launch {
//            val reponse = withContext(Dispatchers.IO) {
//                ApiClient.apiService.getMovieById(11)
//            }
//            Log.d("Elie",ApiClient.apiService.getMovieById(11).toString())
//
//            if (!reponse.isSuccessful) return@launch
//
//            if (reponse.body() == null)
//                return@launch
//
//            val Film = reponse.body()!!
//            Log.d("Film", Film.toString())
//
////              recyclerView.adapter = listOf<String>(ApiClient.apiService.getMovieById(11))
//        }


//        lifecycleScope.launch {
//            val reponse = withContext(Dispatchers.IO) {
//                ApiClient.apiService.getFilmBySearch("Matrix")
//            }
//            Log.d("Elie recerche de film",reponse.toString())
//
//            if (!reponse.isSuccessful) return@launch
//
//            if (reponse.body() == null)
//                return@launch
//
//            val Film = reponse.body()!!
//            Log.d("Film", Film.toString())
//
//
//        }
//    }

}