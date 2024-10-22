package ca.qc.bdeb.c5gm.cinejournal.alfriehalhelou

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class FilmAdapteur(val ctx: Context, val activity: Activity, var films: List<ItemFilm>) :

    RecyclerView.Adapter<ItemFilmHolder>() {
    var listeRechercheInternet: List<ItemFilm> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFilmHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.liste_films_item, parent, false)
        return ItemFilmHolder(view)

    }

    override fun getItemCount(): Int {
        return films.size
    }

    override fun onBindViewHolder(holder: ItemFilmHolder, position: Int) {
        try {
            var film = films[position]

            if (activity is RechercheFilm) {
                holder.layout.setOnClickListener {

                    val intent = Intent(ctx, AjouterEditerFilm::class.java)
                    intent.putExtra("FILM_ID", film.uid)
                    intent.putExtra("FILM_TITRE", film.titre)
                    intent.putExtra("FILM_SLOGAN", film.slogan)
                    intent.putExtra("FILM_ANNEE", film.annee)
                    intent.putExtra("FILM_NOTE", 0)
                    intent.putExtra("FILM_IMAGE", film.image)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    ctx.startActivity(intent)

                }
            } else {
                holder.layout.setOnClickListener {

                    val intent = Intent(ctx, AjouterEditerFilm::class.java)
                    intent.putExtra("FILM_ID", film.uid)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    ctx.startActivity(intent)
                }
            }

            holder.nomFilm.text = film.titre + " (" + film.annee + ")"
            if (film.titre.length > 10) {
                holder.nomFilm.text =
                    film.titre.substring(0, 10) + "  ..." + " (" + film.annee + ")"
            }

            holder.sloganFilm.text = film.slogan
            if (film.slogan.length > 30) {
                holder.sloganFilm.text = film.slogan.substring(0, 30) + " ..."
            }

            holder.noteFilm.rating = film.note

            holder.imagFilm.setImageURI(Uri.parse(film.image))

            Picasso.get().load(film.image).into(holder.imagFilm)

            if (activity is RechercheFilm) {
                holder.sloganFilm.visibility = android.view.View.INVISIBLE
                holder.noteFilm.rating = 0f
                Picasso.get().load(film.image).into(holder.imagFilm)

            }
        } catch (e: Exception) {
        }
    }

    fun mettreAJour(films: List<ItemFilm>) {
        this.films = films
        notifyDataSetChanged()
    }

    fun ajouterFilm(film: List<ItemFilm>) {
        listeRechercheInternet = film
        this.listeRechercheInternet = emptyList()
        notifyDataSetChanged()
    }
}







