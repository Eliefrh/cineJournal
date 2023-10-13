package com.example.cinejournal.alfriehalhelou

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FilmAdapteur(val ctx: Context, val activity: Activity, var films: List<ItemFilm>) :
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

        holder.layout.setOnClickListener {
            val intent = Intent(ctx, AjouterEditerFilm::class.java)
            intent.putExtra("FILM_ID", film.uid)
            ctx.startActivity(intent)
        }

        holder.imagFilm.setImageResource(film.image)
        holder.nomFilm.text = film.titre
        holder.sloganFilm.text = film.slogan
        holder.noteFilm.rating = film.note
    }

    fun mettreAJour(films: List<ItemFilm>) {
        this.films = films
        notifyDataSetChanged()
    }
}

private fun Any.setImageResource(image: Bitmap) {
    TODO("Not yet implemented")
}


