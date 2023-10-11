package com.example.cinejournal.alfriehalhelou

import android.graphics.Bitmap
import android.widget.ImageView

data class ItemFilm(
    val uid: Int,
    val titre: String,
    val slogan: String,
    val annee: Int,
    val note: Float,
    val image: String
) {

}
