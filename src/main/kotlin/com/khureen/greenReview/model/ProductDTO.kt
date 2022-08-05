package com.khureen.greenReview.model

import com.khureen.greenReview.repository.dto.Review
import java.util.*
import javax.persistence.*

data class ProductDTO constructor(
    val id: Long,

    val name: String,

    val vendor: String,

    val price: Int,

    val deliveryFee: Int,

    val originalUrl: List<String>,

    val registeredDate: Date
)