package uk.gov.ukho.gameservice.models;

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class BoardPosition @JvmOverloads constructor(

        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        var id: Int? = null,
        val col: Char,
        val row: Int,
        var isHit: Boolean = false,
        var colour: String = "blue"
) {
    fun positionEqual(position: BoardPosition): Boolean {
        return position.col.equals(this.col) && position.row == this.row
    }

    override fun toString(): String {
        return String.format("%s%d", col, row)
    }
}


