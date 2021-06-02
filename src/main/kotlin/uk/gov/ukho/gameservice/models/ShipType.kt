package com.harragan.battleshipsboot.model.kotlinmodel.ships

enum class ShipType(val length: Int) {
    CRUISER(3),
    CARRIER(5),
    SUBMARINE(3),
    DESTROYER(2),
    BATTLESHIP(4)
}

