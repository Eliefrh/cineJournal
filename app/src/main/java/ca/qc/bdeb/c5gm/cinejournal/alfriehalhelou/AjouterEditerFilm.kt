package ca.qc.bdeb.c5gm.cinejournal.alfriehalhelou

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Suppress("NAME_SHADOWING")
class AjouterEditerFilm : AppCompatActivity() {

    override fun onResume() {
        super.onResume()

        filmViewModel.selectedImageUri.observe(this) { uri ->
            uri?.let {
                imageNouveauFilm.setImageURI(it)
                // filmViewModel.updateSelectedImageUri(it)
            }
        }
        filmViewModel.filmTitle.observe(this) { title ->
            title?.let {
                modifierNomFilm.setText(it)
                // filmViewModel.updateFilmTitle(it)
            }
        }
        filmViewModel.filmSlogan.observe(this) { slogan ->
            slogan?.let {
                modifierSloganFilm.setText(it)
                //  filmViewModel.updateFilmSlogan(it)
            }
        }
        filmViewModel.filmYear.observe(this) { year ->
            year?.let {
                modifierAnneeFilm.setText(it.toString())
                //filmViewModel.updateFilmYear(it)
            }
        }
        filmViewModel.filmRating.observe(this) { rating ->
            rating?.let {
                modifierNoteFilm.rating = it
                //filmViewModel.updateFilmRating(it)
            }
        }
    }

    var film: Film? = null
    private lateinit var modifierNomFilm: EditText
    private lateinit var modifierSloganFilm: EditText
    private lateinit var modifierAnneeFilm: EditText
    private lateinit var modifierNoteFilm: RatingBar
    private lateinit var imageNouveauFilm: ImageView
    private lateinit var boutonAjouterImage: Button
    private lateinit var boutonAnnuler: Button
    private lateinit var boutonSauvegarder: Button
    private lateinit var modifierImageFilm: ImageView
    private var image: Uri? = null


    private lateinit var filmViewModel: FilmViewModel

    //creation d'uri pour l'image choisi
    private fun creerUriPhoto(): Uri {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val photoFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$timeStamp.jpg")
        Log.d("Elie photofile", photoFile.toString())
        val imageUri = photoFile.toUri()
        Log.d("Elie imageUri", imageUri.toString())

        return imageUri
    }

    @SuppressLint("SetTextI18n", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajouter_editer_film)

        val toolbar: Toolbar = findViewById(R.id.toolbar_ajouter_editer)
        setSupportActionBar(toolbar)
        val database: AppDatabase = AppDatabase.getDatabase(applicationContext)

        modifierNomFilm = findViewById(R.id.editTextTitreFilm)
        modifierSloganFilm = findViewById(R.id.editTextSloganFilm)
        modifierAnneeFilm = findViewById(R.id.editTextAnneeFilm)
        modifierNoteFilm = findViewById(R.id.ratingBar)
        imageNouveauFilm = findViewById(R.id.imageNouveauFilm)
        boutonAjouterImage = findViewById(R.id.buttonAjouterImage)
        boutonAnnuler = findViewById(R.id.buttonAnnuler)
        boutonSauvegarder = findViewById(R.id.buttonSauvegarder)
        modifierImageFilm = findViewById(R.id.imageNouveauFilm)
        val text: TextView = findViewById(R.id.textViewNouveauFilm)

        filmViewModel = ViewModelProvider(this)[FilmViewModel::class.java]


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


                    filmViewModel.updateSelectedImageUri(imageLocale)
                    imageNouveauFilm.setImageURI(filmViewModel.selectedImageUri.value)
                    image = filmViewModel.selectedImageUri.value
                    Log.d("AAA", imageLocale.toString())


                }
            }

        boutonAjouterImage.setOnClickListener {
            selectionPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        val intent = intent
        val filmId = intent.getIntExtra("FILM_ID", -1)
        film?.annee?.toString() ?: ""

        if (filmId != -1) {

            // Si l'ID du film est valide, c'est une édition
            lifecycleScope.launch {
                film = withContext(Dispatchers.IO) { database.FilmDao().loadById(filmId) }
                text.text = "Modifier Un Film"

                film?.let {
                    filmViewModel.initializeWithFilmData(it)

                    val anneeFilm = film?.annee?.toString() ?: ""
                    // Mettez à jour les champs avec les données du film
                    modifierNomFilm.setText(filmViewModel.filmTitle.value)
                    modifierSloganFilm.setText(filmViewModel.filmSlogan.value)
                    modifierAnneeFilm.setText(filmViewModel.filmYear.value.toString())
                    modifierNoteFilm.rating = filmViewModel.filmRating.value ?: 0.0f
                    filmViewModel.updateSelectedImageUri(image)
                    image = filmViewModel.selectedImageUri.value
                    //database.FilmDao().getImage(filmId).toString().toUri()
                    imageNouveauFilm.setImageURI(image)

//
//                    Log.d("Elie uri", image.toString())


                }

            }
        }

        //listener boutonsauvegarder
        //faire un film et l'inserer dans la base de donnees
        //s'assurer que les champs principale sont remplis
        boutonSauvegarder.setOnClickListener {

            val titre = modifierNomFilm.text.toString()
            val slogan = modifierSloganFilm.text.toString()
            val annee = modifierAnneeFilm.text.toString().toIntOrNull()
            val note = modifierNoteFilm.rating

            if (titre != "" && annee != null) {
                lifecycleScope.launch(Dispatchers.IO) {
                    if (film == null) {
                        val nouveauFilm = Film(null, titre, slogan, annee, note, image.toString())
                        withContext(Dispatchers.IO) {
                            database.FilmDao().insertAll(nouveauFilm)
                        }
                    } else {
                        film?.let {
                            it.titre = titre
                            it.slogan = slogan
                            it.annee = annee
                            it.note = note
                            it.image = image.toString()

                            withContext(Dispatchers.IO) {
                                database.FilmDao().updateAll(it)
                            }
                        }
                    }
                    setResult(Activity.RESULT_OK)
                    finish()
                }

                //correction du premiere tp ( affichage de toast en cas de succes)
                if (text.text == "Modifier Un Film") {
                    val toast = Toast.makeText(
                        applicationContext,
                        "Film modifié avec succès",
                        LENGTH_SHORT
                    )
                    toast.show()
                } else {
                    val toast = Toast.makeText(
                        applicationContext,
                        "Film ajouté avec succès",
                        LENGTH_SHORT
                    )
                    toast.show()
                }


                val intent = Intent(this, ListeDeFilms::class.java)
                startActivity(intent)
            } else {
                val toast = Toast.makeText(
                    applicationContext,
                    "Remplissez au moins les champs avec un etoile (*) svp",
                    LENGTH_SHORT
                )
                toast.show()
            }
        }

        boutonAnnuler.setOnClickListener {
            val intent = Intent(this, ListeDeFilms::class.java)
            startActivity(intent)
        }
    }
}