package uk.gov.ukho.gameservice.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.ukho.gameservice.repositories.GameRepository
import uk.gov.ukho.gameservice.models.Game
import uk.gov.ukho.gameservice.models.GameStatus
import uk.gov.ukho.gameservice.models.Player
import uk.gov.ukho.gameservice.models.PlayersToPlayersReady
import java.util.LinkedList
import java.util.Optional
import java.util.Queue
import java.util.concurrent.LinkedBlockingQueue

@Service
class GameService @Autowired constructor(gameRepository: GameRepository) {
    private val gameRepository: GameRepository = gameRepository

    fun createGame(numberOfPlayers: Int, gameArenaSize: Int): Int {
        val game = Game(maxPlayers = numberOfPlayers,  gameArenaSize = gameArenaSize, status = GameStatus.CREATE_GAME)
        val savedGame: Game = saveGame(game)

        return savedGame.id ?: throw NullPointerException("Database did not set game ID!!")
    }

    fun joinPlayerToGame(gameId: Int, player: Player) {
        val game: Game = getGame(gameId)
        val players: LinkedList<Player> = getPlayersFromGame(game)
        checkIfMaximumPlayerLimitReached(game, players)
        players.add(player)
        game.players = players
        saveGame(game)
    }

    private fun getPlayersFromGame(game: Game): LinkedList<Player> {
        return LinkedList(game.players)
    }

    private fun checkIfGameIdExistsInRepository(gameOptional: Optional<Game>) {
        require(gameOptional.isPresent) { """The game ID provided does not correspond 
            |to a game record in the repository""".trimMargin() }
    }

    private fun checkIfMaximumPlayerLimitReached(game: Game, players: LinkedList<Player>) {
        check(players.size <= game.maxPlayers) {
            "The maximum number of players (${game.maxPlayers.toString()}) has been reached."
        }
    }

    fun getGame(gameId: Int): Game {
        val optionalGame: Optional<Game> = gameRepository.findById(gameId)
        checkIfGameIdExistsInRepository(optionalGame)
        return optionalGame.get()
    }

    fun nextTurn(game: Game) {
        val playersInGame: Queue<Player> = getPlayersFromGame(game)
        val currentPlayersTurn: Player = playersInGame.remove()
        playersInGame.add(currentPlayersTurn)
        game.players = LinkedList(playersInGame)
    }

    fun getPlayersToPlayersReady(gameId: Int): PlayersToPlayersReady {
        val game: Game = getGame(gameId)
        val playersInGame: List<Player> = getPlayersFromGame(game)
        return PlayersToPlayersReady(game.maxPlayers, getReadyPlayers(playersInGame))
    }

    private fun getReadyPlayers(players: List<Player>): Int {
        return players.stream().filter { p: Player -> p.isReadyToStartGame!! }.count()
            .toInt()
    }

    fun saveGame(game: Game): Game {
        return gameRepository.save(game)
    }

    fun checkForTurn(gameId: Int): Player {
        val game: Game = getGame(gameId)
        val players: Queue<Player> = LinkedBlockingQueue(game.players)
        return players.peek()
    }
}
