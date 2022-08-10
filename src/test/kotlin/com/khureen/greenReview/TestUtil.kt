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
                registeredDate = Date()
            )

            return product
        }

        fun getAccount(): Account {
            return Account(name = "abc", imageUrl = "unused")
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
                registeredDate = Date()
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
                    checklist = Checklist(
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false
                    ),
                    registeredDate = Date()
                )
            )

            return product.reviews.first()
        }
    }
}