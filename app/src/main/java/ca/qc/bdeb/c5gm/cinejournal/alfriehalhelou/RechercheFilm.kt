package ca.qc.bdeb.c5gm.cinejournal.alfriehalhelou

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.android.material.snackbar.Snackbar

class RechercheFilm : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recherche_film)

        val toolbar: Toolbar = findViewById(R.id.toolbar_recherche)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Ciné Journal"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
}



//    private fun afficherSnackbar(message: String) {
//        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).setAction("Réessayer")
//        {
//            // Réessayer la recherche
//
//        }.show()
//    }

