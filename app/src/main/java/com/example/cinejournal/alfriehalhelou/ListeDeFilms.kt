package com.example.cinejournal.alfriehalhelou

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListeDeFilms : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapteur: FilmAdapteur


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activite_liste_film)

        var toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        var titre: TextView = findViewById(R.id.mes_films)
        titre.text = "Mes films"


        lifecycleScope.launch {
            recyclerView = findViewById(R.id.listeFilms)
            val database: AppDatabase = AppDatabase.getDatabase(applicationContext)
            var liste = database.FilmDao().getAll()
            adapteur = FilmAdapteur(applicationContext, ListeDeFilms(), liste)
            recyclerView.adapter = adapteur

        }

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

    private fun setButton(buttonNegative: Int, s: String) {

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
                val alert = AlertDialog.Builder(this).create()
                alert.setTitle("Supprimer Tout")
                alert.setMessage("Êtes-vous sûre???")
                alert.apply {
                    setButton(AlertDialog.BUTTON_POSITIVE, "oui",
                    DialogInterface.OnClickListener { dialog, id ->
                        lifecycleScope.launch {
                            deleteAll()
                        }
                        adapteur.films = listOf()
                        adapteur.notifyDataSetChanged()
                    })
                    setButton(AlertDialog.BUTTON_NEGATIVE, "non",
                        DialogInterface.OnClickListener { dialog, id -> })
                }.show()

            }

            R.id.titre -> {
                lifecycleScope.launch {
                    val filmsTries = withContext(Dispatchers.IO) {
                        val database: AppDatabase =
                            AppDatabase.getDatabase(applicationContext)
                        database.FilmDao().trierParTitre()
                    }
                    adapteur.mettreAJour(filmsTries)
                }
            }

            R.id.note -> {
                lifecycleScope.launch {
                    val filmsTries = withContext(Dispatchers.IO) {
                        val database: AppDatabase =
                            AppDatabase.getDatabase(applicationContext)
                        database.FilmDao().trierParNote()
                    }
                    adapteur.mettreAJour(filmsTries)
                }
            }

            R.id.annee -> {
                lifecycleScope.launch {
                    val filmsTries = withContext(Dispatchers.IO) {
                        val database: AppDatabase =
                            AppDatabase.getDatabase(applicationContext)
                        database.FilmDao().trierParAnnee()
                    }
                    adapteur.mettreAJour(filmsTries)
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