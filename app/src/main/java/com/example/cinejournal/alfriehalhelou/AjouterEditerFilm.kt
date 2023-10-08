package com.example.cinejournal.alfriehalhelou

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import androidx.appcompat.widget.Toolbar

class AjouterEditerFilm : AppCompatActivity() {


    lateinit var modifierNomFilm: EditText
    lateinit var modifierSloganFilm: EditText
    lateinit var modifierAnneeFilm: EditText
    lateinit var modifierNoteFilm: RatingBar
    lateinit var imageNouveauFilm: ImageView
    lateinit var boutonAjouterImage: Button
    lateinit var boutonAnnuler: Button
    lateinit var boutonSauvegarder: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajouter_editer_film)

        var toolbar: Toolbar = findViewById(R.id.toolbar_ajouter_editer)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Ajouter/Éditer un film"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        modifierNomFilm = findViewById(R.id.editTextTitreFilm)
        modifierSloganFilm = findViewById(R.id.editTextSloganFilm)
        modifierAnneeFilm = findViewById(R.id.editTextAnneeFilm)
        modifierNoteFilm = findViewById(R.id.ratingBar)
        imageNouveauFilm = findViewById(R.id.imageNouveauFilm)
        boutonAjouterImage = findViewById(R.id.buttonAjouterImage)
        boutonAnnuler = findViewById(R.id.buttonAnnuler)
        boutonSauvegarder = findViewById(R.id.buttonSauvegarder)


        
        boutonAjouterImage.setOnClickListener() {

        }

        boutonSauvegarder.setOnClickListener() {

        }

        boutonAnnuler.setOnClickListener() {

        }
    }
}