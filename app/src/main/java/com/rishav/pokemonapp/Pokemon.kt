package com.rishav.pokemonapp

import android.accounts.AuthenticatorDescription

class Pokemon {
    var name: String? = null
    var description: String? = null
    var image: Int? = null
    var power: Double? = null
    var latitude: Double? = null
    var longitude: Double? = null
    var isCatch: Boolean? = false

    constructor(
        image: Int,
        name: String,
        description: String,
        power: Double,
        lat: Double,
        long: Double,
        isCatch: Boolean
    ) {
        this.name = name
        this.image = image
        this.description = description
        this.power = power
        this.latitude = lat
        this.longitude = long
        this.isCatch = isCatch
    }
}