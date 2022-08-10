package com.khureen.greenReview

import com.khureen.greenReview.repository.dto.Account
import com.khureen.greenReview.repository.dto.Checklist
import com.khureen.greenReview.repository.dto.Product
import com.khureen.greenReview.repository.dto.Review
import java.util.*

class TestUtil {
    companion object {
        fun getProduct(): Product {
            val product = Product(
                name = "name",
                vendor = "vendor",
                price = 0,
                deliveryFee = 0,
                picUrl = mutableListOf("list"),
                thumbnailUrl = "thumbnail",
                reviews = mutableListOf(),
                registeredDate = Date(),
                originalUrl = "originalUrl"
            )

            return product
        }

        fun getAccount(): Account {
            return Account(name = "abc", imageUrl = "unused")
        }

        fun getChecklist() : Checklist {
            return Checklist(
                false,
                false,
                false,
                false,
                false,
                false,
                false
            )
        }

        fun getReview(): Review {
            val product = Product(
                name = "name",
                vendor = "vendor",
                price = 0,
                deliveryFee = 0,
                picUrl = mutableListOf("list"),
                thumbnailUrl = "thumbnail",
                reviews = mutableListOf(),
                registeredDate = Date(),
                originalUrl = "originalUrl"
            )

            val account = Account(
                name = "name",
                imageUrl = "url"
            )

            product.reviews.add(
                Review(
                    author = account,
                    product = product,
                    content = "content",
                    rate = 0.0,
                    checklist = getChecklist(),
                    registeredDate = Date()
                )
            )

            return product.reviews.first()
        }
    }
}