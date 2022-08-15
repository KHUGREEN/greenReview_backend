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
            picUrl : MutableList<String> = mutableListOf("list"),
            thumbnailUrl : String = "thumbnail",
            registeredDate : Date = Date(),
            originalUrl : String = "originalUrl"
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
                originalUrl = originalUrl
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