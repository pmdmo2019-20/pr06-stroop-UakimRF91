package es.iessaladillo.pedrojoya.stroop.data

import android.content.Context
import es.iessaladillo.pedrojoya.stroop.data.dao.PlayerDao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import es.iessaladillo.pedrojoya.stroop.data.dao.GameDao
import es.iessaladillo.pedrojoya.stroop.data.entity.Game
import es.iessaladillo.pedrojoya.stroop.data.entity.Player

@Database(
    entities = [Player::class, Game::class],
    version = 1,
    exportSchema = true
)
abstract class StroopDatabase : RoomDatabase() {
    abstract val playerDao: PlayerDao
    abstract val gameDao: GameDao

    companion object {

        @Volatile
        private var INSTANCE: StroopDatabase? = null

        fun getInstance(context: Context): StroopDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            StroopDatabase::class.java,
                            "stroop_database"
                        ).allowMainThreadQueries().build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}