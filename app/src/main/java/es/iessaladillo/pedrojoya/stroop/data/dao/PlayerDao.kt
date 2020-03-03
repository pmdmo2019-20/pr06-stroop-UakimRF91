package es.iessaladillo.pedrojoya.stroop.data.dao

import androidx.lifecycle.LiveData
import es.iessaladillo.pedrojoya.stroop.data.entity.Player
import androidx.room.*

@Dao
interface PlayerDao {
    @Query("SELECT * FROM Players")
    fun queryAllPlayers(): LiveData<List<Player>>

    @Query("SELECT * FROM Players WHERE playerId = :playerId")
    fun queryPlayer(playerId: Long): Player

    @Query("SELECT COUNT(*) FROM Players WHERE playerId = :playerId")
    fun existsPlayerWithThisId(playerId: Long): Int

    @Insert
    fun insertPlayer(player: Player)

    @Update
    fun updatePlayer(player: Player)

    @Delete
    fun deletePlayer(player: Player)
}