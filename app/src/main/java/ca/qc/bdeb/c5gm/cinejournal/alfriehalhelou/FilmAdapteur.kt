package ca.qc.bdeb.c5gm.cinejournal.alfriehalhelou

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
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
            // TODO: Toast qui affiche film.titre ou position
            //Toast.makeText(ctx, "$position", Toast.LENGTH_SHORT).show()
            val intent = Intent(ctx, AjouterEditerFilm::class.java)
            intent.putExtra("FILM_ID", film.uid)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ctx.startActivity(intent)
        }

        holder.nomFilm.text = film.titre + " (" + film.annee + ")"
        holder.sloganFilm.text = film.slogan
        holder.noteFilm.rating = film.note

        holder.imagFilm.setImageURI(Uri.parse(film.image))
    }

    fun mettreAJour(films: List<ItemFilm>) {
        this.films = films
        notifyDataSetChanged()
    }
}





