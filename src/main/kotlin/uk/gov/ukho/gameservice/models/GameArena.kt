package uk.gov.ukho.gameservice.models

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import uk.gov.ukho.gameservice.models.Ship
import java.util.HashSet
import javax.persistence.ManyToMany
import uk.gov.ukho.gameservice.models.BoardPosition
import java.util.ArrayList
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
data class GameArena(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @OneToMany(cascade = [CascadeType.ALL])
    var shipsOnBoard: MutableSet<Ship> = HashSet(),
    var isAllShipsPlaced: Boolean = false,
    var isAllShipsSunk: Boolean = false,

    @ManyToMany(cascade = [CascadeType.ALL])
    val shotBoardPositions: MutableSet<BoardPosition> = HashSet(),

    @OneToMany(cascade = [CascadeType.ALL])
    var sunkShips: MutableSet<Ship> = HashSet(),
    var gameArenaSize: Int = 0
)