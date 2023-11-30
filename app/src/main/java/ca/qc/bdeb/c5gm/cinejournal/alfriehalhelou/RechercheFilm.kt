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

class RechercheFilm : AppCompatActivity() {

    private lateinit var adapteur: FilmAdapteur
    private lateinit var recyclerView: RecyclerView
    var listeFilms: List<ItemFilm> = listOf()


    fun mapApiResultToItemFilms(apiResponse: ApiResponse ): List<ItemFilm> {
        return apiResponse.results.map { film ->
            ItemFilm(
                uid = film.id,
                titre = film.title,
                slogan = "" ,
                annee = film.release_date?.substringBefore("-")?.toIntOrNull() ?: 0,
                note = film.vote_average.toFloat(),
                image = "https://image.tmdb.org/t/p/w500" + (film.poster_path ?: "")
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recherche_film)

        val toolbar: Toolbar = findViewById(R.id.toolbar_recherche)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Ciné Journal"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val boutonRechercher: ImageButton = findViewById(R.id.imageButtonRechercher)
        val barDeRecherche: EditText = findViewById(R.id.editTextRecherche)
        val pageVide: TextView = findViewById(R.id.pageVide)
        adapteur = FilmAdapteur(applicationContext, this, listOf())

        // le jeton de l'API
        BuildConfig.API_KEY_TMBD

        boutonRechercher.setOnClickListener {
            val chercheFilm = barDeRecherche.text.toString()

            lifecycleScope.launch {
                val reponse = withContext(Dispatchers.IO) {
                    ApiClient.apiService.getFilmBySearch(chercheFilm)
                }

                Log.d("Elie recherche de film", reponse.toString())

                if (!reponse.isSuccessful) return@launch

                if (reponse.body() == null)
                    return@launch

                // Mappez la réponse de l'API vers la liste d'ItemFilm
                mapApiResultToItemFilms(reponse.body()!!)
                val nouveauxFilms = mapApiResultToItemFilms(reponse.body()!!)

                remplirListe(nouveauxFilms)
//
//                listeFilms.clear()
//                listeFilms.(nouveauxFilms)
                adapteur.mettreAJour(listeFilms)

                Log.d("Liste des films", nouveauxFilms.toString())

                recyclerView = findViewById(R.id.listeFilms)

                if (listeFilms.isEmpty()) {
                    pageVide.visibility = View.VISIBLE
                } else {
                    pageVide.visibility = View.INVISIBLE
                }

                Log.d("Elie liste", listeFilms.toString())
                adapteur = FilmAdapteur(applicationContext, RechercheFilm(), nouveauxFilms)
                recyclerView.adapter = adapteur
                adapteur.ajouterFilm(nouveauxFilms)
            }
        }
    }


    fun remplirListe(liste: List<ItemFilm>): List<ItemFilm> {
        listeFilms = liste
        Log.d("liste Remplie", listeFilms.toString())
        return listeFilms
    }
}
