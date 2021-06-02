package uk.gov.ukho.gameservice.models

import java.util.LinkedList
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.OrderColumn
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
data class Game(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    @OrderColumn
    @OneToMany
    var players: List<Player>? = null,
    val maxPlayers: Int,
    val gameArenaSize: Int,
    var status: GameStatus? = null
)
