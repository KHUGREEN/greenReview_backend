package com.khureen.greenReview.review

import com.khureen.greenReview.account.Account
import com.khureen.greenReview.product.Product
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@DataJpaTest
class ReviewTest {

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
                    false)
            )
        )

        em.persist(product)
        em.persist(account)
        em.persist(product.reviews.first())
        em.flush()

        assertEquals(em.find(Review::class.java, product.reviews.first().id), product.reviews.first())
    }
}