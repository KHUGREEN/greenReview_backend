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

package com.khureen.greenReview

import com.khureen.greenReview.repository.dto.Checklist
import com.khureen.greenReview.repository.dto.Product
import com.khureen.greenReview.repository.dto.Review
import java.util.*

class TestUtil {
    companion object {
        fun getProduct(
            name : String = "name",
            vendor : String= "vendor",
            price : Int = 0,
            deliveryFee : Int = 0,
            picUrl :String = "list",
            thumbnailUrl : String = "thumbnail",
            registeredDate : Date = Date(),
            originalUrl : String = "originalUrl",
            detailUrl : String = "detaulUrl"
        ): Product {
            val product = Product(
                name = name,
                vendor = vendor,
                price = price,
                deliveryFee = deliveryFee,
                picUrl = picUrl,
                thumbnailUrl = thumbnailUrl,
                reviews = mutableListOf(),
                registeredDate = registeredDate,
                originalUrl = originalUrl,
                detailpicUrl = detailUrl
            )

            return product
        }

        fun getChecklist() : Checklist {
            return Checklist(
                0,
                0,
                0,
                0,
                0,
                0,
                0
            )
        }

        fun getReview(
            product: Product,
            author : String = "account",
            content : String = "content",
            rate : Double = 0.0,
            checklist : Checklist = getChecklist(),
            registeredDate : Date = Date()
        ): Review {
            val review = Review(
                author = author,
                product = product,
                content = content,
                rate = rate,
                checklist = checklist,
                registeredDate = registeredDate
            )

            product.reviews.add(review)

            return review
        }
    }
}