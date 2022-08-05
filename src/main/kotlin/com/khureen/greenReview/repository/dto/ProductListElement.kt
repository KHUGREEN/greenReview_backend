package com.khureen.greenReview.repository.dto

data class ProductListElement constructor(
    val id : Long, val thumbnailUrl : String, val name : String, val vendor : String, val price : Int
)