package ca.qc.bdeb.c5gm.cinejournal.alfriehalhelou

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FilmViewModel : ViewModel() {

    var dejaCharge = false


    private val _selectedImageUri = MutableLiveData<Uri?>()
    val selectedImageUri: MutableLiveData<Uri?> get() = _selectedImageUri


    private val _filmTitle = MutableLiveData<String?>()
    val filmTitle: MutableLiveData<String?> get() = _filmTitle
    private val _filmSlogan = MutableLiveData<String?>()
    val filmSlogan: MutableLiveData<String?> get() = _filmSlogan
    private val _filmYear = MutableLiveData<Int>()
    val filmYear: MutableLiveData<Int> get() = _filmYear
    private val _filmRating = MutableLiveData<Float>()
    val filmRating: MutableLiveData<Float> get() = _filmRating

    private val _lattitude = MutableLiveData<Double?>()
    val lattitude: MutableLiveData<Double?> get() = _lattitude

    private val _longitude = MutableLiveData<Double?>()
    val longitude: MutableLiveData<Double?> get() = _longitude

    fun initializeWithFilmData(film: Film) {
        _selectedImageUri.value = film.image.toUri()
        _filmTitle.value = film.titre
        _filmSlogan.value = film.slogan
        _filmYear.value = film.annee ?: 0
        _filmRating.value = film.note ?: 0.0f
        _lattitude.value = film.latitude ?: 0.0
        _longitude.value = film.longitude ?: 0.0


        dejaCharge = true
    }

    fun estDejaCharge() = dejaCharge

    fun updateSelectedImageUri(uri: Uri?) {
        _selectedImageUri.value = uri
    }

    fun updateFilmTitle(title: String) {
        _filmTitle.value = title
    }

    fun updateFilmSlogan(slogan: String) {
        _filmSlogan.value = slogan
    }

    fun updateFilmYear(year: Int) {
        _filmYear.value = year
    }

    fun updateFilmRating(rating: Float) {
        _filmRating.value = rating
    }

}