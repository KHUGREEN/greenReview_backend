package com.khureen.greenReview.model

import java.util.Date

data class ReviewDTO constructor(
    val author : String,

    val content : String,

    val rate : Double,

    val checklist: ChecklistDTO,

    val registeredDate: Date
)


data class AddReviewDTO constructor(
    val product: ProductId,

    val review: ReviewDTO
)

data class GetReviewDTO constructor(
    val id: ReviewId,
    val review: ReviewDTO
)

data class ReviewId constructor(
    val id: Long
)

data class ChecklistDTO constructor(
    val hidingSideEffects : Boolean,

    val notSufficientEvidence : Boolean,

    val ambiguousStatement: Boolean,

    val notRelatedStatement : Boolean,

    val lieStatement : Boolean,

    val justifyingHarmingProduct : Boolean,

    val inappropriateCertification : Boolean
)

data class ReviewStatisticsDTO constructor(
    val reviewer : Int,
    val score: Double,
    val checklistStatisticsDTO: ChecklistStatisticsDTO
)

data class ChecklistStatisticsDTO constructor(
    val hidingSideEffects : Double,

    val notSufficientEvidence : Double,

    val ambiguousStatement: Double,

    val notRelatedStatement : Double,

    val lieStatement : Double,

    val justifyingHarmingProduct : Double,

    val inappropriateCertification : Double
)