package uk.gov.ukho.gameservice.models

import java.util.Objects

class PlayersToPlayersReady(private val playersInGame: Int, private val playersReady: Int) {
    override fun equals(playersToPlayersReady: Any?): Boolean {
        if (this === playersToPlayersReady) {
            return true
        }
        if (playersToPlayersReady == null || javaClass != playersToPlayersReady.javaClass) {
            return false
        }
        val that = playersToPlayersReady as PlayersToPlayersReady
        return playersInGame == that.playersInGame && playersReady == that.playersReady
    }

    override fun hashCode(): Int {
        return Objects.hash(playersInGame, playersReady)
    }
}