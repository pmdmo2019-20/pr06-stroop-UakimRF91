package es.iessaladillo.pedrojoya.stroop.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(

    tableName = "Games",
    indices = [
        Index(
            name = "GAMES_INDEX_ID_UNIQUE",
            value = ["gameId"],
            unique = true
        )
    ]
)
data class Game(
    @PrimaryKey(autoGenerate = true)
    val gameId: Long = 0,
    @Embedded
    val player: Player,
    val gameMode: String,
    val words: Int,
    val time: Int,
    val points: Int,
    val numCorrects: Int
)