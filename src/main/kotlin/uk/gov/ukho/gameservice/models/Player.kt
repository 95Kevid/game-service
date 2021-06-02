package uk.gov.ukho.gameservice.models

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
class Player (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Number?,
    @JsonIgnore
    @OneToOne(cascade = [CascadeType.ALL])
    val gameArena: GameArena?,
    @JsonIgnore
    var isReadyToStartGame: Boolean?,
    val name: String,
    var isLoser: Boolean,
    var isWinner: Boolean
)