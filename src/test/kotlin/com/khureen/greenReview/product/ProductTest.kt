package com.khureen.greenReview.product

import com.khureen.greenReview.account.Account
import com.khureen.greenReview.review.Checklist
import com.khureen.greenReview.review.Review
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@DataJpaTest
class ProductTest {

    @PersistenceContext
    lateinit var em: EntityManager

    @Test
    fun ctor_PersistenceTest() {
        val product = Product(
            name = "name",
            vendor = "vendor",
            price = 0,
            deliveryFee = 0,
            originalUrl = mutableListOf("list"),
            thumbnailUrl = "thumbnail",
            reviews = mutableListOf()
        )

        em.persist(product)
        em.flush()

        assertEquals(em.find(Product::class.java, product.id), product)
    }


    @Test
    fun ctor_AddReviewTest() {
        val product = Product(
            name = "name",
            vendor = "vendor",
            price = 0,
            deliveryFee = 0,
            originalUrl = mutableListOf("list"),
            thumbnailUrl = "thumbnail",
            reviews = mutableListOf()
        )

        em.persist(product)

        product.reviews.add(
            Review(
                author = Account(
                    name = "name",
                    imageUrl = "url"
                ),
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
                    false)
            )
        )

        em.flush()

        assertEquals(em.find(Product::class.java, product.id).reviews.first().author.name, "name")
        assertEquals(em.find(Product::class.java, product.id).reviews.first().author.imageUrl, "url")
        assertEquals(em.find(Product::class.java, product.id).reviews.first().rate, 0.0)
        assertEquals(em.find(Product::class.java, product.id).reviews.first().content, "content")
    }
}