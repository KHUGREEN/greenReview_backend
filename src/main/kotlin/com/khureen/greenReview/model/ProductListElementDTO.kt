package com.khureen.greenReview.model

data class ProductListElementDTO constructor(
    val id: Long,
    val thumbnailUrl: String,
    val name: String,
    val vendor: String,
    val price: Int
)