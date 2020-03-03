package es.iessaladillo.pedrojoya.stroop.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Players",
    indices = [
        Index(
            name = "PLAYERS_INDEX_NAME_UNIQUE",
            value = ["playerName"],
            unique = true
        )
    ]
)

data class Player(
    @PrimaryKey(autoGenerate = true)
    val playerId: Long = 0,
    var playerName: String,
    val imageId: Int
)