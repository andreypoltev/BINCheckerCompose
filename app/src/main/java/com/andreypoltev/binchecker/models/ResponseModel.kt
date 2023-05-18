package com.andreypoltev.binchecker.models

import com.andreypoltev.binchecker.models.submodels.Bank
import com.andreypoltev.binchecker.models.submodels.Country
import com.andreypoltev.binchecker.models.submodels.Number
import kotlinx.serialization.Serializable

@Serializable
data class ResponseModel(
    val number: Number? = Number(),
    val scheme: String? = "",
    val type: String? = "",
    val brand: String? = "",
    val prepaid: String? = "",
    val country: Country? = Country(),
    val bank: Bank? = Bank()
)
