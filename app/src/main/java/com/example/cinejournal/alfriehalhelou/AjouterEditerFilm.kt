package com.example.cinejournal.alfriehalhelou

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class AjouterEditerFilm : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapteur: FilmAdapteur
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

            val titre = modifierNomFilm.text.toString()
            val slogan = modifierSloganFilm.text.toString()
            val annee = modifierAnneeFilm.text.toString().toInt()
            val note = modifierNoteFilm.rating
            val films = Film(null, titre, slogan, annee, note, null)
            lifecycleScope.launch(Dispatchers.IO) {
                val database: AppDatabase = AppDatabase.getDatabase(applicationContext)
                database.FilmDao().insertAll(films)
            }
            val intent = Intent(this, ListeDeFilms::class.java);
            startActivity(intent)

        }

        boutonAnnuler.setOnClickListener() {

        }
    }
}