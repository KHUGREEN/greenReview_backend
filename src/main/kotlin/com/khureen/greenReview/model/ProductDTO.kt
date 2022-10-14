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