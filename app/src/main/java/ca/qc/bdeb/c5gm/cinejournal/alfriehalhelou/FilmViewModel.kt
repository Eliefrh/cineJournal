package ca.qc.bdeb.c5gm.cinejournal.alfriehalhelou

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FilmViewModel : ViewModel() {
    private val _selectedImageUri = MutableLiveData<Uri?>()
    val selectedImageUri: LiveData<Uri?> get() = _selectedImageUri
    private val _filmTitle = MutableLiveData<String?>()
    val filmTitle: MutableLiveData<String?> get() = _filmTitle
    private val _filmSlogan = MutableLiveData<String?>()
    val filmSlogan: MutableLiveData<String?> get() = _filmSlogan
    private val _filmYear = MutableLiveData<Int>()
    val filmYear: LiveData<Int> get() = _filmYear
    private val _filmRating = MutableLiveData<Float>()
    val filmRating: LiveData<Float> get() = _filmRating

    fun initializeWithFilmData(film: Film) {
        _selectedImageUri.value = film.image.toUri()
        _filmTitle.value = film.titre
        _filmSlogan.value = film.slogan
        _filmYear.value = film.annee ?: 0
        _filmRating.value = film.note ?: 0.0f
    }

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