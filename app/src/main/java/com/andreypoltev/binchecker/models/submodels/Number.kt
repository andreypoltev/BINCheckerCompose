package com.andreypoltev.binchecker.models.submodels

import kotlinx.serialization.Serializable

@Serializable
data class Number(
    val length: Int = 0,
    val luhn: Boolean = false
)