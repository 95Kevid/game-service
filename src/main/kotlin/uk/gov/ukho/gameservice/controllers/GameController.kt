package uk.gov.ukho.gameservice.controllers

import com.harragan.battleshipsboot.facades.LoadGameFacade
import com.harragan.battleshipsboot.facades.PlayerAddingFacade
import com.harragan.battleshipsboot.model.kotlinmodel.game.JoinGameRequest
import com.harragan.battleshipsboot.model.kotlinmodel.game.JoinGameResponse
import com.harragan.battleshipsboot.model.kotlinmodel.game.LoadGameRequest
import com.harragan.battleshipsboot.model.kotlinmodel.game.LoadGameResponse
import com.harragan.battleshipsboot.service.GameService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import uk.gov.ukho.gameservice.services.GameService

@RestController
class GameController(
    private val gameService: GameService,
    private val playerAddingFacade: PlayerAddingFacade,
    private val loadGameFacade: LoadGameFacade) {

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = ["/creategame/{numberOfPlayers}/{arenaSize}"], method = [RequestMethod.POST])
    fun createGame(@PathVariable numberOfPlayers: Int, @PathVariable arenaSize: Int): Int =
            gameService.createGame(numberOfPlayers, arenaSize)

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = ["/joingame"], method = [RequestMethod.POST])
    fun joinGame(@RequestBody joinGameRequest: JoinGameRequest): JoinGameResponse =
            playerAddingFacade.createPlayerAndJoinToGame(joinGameRequest.playerName, joinGameRequest.gameId)

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = ["/loadgame"], method = [RequestMethod.GET])
    fun loadGame(@RequestBody loadGameRequest: LoadGameRequest): LoadGameResponse =
            loadGameFacade.loadGame(loadGameRequest)
}