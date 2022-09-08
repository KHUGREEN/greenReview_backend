package com.khureen.greenReview.model

import com.khureen.greenReview.repository.dto.Checklist
import java.util.*
import javax.persistence.Column

data class ProductDTO constructor(
    val name: String,

    val vendor: String,

    val price: Int,

    val deliveryFee: Int,

    val picUrl: String,

    val registeredDate: Date,

    val thumbnailUrl : String,

    val originalUrl : String,

    val detailpicUrl: List<String>
)

data class AddProductDTO constructor(
    val product: ProductDTO
)

data class GetProductDTO constructor(
    val id: ProductId,
    val product: ProductDTO,
    val score: Optional<ProductScore>
)

data class ProductId constructor(
    val id: Long
)

data class ProductScore constructor(
    val reviewer : Long,
    val checklist: ChecklistStatisticsDTO
)

data class GetProductScoreDTO constructor(
    val id: ProductId,
    val score: Optional<ProductScore>
)