package com.andreypoltev.binchecker.models.submodels

import kotlinx.serialization.Serializable

@Serializable
data class Bank(
    val name: String = "",
    val url: String = "",
    val phone: String = "",
    val city: String = ""
)