package com.andreypoltev.binchecker.models.submodels

import kotlinx.serialization.Serializable

@Serializable
data class Number(
    val length: String? = "",
    val luhn: String? = ""
)