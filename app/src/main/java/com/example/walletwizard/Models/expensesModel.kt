package com.example.walletwizard.Models

import java.io.Serializable

data class expensesModel(
    var expensesId: String? = null,
    var expensesAmount: String? = null,
    var expensesNote: String? = null,
    var expensesType: String? = null
): Serializable
