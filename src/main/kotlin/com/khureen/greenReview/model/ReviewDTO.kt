/*
 * Copyright 2022 KHUGREEN (https://github.com/KHUGREEN)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.khureen.greenReview.model

import java.util.Date

data class ReviewDTO constructor(
    val author : String,

    val content : String,

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

data class ChecklistStatisticsDTO constructor(
    val hidingSideEffects : Int,

    val notSufficientEvidence : Int,

    val ambiguousStatement: Int,

    val notRelatedStatement : Int,

    val lieStatement : Int,

    val justifyingHarmingProduct : Int,

    val inappropriateCertification : Int
)