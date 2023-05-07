package com.example.walletwizard.Models

import java.io.Serializable

data class incomeModel(
    var incomeId: String? = null,
    var incomeAmount: String? = null,
    var incomeNote: String? = null,
    var incomeType: String? = null
): Serializable
