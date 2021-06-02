package uk.gov.ukho.gameservice.repositories

import org.springframework.data.repository.CrudRepository
import uk.gov.ukho.gameservice.models.Game


interface GameRepository : CrudRepository<Game, Int>