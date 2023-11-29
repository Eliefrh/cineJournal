package ca.qc.bdeb.c5gm.cinejournal.alfriehalhelou

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

class RechercheFilm : AppCompatActivity(){

    private lateinit var adapteur: FilmAdapteur
    private lateinit var recyclerView: RecyclerView


    private fun mapApiResultToItemFilms(apiResponse: ApiResponse): List<ItemFilm> {
        return apiResponse.results.map { film ->
            ItemFilm(
                uid = film.id,
                titre = film.title,
                slogan = film.overview ?: "",
                annee = film.release_date?.substringBefore("-")?.toIntOrNull() ?: 0,
                note = film.vote_average.toFloat(),
                image = "https://image.tmdb.org/t/p/w500" + film.poster_path ?: ""
            )
        }
    }

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
        adapteur = FilmAdapteur(applicationContext, this, listOf())


        // le jeton de l'API
        BuildConfig.API_KEY_TMBD

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



}