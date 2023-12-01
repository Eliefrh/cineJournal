package ca.qc.bdeb.c5gm.cinejournal.alfriehalhelou

/**Application = ciné journal
 **fait par : Elie Alfrieh et Khalil Alhelou
 **Dans le cadre du cours : Application mobile
 **Description : une application android qui permet de noter, gérer et archiver des films que vous
 * deja regardé
 **/
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
import android.widget.TextView
import androidx.activity.viewModels
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


class
ListeDeFilms : AppCompatActivity() {
    lateinit var films: Film
    lateinit var prefsTri: preferencesTri
    lateinit var recyclerView: RecyclerView
    lateinit var adapteur: FilmAdapteur


    private fun creerUriPhoto(): Uri {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val photoFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$timeStamp.jpg")
        val imageUri = photoFile.toUri()
        return imageUri
    }

    //s'assurer que le tri est toujours (en revenant sur la page)afficher et que les films sont
    //trier de la meme facon declarer dans les preferences
    override fun onResume() {
        super.onResume()
        prefsTri = preferencesTri(this)
        val triEnCours = prefsTri.getTriPreference()
        Log.d("Trie", triEnCours)
        var tri: TextView = findViewById(R.id.tri)
        tri.text = triEnCours
        trierEtMettreAJour(triEnCours)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activite_liste_film)

        val data: FilmViewModel by viewModels()
        data.dejaCharge = false

        prefsTri = preferencesTri(this)
        val triEnCours = prefsTri.getTriPreference()
        Log.d("Trie", triEnCours)
        var tri: TextView = findViewById(R.id.tri)
        tri.text = triEnCours
        trierEtMettreAJour(triEnCours)

        recyclerView = findViewById(R.id.listeFilms)
        adapteur = FilmAdapteur(applicationContext, ListeDeFilms(), listOf())
        recyclerView.adapter = adapteur


        var toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        var titre: TextView = findViewById(R.id.mes_films)
        titre.text = "Mes films"


        var pageVide: TextView = findViewById(R.id.pageVide)

        //ouvrir page ajouterFilm
        var ajouter: Button = findViewById(R.id.ajouter)
        ajouter.setOnClickListener() {
            val intent = Intent(this, AjouterEditerFilm::class.java)
            startActivity(intent)
        }

        //charger les films a partir de notre BD et controler l'affichage du text liste qui contient
        //pas des films
        val recycleThread = (lifecycleScope.launch {
            recyclerView = findViewById(R.id.listeFilms)
            var liste = chargerFilms()

            if (liste.isEmpty()) {
                pageVide.visibility = View.VISIBLE
            } else {
                pageVide.visibility = View.INVISIBLE
            }
            Log.d("Elie liste", liste.toString())
            adapteur = FilmAdapteur(applicationContext, ListeDeFilms(), liste)
            recyclerView.adapter = adapteur
        })

    }

    //inflater pour afficher le menu de l'application
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.map -> {
                Log.d("MonTag", "Clic sur Carte de films")
                val intent = Intent(this, MapActivity::class.java);
                intent.putExtra("disableButton", true)
                startActivity(intent)
            }

            //ouvrir page a propos
            R.id.aPropos -> {
                Log.d("MonTag", "Clic sur À Propos")
                val intent = Intent(this, APropos::class.java);
                startActivity(intent)
            }
            //affichage de la page de recherche
            R.id.trouverUnFilm -> {
                Log.d("MonTag", "Clic sur Trouver un film")
                val intent = Intent(this, RechercheFilm::class.java);
                startActivity(intent)
            }

            //afficher un alert pour demander d'effacer le contenu
            //supprimer tous ce qu'il est dans la base de donnees
            R.id.toutSupprimer -> {
                var pageVide: TextView = findViewById(R.id.pageVide)

                val alert = AlertDialog.Builder(this).create()
                alert.setTitle("Supprimer Tout")
                alert.setMessage("Êtes-vous sûre???")
                alert.apply {
                    setButton(AlertDialog.BUTTON_POSITIVE,
                        "oui",
                        DialogInterface.OnClickListener { dialog, id ->
                            lifecycleScope.launch {
                                deleteAll()
                            }
                            pageVide.visibility = View.VISIBLE
                            adapteur.films = listOf()
                            adapteur.notifyDataSetChanged()
                        })
                    setButton(AlertDialog.BUTTON_NEGATIVE,
                        "non",
                        DialogInterface.OnClickListener { dialog, id -> })
                }.show()

            }

            //trier l'affichage en ordre alphabétique
            //ajout dans les preferences
            R.id.titre -> {
                val tri = "Trié par titre"
                prefsTri.setTriPreference(tri)
                trierEtMettreAJour(tri)
                lifecycleScope.launch {
                    val filmsTries = withContext(Dispatchers.IO) {
                        trierParTitre()
                    }
                    adapteur.mettreAJour(filmsTries) // Mettre à jour la liste de films
                }
            }
            //trier l'affichage par note
            //ajout dans les preferences
            R.id.note -> {
                val tri = "Trié par note"
                prefsTri.setTriPreference(tri)
                trierEtMettreAJour(tri)
                lifecycleScope.launch {
                    val filmsTries = withContext(Dispatchers.IO) {
                        trierParNote()
                    }
                    adapteur.mettreAJour(filmsTries) // Mettre à jour la liste de films
                }
            }
            //trier l'affichage par annee
            //ajout dans les preferences
            R.id.annee -> {
                val tri = "Trié par annee"
                prefsTri.setTriPreference(tri)
                trierEtMettreAJour(tri)
                lifecycleScope.launch {
                    val filmsTries = withContext(Dispatchers.IO) {
                        trierParAnnee()
                    }
                    adapteur.mettreAJour(filmsTries) // Mettre à jour la liste de films
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private suspend fun deleteAll() = withContext(Dispatchers.IO) {
        val database: AppDatabase = AppDatabase.getDatabase(applicationContext)
        database.FilmDao().deleteAllData()
    }

    private suspend fun trierParTitre() = withContext(Dispatchers.IO) {
        val database: AppDatabase = AppDatabase.getDatabase(applicationContext)
        database.FilmDao().trierParTitre()
    }

    private suspend fun trierParNote() = withContext(Dispatchers.IO) {
        val database: AppDatabase = AppDatabase.getDatabase(applicationContext)
        database.FilmDao().trierParNote()
    }

    private suspend fun trierParAnnee() = withContext(Dispatchers.IO) {
        val database: AppDatabase = AppDatabase.getDatabase(applicationContext)
        database.FilmDao().trierParAnnee()
    }

    private suspend fun chargerFilms() = withContext(Dispatchers.IO) {
        val database: AppDatabase = AppDatabase.getDatabase(applicationContext)
        database.FilmDao().getAll()
    }

    //une fonction pour avoir les preferance et manipuler le tri
    private fun trierEtMettreAJour(tri: String) {
        when (tri) {
            "Trié par titre" -> {
                lifecycleScope.launch {
                    val filmsTries = withContext(Dispatchers.IO) {
                        trierParTitre()
                    }
                    adapteur.mettreAJour(filmsTries)
                    var tri: TextView = findViewById(R.id.tri)
                    tri.text = prefsTri.getTriPreference()
                }
            }

            "Trié par note" -> {

                lifecycleScope.launch {
                    val filmsTries = withContext(Dispatchers.IO) {
                        trierParNote()
                    }
                    adapteur.mettreAJour(filmsTries)
                    var tri: TextView = findViewById(R.id.tri)
                    tri.text = prefsTri.getTriPreference()
                }
            }

            "Trié par annee" -> {
                lifecycleScope.launch {
                    val filmsTries = withContext(Dispatchers.IO) {
                        trierParAnnee()
                    }
                    adapteur.mettreAJour(filmsTries)
                    var tri: TextView = findViewById(R.id.tri)
                    tri.text = prefsTri.getTriPreference()

                }

            }


        }
    }

}
