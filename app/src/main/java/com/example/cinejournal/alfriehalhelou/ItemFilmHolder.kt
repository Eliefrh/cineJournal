package com.example.cinejournal.alfriehalhelou

import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class ItemFilmHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val layout: ConstraintLayout
    val imagFilm: ImageView
    val nomFilm: TextView
    val sloganFilm: TextView
    val noteFilm: RatingBar

    init {
        layout = itemView.findViewById(R.id.layout)
        imagFilm = itemView.findViewById(R.id.affichageImageFilm)
        nomFilm = itemView.findViewById(R.id.affichageTitreFilm)
        sloganFilm = itemView.findViewById(R.id.afficherSloganFilm)
        noteFilm = itemView.findViewById(R.id.afficherNoteFilm)
    }
}
