package com.example.cinejournal.alfriehalhelou

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class FilmAdapteur(val ctx: Context, val activity: ListeDeFilms, var films: List<ItemFilm>) :
    RecyclerView.Adapter<ItemFilmHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFilmHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.liste_films_item, parent, false)
        return ItemFilmHolder(view)
    }

    override fun getItemCount(): Int {
        return films.size
    }

    override fun onBindViewHolder(holder: ItemFilmHolder, position: Int) {
        val film = films[position]
        holder.imagFilm.setImageResource(film.image)
        holder.nomFilm.text = film.nom
        holder.sloganFilm.text = film.slogan
        holder.noteFilm.rating = film.note

//        holder.layout.setOnClickListener {
//            Toast.makeText(ctx, "On a cliqué sur le film ${film.nom}", Toast.LENGTH_SHORT).show()
//        }
    }
}


