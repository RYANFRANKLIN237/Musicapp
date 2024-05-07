package com.example.musicapp.models

data class CategoryModel(
    val name: String,
    val coverUrl: String,
) {
    constructor() : this("","")
}
