package com.khureen.greenReview.service.dto

import com.khureen.greenReview.model.GetProductDTO
import com.khureen.greenReview.model.GetProductScoreDTO

data class ProductResponse constructor(val product : GetProductDTO)

data class ProductChecklistResponse constructor(val product: GetProductScoreDTO)