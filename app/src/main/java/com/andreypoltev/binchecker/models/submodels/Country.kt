package com.andreypoltev.binchecker.models.submodels

import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val numeric: String = "",
    val alpha2: String = "",
    val name: String = "",
    val emoji: String = "",
    val currency: String = "",
    val latitude: Int = 0,
    val longitude: Int = 0
)