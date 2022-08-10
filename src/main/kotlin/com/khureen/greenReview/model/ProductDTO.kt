package com.khureen.greenReview.model

import java.util.*

data class ProductDTO constructor(
    val name: String,

    val vendor: String,

    val price: Int,

    val deliveryFee: Int,

    val picUrl: List<String>,

    val registeredDate: Date,

    val thumbnailUrl : String
)

data class AddProductDTO constructor(
    val product: ProductDTO
)

data class GetProductDTO constructor(
    val id: ProductId,
    val product: ProductDTO
)

data class ProductId constructor(
    val id: Long
)