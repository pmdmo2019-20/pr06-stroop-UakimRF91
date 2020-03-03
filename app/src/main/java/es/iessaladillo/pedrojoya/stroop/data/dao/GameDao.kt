package es.iessaladillo.pedrojoya.stroop.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import es.iessaladillo.pedrojoya.stroop.data.entity.Game

@Dao
interface GameDao {
    @Insert
    fun insertGame(game: Game)

    @Query("SELECT * FROM Games ORDER BY points DESC LIMIT 3")
    fun queryAllGames(): List<Game>

    @Query("SELECT * FROM Games WHERE gameMode = 'Time' ORDER BY points DESC LIMIT 3")
    fun queryTimeGames(): List<Game>

    @Query("SELECT * FROM Games WHERE gameMode = 'Attempts' ORDER BY points DESC LIMIT 3")
    fun queryAttemptsGames(): List<Game>

    @Query("SELECT * FROM Games WHERE gameId = :gameId")
    fun queryGame(gameId: Long): Game
}