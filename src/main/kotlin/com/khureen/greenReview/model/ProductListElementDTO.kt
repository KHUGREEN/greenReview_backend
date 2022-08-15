package com.khureen.greenReview.model

import java.util.*

data class ProductListElementDTO constructor(
    val id: Long,
    val thumbnailUrl: String,
    val name: String,
    val vendor: String,
    val price: Int,
    val score : Optional<ProductScore>
)