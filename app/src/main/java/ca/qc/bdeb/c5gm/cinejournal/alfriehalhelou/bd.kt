package ca.qc.bdeb.c5gm.cinejournal.alfriehalhelou

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update

@Entity
data class Film(
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    var titre: String = "",
    var slogan: String = "",
    var annee: Int = 0,
    var note: Float = 0.0f,
    var image: String = "",
    var latitude: Double?,
    var longitude: Double?
)

@Dao
interface FilmDao {
    @Query("SELECT * FROM Film")
    suspend fun getAll(): List<ItemFilm>

    @Query("SELECT image FROM Film WHERE uid = :filmId")
    suspend fun getImage(filmId: Int): String?

    @Query("SELECT latitude, longitude FROM Film")
    suspend fun getPosition(): List<PositionData>

//    @Query("UPDATE Film SET image = :newImageUri WHERE uid = :filmId")
//    suspend fun setImage(filmId: Int, newImageUri: String): Int

    @Query("SELECT * FROM Film WHERE uid IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<ItemFilm>

    @Query("SELECT * FROM Film WHERE uid = :filmId")
    suspend fun loadById(filmId: Int): Film

    @Query("SELECT * FROM Film WHERE titre LIKE :titre AND " + "slogan LIKE :slogan LIMIT 1")
    suspend fun findByName(titre: String, slogan: String?): Film

    @Query("SELECT * FROM Film ORDER BY titre ASC")
    suspend fun trierParTitre(): List<ItemFilm>

    @Query("SELECT * FROM Film ORDER BY note DESC")
    suspend fun trierParNote(): List<ItemFilm>

    @Query("SELECT * FROM Film ORDER BY annee ASC")
    suspend fun trierParAnnee(): List<ItemFilm>

    @Insert
    suspend fun insertAll(vararg Films: Film)

    @Update
    suspend fun updateAll(vararg Films: Film)

    @Delete
    suspend fun delete(Film: Film)

    @Query("DELETE  FROM Film")
    suspend fun deleteAllData()

}

@Database(entities = [Film::class], version = 21)

abstract class AppDatabase : RoomDatabase() {
    abstract fun FilmDao(): FilmDao

    companion object {
        // Singleton empêche plusieurs instances de la base
        // d'être ouvertes en même temps
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            // si INSTANCE n'est pas null, on le retourne,
            // sinon, on crée la base de données
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "sqlite_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
