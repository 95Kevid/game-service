package uk.gov.ukho.gameservice.services

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.jupiter.MockitoExtension
import uk.gov.ukho.gameservice.models.Game
import uk.gov.ukho.gameservice.models.GameArena
import uk.gov.ukho.gameservice.models.Player
import uk.gov.ukho.gameservice.models.PlayersToPlayersReady
import uk.gov.ukho.gameservice.repositories.GameRepository
import java.util.Optional

@RunWith(MockitoJUnitRunner::class)
@ExtendWith(MockitoExtension::class)
class GameServiceTest(
    @Mock private val gameRepository: GameRepository
) {
    private var player1: Player
    private var player2: Player
    private var player3: Player
    private var game1: Game
    private var game2: Game
    private var game3: Game

    @InjectMocks private val gameService: GameService = GameService(gameRepository)

    init {
        player1 = Player(1, GameArena(10),false,"Burny", isLoser = false, isWinner = false)
        player2 = Player(2, GameArena(10),false,"Helga", isLoser = false, isWinner = false)
        player3 = Player(3, GameArena(10),false,"Fred", isLoser = false, isWinner = false)

        game1 = Game(12, arrayListOf(player1, player2, player3), 3, 10)
        game2 = Game(189, arrayListOf(player1, player2), 2, 10)
        game3 = Game(9, arrayListOf(player1, player2, player3), 3, 10)
    }

    @Test
    fun givenANumberOfPlayersWhenAGameIsInstantiatedAGameIdIsReturned() {
        `when`(gameRepository.save(any(Game::class.java))).thenReturn(game1)
        val gameId1 = gameService.createGame(2, 10)
        verify(gameRepository, times(1))
            .save(any())

        assertThat(gameId1).`as`("1rst run, the gameId should be 12").isEqualTo(12)

        `when`(gameRepository.save(any(Game::class.java))).thenReturn(game2)
        val gameId2 = gameService.createGame(2, 10)
        verify(gameRepository, times(2))
            .save(any(Game::class.java))

        assertThat(gameId2).isEqualTo(189).`as`("2nd run gameId should be 189")
    }

    @Test
    fun givenAPlayerAndAGameIdThenAPlayerIsAddedToAGame() {
        `when`(gameRepository.findById(game1.id!!)).thenReturn(Optional.of(game1))
        `when`(gameRepository.save(game1)).thenReturn(game1)
        gameService.joinPlayerToGame(game1.id!!, player1)

        assertThat(game1.players!!.contains(player1)).isEqualTo(true)
    }

    @Test
    fun givenAGameIdAGameIsReturned() {
        `when`(gameRepository.findById(14)).thenReturn(Optional.of(game1))
        val returnedGame: Game = gameService.getGame(14)

        assertThat(returnedGame)
            .`as`("The correct game that corresponds to " + "the Id of 14 should be returned")
            .isEqualTo(game1)
    }

    @Test
    fun givenAGameIdThenReturnTheNumberOfPlayersToNumberOfNotReadyPlayers() {
        player1.isReadyToStartGame = false
        player2.isReadyToStartGame = true
        player3.isReadyToStartGame = false

        `when`(gameRepository.findById(9)).thenReturn(Optional.of(game3))
        val correctPlayersToPlayersNotReady = PlayersToPlayersReady(3, 1)
        val incorrectPlayersToPlayersNotReady = PlayersToPlayersReady(3, 3)

        assertThat(gameService.getPlayersToPlayersReady(game3.id!!))
            .isEqualTo(correctPlayersToPlayersNotReady)
            .`as`("1 of the 3 players should be ready")
        assertThat(gameService.getPlayersToPlayersReady(game3.id!!))
            .isNotEqualTo(incorrectPlayersToPlayersNotReady)
            .`as`("3 of the 3 players should not be ready")
    }

    @Test
    fun firstPlayerThatJoinsGameIsTheFirstPlayerWhosTurnItIs() {
        `when`(gameRepository.findById(1)).thenReturn(Optional.of(game1))
        assertThat(gameService.checkForTurn(1)).isEqualTo(player1)
    }

    @Test
    fun secondPlayerThatJoinsGameIsTheSecondPlayerWhosTurnItIs() {
        `when`(gameRepository.findById(1)).thenReturn(Optional.of(game1))
        gameService.nextTurn(game1)
        assertThat(gameService.checkForTurn(1)).isEqualTo(player2)
    }

    @Test
    fun thirdPlayerThatJoinsGameIsTheSecondPlayerWhosTurnItIs() {
        `when`(gameRepository.findById(1)).thenReturn(Optional.of(game1))
        repeat(2) {
            gameService.nextTurn(game1)
        }

        assertThat(gameService.checkForTurn(1)).isEqualTo(player3)
    }

    @Test
    fun givenThatAllPlayersHasHadTheirTurnThenThePlayerThatSetTheGameUpHasTheirTurnAgain() {
        `when`(gameRepository.findById(1)).thenReturn(Optional.of(game1))
        repeat(3) {
            gameService.nextTurn(game1)
        }

        assertThat(gameService.checkForTurn(1)).isEqualTo(player1)
    }
}
