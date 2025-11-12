package com.example.subscriptionapplication

data class Service(
    val id: Int,
    val name: String,
    val image: String,
    var selected: Boolean = false,
    var price: Int = 0
)
