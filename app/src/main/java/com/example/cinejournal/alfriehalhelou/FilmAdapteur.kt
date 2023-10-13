package com.example.cinejournal.alfriehalhelou

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.room.Database
import kotlinx.coroutines.launch
import java.io.File

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
            // TODO: Toast qui affiche film.titre ou position
            //Toast.makeText(ctx, "$position", Toast.LENGTH_SHORT).show()
            val intent = Intent(ctx, AjouterEditerFilm::class.java)
            intent.putExtra("FILM_ID", film.uid)
            ctx.startActivity(intent)
        }
        val imagePath = "/storage/self/primary/DCIM/Camera/IMG_20231011_112906.jpg"
        val imageUri = Uri.parse("file://$imagePath")
        holder.imagFilm.setImageURI(imageUri)
        //holder.imagFilm.setImageURI("/storage/self/primary/DCIM/Camera/IMG_20231011_112906.jpg".toUri())
      //  Log.d("imagFilm" , holder.imagFilm.toString())
        Log.d("imagFilm" , holder.imagFilm.setImageURI(imageUri).toString() )
        //setImageResource(film.image, holder.imagFilm)
        holder.nomFilm.text = film.titre
        holder.sloganFilm.text = film.slogan
        holder.noteFilm.rating = film.note
    }

    fun mettreAJour(films: List<ItemFilm>) {
        this.films = films
        notifyDataSetChanged()
    }
//    fun setImageResource(uri: String, imageView: ImageView) {
//        Picasso.get()
//            .load(Uri.parse(uri))
//            .into(imageView)
//    }
}






