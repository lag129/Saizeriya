package com.example.saizeriya

import kotlinx.serialization.Serializable

@Serializable
data class Menu(
    val id: Int,
    val name: String,
    val value: Int,
    val category: String
)