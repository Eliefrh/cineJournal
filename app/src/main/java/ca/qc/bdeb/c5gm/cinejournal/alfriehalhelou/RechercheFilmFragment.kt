package ca.qc.bdeb.c5gm.cinejournal.alfriehalhelou

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException


class RechercheFilmFragment : Fragment() {

    val data: FilmViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recherche_film, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val butounRechercher: ImageButton = view.findViewById(R.id.imageButtonRechercher)
        val barDeRecherche: EditText = view.findViewById(R.id.editTextRecherche)
        var pageVide: TextView = view.findViewById(R.id.pageVide)

        BuildConfig.API_KEY_TMBD

        butounRechercher.setOnClickListener {
            val chercheFilm = barDeRecherche.text.toString()

            lifecycleScope.launch {
                try {
                    val reponse = withContext(Dispatchers.IO) {
                        ApiClient.apiService.getFilmBySearch(chercheFilm)
                    }
                    Log.d("on recerche de film", reponse.toString())

                    if (!reponse.isSuccessful) return@launch

                    if (reponse.body() == null)
                        return@launch

                    // Mappez la réponse de l'API vers la liste d'ItemFilm
                    val listeFilms = mapApiResultToItemFilms(reponse.body()!!)

                    Log.d("Liste des films", listeFilms.toString())

                    lifecycleScope.launch {

                        var liste = listeFilms


                            pageVide.visibility = View.INVISIBLE


                        Log.d("Film liste", liste.toString())

                        data.listFilmApi.value = liste

                    }
                } catch (e: SocketTimeoutException) {
                    Toast.makeText(
                        requireContext(),
                        "Erreur: Réseau indisponible",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

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

}

