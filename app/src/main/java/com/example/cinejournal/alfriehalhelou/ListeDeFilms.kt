package com.example.cinejournal.alfriehalhelou

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ListeDeFilms : AppCompatActivity() {
    lateinit var films: Film

    lateinit var recyclerView: RecyclerView
    lateinit var adapteur: FilmAdapteur
    private fun creerUriPhoto(): Uri {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val photoFile =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$timeStamp.jpg")
        val imageUri = photoFile.toUri()
        //  imageNouveauFilm.setImageURI(imageUri)
        return imageUri
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activite_liste_film)

        var toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        var titre: TextView = findViewById(R.id.mes_films)
        titre.text = "Mes films"

        var tri: TextView = findViewById(R.id.tri)
        tri.text = "Trié par l'ordre d'ajout"

        var pageVide: TextView = findViewById(R.id.pageVide)
//        pageVide.visibility= View.VISIBLE


        val recycleThread = (lifecycleScope.launch {
            recyclerView = findViewById(R.id.listeFilms)
            val database: AppDatabase = AppDatabase.getDatabase(applicationContext)
            var liste = database.FilmDao().getAll()
            if (liste.isEmpty()) {
                pageVide.visibility = View.VISIBLE
            } else {
                pageVide.visibility = View.INVISIBLE

            }
            Log.d("AAA", liste.toString())
            adapteur = FilmAdapteur(applicationContext, ListeDeFilms(), liste)  // liste

            recyclerView.adapter = adapteur

        })

//        if (recyclerView != null) {
//            pageVide.visibility = View.VISIBLE
//        } else {
//            pageVide.visibility = View.INVISIBLE
//        }

        var ajouter: Button = findViewById(R.id.ajouter)
        ajouter.setOnClickListener()
        {
            val intent = Intent(this, AjouterEditerFilm::class.java)
            startActivity(intent)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.aPropos -> {
                Log.d("MonTag", "Clic sur À Propos")
                val intent = Intent(this, APropos::class.java);
                startActivity(intent)
            }

            R.id.trouverUnFilm -> {
                Log.d("MonTag", "Clic sur Trouver un film")
                val intent = Intent(this, RechercheFilm::class.java);
                startActivity(intent)
            }

            R.id.toutSupprimer -> {
                var pageVide: TextView = findViewById(R.id.pageVide)

                val alert = AlertDialog.Builder(this).create()
                alert.setTitle("Supprimer Tout")
                alert.setMessage("Êtes-vous sûre???")
                alert.apply {
                    setButton(
                        AlertDialog.BUTTON_POSITIVE, "oui",
                        DialogInterface.OnClickListener { dialog, id ->

                            lifecycleScope.launch {
                                deleteAll()
                            }

                            pageVide.visibility = View.VISIBLE

                            adapteur.films = listOf()
                            adapteur.notifyDataSetChanged()
                        }
                    )

                    setButton(
                        AlertDialog.BUTTON_NEGATIVE, "non",
                        DialogInterface.OnClickListener { dialog, id -> })
                }.show()

            }

            R.id.titre -> {
                var tri: TextView = findViewById(R.id.tri)
                tri.text = "Trié par titre"
                lifecycleScope.launch {
                    val filmsTries = withContext(Dispatchers.IO) {
                        val database: AppDatabase =
                            AppDatabase.getDatabase(applicationContext)
                        database.FilmDao().trierParTitre()
                    }
                    adapteur.mettreAJour(filmsTries) // Mettre à jour la liste de films
                }
            }

            R.id.note -> {
                var tri: TextView = findViewById(R.id.tri)
                tri.text = "Trié par note"
                lifecycleScope.launch {
                    val filmsTries = withContext(Dispatchers.IO) {
                        val database: AppDatabase =
                            AppDatabase.getDatabase(applicationContext)
                        database.FilmDao().trierParNote()
                    }
                    adapteur.mettreAJour(filmsTries) // Mettre à jour la liste de films

                }

            }

            R.id.annee -> {
                var tri: TextView = findViewById(R.id.tri)
                tri.text = "Trié par année"
                lifecycleScope.launch {
                    val filmsTries = withContext(Dispatchers.IO) {
                        val database: AppDatabase =
                            AppDatabase.getDatabase(applicationContext)
                        database.FilmDao().trierParAnnee()
                    }
                    adapteur.mettreAJour(filmsTries) // Mettre à jour la liste de films
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private suspend fun deleteAll() = withContext(Dispatchers.IO) {
        val database: AppDatabase =
            AppDatabase.getDatabase(applicationContext)

        database.FilmDao().deleteAllData()


    }

}