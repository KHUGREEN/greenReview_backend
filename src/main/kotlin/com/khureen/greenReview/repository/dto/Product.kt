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

import java.util.Date
import javax.persistence.*

@Entity
@Table(indexes = [Index(name = "i_product_date", columnList = "registeredDate")])
class Product(
    @Id
    @Column(name = "product_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO) //기본 키에 자동으로 매핑
    var id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var vendor : String,

    @Column(nullable = false)
    var price : Int,

    @Column(nullable = false)
    var deliveryFee : Int,

    @Column(nullable = false, length = 2048)
    var picUrl : String,

    @Column(nullable = false)
    var thumbnailUrl : String,

    @OneToMany(mappedBy = "product")
    var reviews: MutableList<Review>,

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    var registeredDate : Date,

    @Column(nullable = false, length = 2048)
    var originalUrl : String,

    @Column(nullable = false, length = 40960)
    var detailpicUrl : String
)