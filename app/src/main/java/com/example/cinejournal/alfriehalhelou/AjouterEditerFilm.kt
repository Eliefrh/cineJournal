package com.example.cinejournal.alfriehalhelou

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import androidx.room.Database
import kotlinx.coroutines.withContext


class AjouterEditerFilm : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapteur: FilmAdapteur
    var film: Film? = null
    lateinit var modifierNomFilm: EditText
    lateinit var modifierSloganFilm: EditText
    lateinit var modifierAnneeFilm: EditText
    lateinit var modifierNoteFilm: RatingBar
    lateinit var imageNouveauFilm: ImageView
    lateinit var boutonAjouterImage: Button
    lateinit var boutonAnnuler: Button
    lateinit var boutonSauvegarder: Button
    lateinit var modifierImageFilm: ImageView
    lateinit var image: Uri


    private fun creerUriPhoto(): Uri {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val photoFile =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$timeStamp.jpg")
        Log.d("Elie photofile", photoFile.toString())
        val imageUri = photoFile.toUri()
        Log.d("Elie imageUri", imageUri.toString())

        return imageUri
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajouter_editer_film)

        var toolbar: Toolbar = findViewById(R.id.toolbar_ajouter_editer)
        setSupportActionBar(toolbar)
        val database: AppDatabase =
            AppDatabase.getDatabase(applicationContext)

        modifierNomFilm = findViewById(R.id.editTextTitreFilm)
        modifierSloganFilm = findViewById(R.id.editTextSloganFilm)
        modifierAnneeFilm = findViewById(R.id.editTextAnneeFilm)
        modifierNoteFilm = findViewById(R.id.ratingBar)
        imageNouveauFilm = findViewById(R.id.imageNouveauFilm)
        boutonAjouterImage = findViewById(R.id.buttonAjouterImage)
        boutonAnnuler = findViewById(R.id.buttonAnnuler)
        boutonSauvegarder = findViewById(R.id.buttonSauvegarder)
        modifierImageFilm = findViewById(R.id.imageNouveauFilm)


        val selectionPhoto =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
                if (uri != null) {

                    val imageLocale = creerUriPhoto()
                    val inputStream = contentResolver.openInputStream(uri)
                    val outputStream = contentResolver.openOutputStream(imageLocale)
                    Log.d("Elie input", uri.toString())
                    Log.d("Elie output", imageLocale.toString())


                    inputStream?.use { input ->
                        outputStream.use { output ->
                            output?.let { input.copyTo(it) }
                        }
                    }

                    imageNouveauFilm.setImageURI(imageLocale)
                    image = imageLocale
                    Log.d("AAA", imageLocale.toString())

                }
            }
        //imageView = findViewById(R.id.imageNouveauFilm)

        boutonAjouterImage.setOnClickListener() {

            selectionPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))


        }

        //database = AppDatabase.getDatabase(this).FilmDao()
        val intent = intent
        val filmId = intent.getIntExtra("FILM_ID", -1)
        val anneeFilm = film?.annee?.toString() ?: ""
        image = Uri.EMPTY

        if (filmId != -1) {
            // Si l'ID du film est valide, c'est une édition
            lifecycleScope.launch {
                film = withContext(Dispatchers.IO) { database.FilmDao().loadById(filmId) }

                film?.let {
                    val anneeFilm = film?.annee?.toString() ?: ""
                    // Mettez à jour les champs avec les données du film
                    modifierNomFilm.setText(it.titre)
                    modifierSloganFilm.setText(it.slogan)
                    modifierAnneeFilm.setText(anneeFilm)
                    modifierNoteFilm.rating = it.note ?: 0.0f
                    imageNouveauFilm.setImageURI(Uri.parse(it.image))


                }
            }
        }

        boutonSauvegarder.setOnClickListener {

            val titre = modifierNomFilm.text.toString()
            val slogan = modifierSloganFilm.text.toString()
            val annee = modifierAnneeFilm.text.toString().toIntOrNull()
            val note = modifierNoteFilm.rating


            //    Log.d("AAA", "$titre, $slogan, $annee, $note")

            if (titre != "") {
                lifecycleScope.launch(Dispatchers.IO) {
                    if (film == null) {
                        val nouveauFilm =
                            Film(null, titre, slogan, annee, note, image.toString())
                        withContext(Dispatchers.IO) {
                            database.FilmDao().insertAll(nouveauFilm)
                        }
                    } else {
                        film?.let {
                            it.titre = titre
                            it.slogan = slogan
                            it.annee = annee
                            it.note = note

                            withContext(Dispatchers.IO) {
                                database.FilmDao().updateAll(it)

                            }
                        }
                    }
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                val intent = Intent(this, ListeDeFilms::class.java);
                startActivity(intent)
            } else {
                val toast = Toast.makeText(
                    applicationContext,
                    "Remplissez toutes les champs svp",
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