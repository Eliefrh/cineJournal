package ca.qc.bdeb.c5gm.cinejournal.alfriehalhelou

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Suppress("NAME_SHADOWING")
class AjouterEditerFilm : AppCompatActivity() {


    var film: Film? = null
    private lateinit var boutonPosition: Button
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


    val data: FilmViewModel by viewModels()


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

        Log.d("uri image ", data.selectedImageUri.value.toString())


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
        imageNouveauFilm.setImageURI(data.selectedImageUri.value)

        var textLatitude: TextView = findViewById(R.id.textViewLatitude)
        var textLongitude: TextView = findViewById(R.id.textViewLongitude)
        var sloganSet = false


        boutonPosition = findViewById(R.id.buttonObtenirPosition)
        boutonPosition.setOnClickListener() {
            val intent = Intent(this, MapActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_MAP)
            intent.putExtra("FILM_ID", film?.uid)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            this.startActivity(intent)
        }

        //watchers pour les changement des textes
        //titre
        modifierNomFilm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence?, start: Int, before: Int, count: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable?) {
                val nouveauTitre = editable.toString()
                data.updateFilmTitle(nouveauTitre)


            }
        })

        //slogan
        modifierSloganFilm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence?, start: Int, before: Int, count: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable?) {
                val nouveauSlogan = editable.toString()
                data.updateFilmSlogan(nouveauSlogan)
            }
        })

        //annee
        modifierAnneeFilm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence?, start: Int, before: Int, count: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable?) {
                val nouveauAnnee = editable.toString()

                if (nouveauAnnee.length > 4) {
                    modifierAnneeFilm.setText(nouveauAnnee.substring(0, 4))
                    modifierAnneeFilm.setSelection(4)   // placer le curseur à la fin
                    Toast.makeText(applicationContext, "4 chiffres maximum", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    data.updateFilmYear(nouveauAnnee.toIntOrNull() ?: 0)
                }
            }
        })

        //latitude
        textLatitude.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence?, start: Int, before: Int, count: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable?) {
                val nouveauLatitude = editable.toString()
                data.lattitude.value = nouveauLatitude.toDoubleOrNull()
            }
        })

        //longitude
        textLongitude.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence?, start: Int, before: Int, count: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable?) {
                val nouveauLongitude = editable.toString()
                data.longitude.value = nouveauLongitude.toDoubleOrNull()
            }
        })


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


                    data.updateSelectedImageUri(imageLocale)
                    imageNouveauFilm.setImageURI(data.selectedImageUri.value)
                    image = data.selectedImageUri.value
                    Log.d("AAA", imageLocale.toString())


                }
            }

        boutonAjouterImage.setOnClickListener {
            selectionPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        val intent = intent
        val filmId = intent.getIntExtra("FILM_ID", -1)
        film?.annee?.toString() ?: ""

        imageNouveauFilm.setImageURI(data.selectedImageUri.value)


        if (filmId != -1) {



            // Si l'ID du film est valide, c'est une édition
            lifecycleScope.launch {
                film = withContext(Dispatchers.IO) { database.FilmDao().loadById(filmId) }

                if (!data.estDejaCharge()) {
                    film?.let {
                        data.initializeWithFilmData(it)
                    }
                }

                text.text = "Modifier Un Film"

                film?.let {
                    Log.d("Elie uri 1 ", image.toString())

                    val anneeFilm = film?.annee?.toString() ?: ""
                    // Mettez à jour les champs avec les données du film
                    modifierNomFilm.setText(data.filmTitle.value)
                    modifierSloganFilm.setText(data.filmSlogan.value)
                    modifierAnneeFilm.setText(data.filmYear.value.toString())
                    modifierNoteFilm.rating = data.filmRating.value ?: 0.0f
                    image = data.selectedImageUri.value
                    imageNouveauFilm.setImageURI(image)
                    Picasso.get().load(image).into(imageNouveauFilm)

                    textLatitude.text = data.lattitude.value?.toString() ?: "0"
                    textLongitude.text = data.longitude.value?.toString() ?: "0"

                    Log.d("Elie uri", image.toString())

                    data.updateSelectedImageUri(image)

                }

                modifierSloganFilm.setText(data.filmSlogan.value)


            }



            if (modifierNomFilm.text != null) {

                // le jeton de l'API
                BuildConfig.API_KEY_TMBD

                lifecycleScope.launch {
                    val reponse = withContext(Dispatchers.IO) {
                        ApiClient.apiService.getMovieDetailsById(filmId)
                    }

                    if (reponse.isSuccessful) {
                        data.updateFilmSlogan(reponse.body()!!.tagline)
                        Log.d("Elie slogan", reponse.body()!!.tagline)
                        modifierSloganFilm.setText(reponse.body()!!.tagline)
                    }else{
                        data.updateFilmSlogan("")
                    }
                    sloganSet = true

                }

                val filmId = intent.extras?.getInt("FILM_ID", -1)

                val filmTitre = intent.extras?.getString("FILM_TITRE", "")
               // val filmSlogan = data.filmSlogan.value.toString()?:""
                val filmAnnee = intent.extras?.getInt("FILM_ANNEE", 0)
                val filmNote = intent.extras?.getFloat("FILM_NOTE", 0.0f)
                val filmImage = intent.extras?.getString("FILM_IMAGE", "")


                modifierNomFilm.setText(filmTitre)
              //  modifierSloganFilm.setText(filmSlogan)
                modifierAnneeFilm.setText(filmAnnee.toString())
                modifierNoteFilm.rating = filmNote ?: 0.0f
                image = filmImage?.toUri()

                if (image != null && image != Uri.EMPTY) {
                    data.updateSelectedImageUri(image)
                }

                Log.d("update viewmodel uri", data.selectedImageUri.value.toString())

                Picasso.get().load(image).into(imageNouveauFilm)
                textLatitude.text = data.lattitude.value?.toString() ?: "0"
                textLongitude.text = data.longitude.value?.toString() ?: "0"


            }


        }


        //listener boutonsauvegarder
        //faire un film et l'inserer dans la base de donnees
        //s'assurer que les champs principale sont remplis
        boutonSauvegarder.setOnClickListener {

            val titre = modifierNomFilm.text.toString()
            val slogan = modifierSloganFilm.text.toString()
            val annee = modifierAnneeFilm.text.toString().toInt()
            val note = modifierNoteFilm.rating
            val latitude = textLatitude.text.toString().toDoubleOrNull()
            val longitude = textLongitude.text.toString().toDoubleOrNull()
            val image = data.selectedImageUri.value

//            if (filmInternet == true){
//
//            }
            data.dejaCharge = false


            if (titre != "" && annee != 0) {
                lifecycleScope.launch(Dispatchers.IO) {
                    if (film == null) {
                        val nouveauFilm = Film(
                            null, titre, slogan, annee, note, image.toString(), latitude, longitude
                        )
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
                            it.latitude = latitude
                            it.longitude = longitude

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
                    data.dejaCharge = false

                    val toast = Toast.makeText(
                        applicationContext, "Film modifié avec succès", LENGTH_SHORT
                    )
                    toast.show()
                } else {
                    data.dejaCharge = false

                    val toast = Toast.makeText(
                        applicationContext, "Film ajouté avec succès", LENGTH_SHORT
                    )
                    toast.show()
                }


                val intent = Intent(this, ListeDeFilms::class.java)
                startActivity(intent)
            } else {
                data.dejaCharge = false

                val toast = Toast.makeText(
                    applicationContext,
                    "Remplissez au moins les champs avec un etoile (*) svp",
                    LENGTH_SHORT
                )
                toast.show()
            }
            data.dejaCharge = false

        }

        boutonAnnuler.setOnClickListener {
            data.dejaCharge = false
            val intent = Intent(this, ListeDeFilms::class.java)
            startActivity(intent)

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var textLatitude: TextView = findViewById(R.id.textViewLatitude)
        var textLongitude: TextView = findViewById(R.id.textViewLongitude)
        if (requestCode == REQUEST_CODE_MAP && resultCode == Activity.RESULT_OK) {
            // Récupérer les données retournées par l'activité MapActivity
            val latitude = data?.getDoubleExtra("latitude", 0.0)
            val longitude = data?.getDoubleExtra("longitude", 0.0)

            // Faire quelque chose avec les données (sauvegarde, affichage, etc.)
            if (latitude != null && longitude != null) {
                // Exemple : Afficher les données dans un Toast

                textLatitude.text = latitude.toString()
                textLongitude.text = longitude.toString()

                val message = "Latitude: $latitude\nLongitude: $longitude"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_MAP = 1
    }

}