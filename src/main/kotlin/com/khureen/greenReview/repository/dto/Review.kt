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

package com.khureen.greenReview.repository.dto

import java.util.*
import javax.persistence.*

@Entity
@Table(indexes = [Index(name = "i_review_date", columnList = "registeredDate"), Index(name  = "i_product_id", columnList = "product_id")])
class Review(
    @Id
    @Column(name = "review_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

    @Column(nullable = false)
    var author : String,


    @ManyToOne
    @JoinColumn(name = "product_id")
    var product: Product,

    @Column(nullable = false, length = 2048)
    var content: String,

    @Column(nullable = false)
    var rate: Double,

    @Embedded
    var checklist: Checklist,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    var registeredDate: Date
)