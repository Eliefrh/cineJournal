package ca.qc.bdeb.c5gm.cinejournal.alfriehalhelou

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch


class affichageFilmFragment : Fragment() {

    val data: FilmViewModel by activityViewModels()

    lateinit var adapteur: FilmAdapteur
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_affichage_film, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.listeFilms)

        adapteur = FilmAdapteur(requireContext(), RechercheFilm(), listOf())
        recyclerView.adapter = adapteur


        data.listFilmApi.observe(viewLifecycleOwner) { it ->
            it?.let { listeFilm ->

                var liste = listeFilm

                Log.d("Film liste", liste.toString())
                adapteur.films = liste
                adapteur.notifyDataSetChanged()
            }
        }
    }
}