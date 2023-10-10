package com.example.cinejournal.alfriehalhelou

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
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

        var tri: TextView = findViewById(R.id.tri)
        //tri.text = "Trier par ${}"


        val films = Film(null, "hello", "it's me", 2015, 3.5f, null)

        fun recycleThread() {
            lifecycleScope.launch(Dispatchers.IO) {
                val database: AppDatabase =
                    AppDatabase.getDatabase(applicationContext)
                database.FilmDao().insertAll(films)
                var liste = database.FilmDao().getAll()
                // database.FilmDao().delete()
                runOnUiThread {
                    Log.d("film", "Films sont ajouté dans la lite")
                }

                adapteur = FilmAdapteur(applicationContext, activity = this@ListeDeFilms, liste)
                recyclerView.adapter = adapteur


//            var film = database.FilmDao().findByName(
//                "hello",
//                "it's me"
//            )
//
            }
        }


        val recycleThread = (lifecycleScope.launch {
            recyclerView = findViewById(R.id.listeFilms)
            val database: AppDatabase = AppDatabase.getDatabase(applicationContext)
            var liste = database.FilmDao().getAll()
            adapteur = FilmAdapteur(applicationContext, ListeDeFilms(), liste)
            recyclerView.adapter = adapteur


            val filmsListe = withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(applicationContext).FilmDao().getAll()
                return@withContext
            }

        })


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
//                Log.d("MonTag", "Clic sur Trouver un film")
                val intent = Intent(this, RechercheFilm::class.java);
                startActivity(intent)
            }

            R.id.toutSupprimer -> {
                lifecycleScope.launch {
                    deleteAll()

                    adapteur.films = listOf()
                    adapteur.notifyDataSetChanged()
                }
            }

            R.id.titre -> {

            }

            R.id.note -> {

            }

            R.id.annee -> {

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