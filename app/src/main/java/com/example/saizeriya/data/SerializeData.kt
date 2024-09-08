package com.example.saizeriya.data

import kotlinx.serialization.Serializable

@Serializable
data class Menu(
    val id: Int,
    val name: String,
    val value: Int,
    val category: String
)