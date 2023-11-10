package com.example.cinejournal.alfriehalhelou

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * Ensemble de data class pour représenter les données fournies par l'API
 */
data class Films(
    /**
     * Dans le cas où on veut donner un nom plus clair à notre classe
     * que le nom qui est disponible dans l'API, on peut utiliser
     *
     * @SerializedName("nomDansLAPI")
     */
    val adult: Boolean,
    val backdrop_path : String,
    val budget : Int,
    val id : String,
    val name : String


)


/**
 * Interface qui spécifie des méthodes qui seront générées automatiquement par la librairie Retrofit
 *
 * Très similaire au DAO (Data Access Object) dans Room où vous définissiez vos requêtes
 *
 * Les méthodes peuvent composer
 */
interface ApiService {
    @GET("movie/{movie_id}")
    suspend fun getMovieById (@Path("movie_id") movieId: Int): Response<Films>
    /**
     * Méthode avec un paramètre à injecter dans l'URL, par exemple :
     *
     * num = 17
     * => https://jsonplaceholder.typicode.com/posts/17
     */
//    @GET("posts/{num}")
//    suspend fun getPostById(@Path("num") num: Int): Response<Post>

    /**
     * Méthode avec un paramètre à injecter dans la Query String, par exemple :
     *
     * postId = 17
     * => https://jsonplaceholder.typicode.com/comments?postId=17
     */
//    @GET("comments")
//    suspend fun getCommentsByPost(@Query("postId") postId: Int): Response<List<Comment>>

    /**
     * On peut également définir des méthodes @POST() pour envoyer des données à l'API si celui-ci
     * le supporte
     *
     * D'autres méthodes HTTP sont également possibles, à vous de fouiller dans la doc
     */
}

/**
 * Objet d'accès à l'API : essentiellement équivalent au singleton qui donnait accès à la BD dans le cas de Room
 */
object ApiClient {
    /**
     * URL de base pour toutes les requêtes faites à l'API
     */
    private const val BASE_URL: String = "https://api.themoviedb.org/3/"

    /** __by lazy__ est un construit de Kotlin qui permet d'initialiser une variable
     * au moment de l'utiliser pour la première fois
     *
     * Ça fait essentiellement ce qu'un singleton ferait avec :
     *     if(INSTANCE == null)
     *         INSTANCE = new Bidule()
     *
     * Mais en plus gracieux, en utilisant une fonctionnalité du langage plutôt qu'on codant
     * cette logique à la main à chaque fois
     */

    private val gson: Gson by lazy {
        GsonBuilder().setLenient().create()
    }

    private val httpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addNetworkInterceptor(logging)
            .addInterceptor { chain ->
                val original = chain.request()
                // Request customization: add request headers
                val requestBuilder = original.newBuilder()
                    .header(
                        "Authorization",
                        "Bearer ${BuildConfig.API_KEY_TMBD}"
                    )
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()
    }

    private val retrofit: Retrofit by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
