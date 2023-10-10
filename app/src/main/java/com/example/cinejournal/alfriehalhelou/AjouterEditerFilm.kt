package com.example.cinejournal.alfriehalhelou

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.properties.Delegates
import androidx.core.content.FileProvider
import androidx.core.net.toUri


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
    lateinit var imageView: ImageView


    private fun creerUriPhoto(): Uri {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val photoFile =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$timeStamp.jpg")
        return photoFile.toUri()
    }

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


        val selectionPhoto =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
                if (uri != null) {

                    val imageLocale = creerUriPhoto()
                    val inputStream = contentResolver.openInputStream(uri)
                    val outputStream = contentResolver.openOutputStream(imageLocale)

                    inputStream?.use { input ->
                        outputStream.use { output ->
                            output?.let { input.copyTo(it) }
                        }
                    }

                    imageView.setImageURI(imageLocale)

                    // On fait quelque chose avec l'image reçue sous forme d'URI
                    imageView.setImageURI(uri)

                }
            }
        imageView = findViewById(R.id.imageNouveauFilm)

        boutonAjouterImage.setOnClickListener() {

            selectionPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))


        }



        boutonSauvegarder.setOnClickListener {

            val titre = modifierNomFilm.text.toString()
            val slogan = modifierSloganFilm.text.toString()
            val annee = null
            if (annee != "") {
                modifierAnneeFilm.text.toString().toIntOrNull()
            }
            val note = modifierNoteFilm.rating
            val films = Film(null, titre, slogan, annee, note, null)

            Log.d("AAA", "$titre, $slogan, $annee, $note")

            if (titre != "") {
                lifecycleScope.launch(Dispatchers.IO) {
                    val database: AppDatabase = AppDatabase.getDatabase(applicationContext)
                    database.FilmDao().insertAll(films)
                }

                val intent = Intent(this, ListeDeFilms::class.java);
                startActivity(intent)
            } else {
                val toast = Toast.makeText(
                    applicationContext,
                    "Ajoutez au moins le titre du film",
                    LENGTH_SHORT
                )
                toast.show()
            }
        }

        boutonAnnuler.setOnClickListener() {
            val intent = Intent(this, ListeDeFilms::class.java);
            startActivity(intent)
        }


    }
}