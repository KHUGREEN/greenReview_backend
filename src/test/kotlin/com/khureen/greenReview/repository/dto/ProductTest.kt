package com.khureen.greenReview.repository.dto

import com.khureen.greenReview.TestUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@DataJpaTest
class ProductTest {

    @PersistenceContext
    lateinit var em: EntityManager

    @Test
    fun ctor_PersistenceTest() {
        // given
        val product = TestUtil.getProduct()

        // when
        em.persist(product)
        em.flush()

        // then
        assertEquals(em.find(Product::class.java, product.id), product)
    }


    @Test
    fun ctor_AddReviewTest() {
        // given
        val product = TestUtil.getProduct()
        val account = TestUtil.getAccount()

        val review = Review(
            author = account,
            product =  product,
            content = "abc",
            rate = 0.0,
            checklist = Checklist(false, false, false, false, false, false, false),
            registeredDate = Date()
        )

        product.reviews.add(review)

        // when
        em.persist(product)
        em.persist(account)
        em.persist(review)
        em.flush()

        // then
        assertEquals(em.find(Review::class.java, review.id).author.name, account.name)
        assertEquals(em.find(Review::class.java, review.id).author.imageUrl, account.imageUrl)
        assertEquals(em.find(Review::class.java, review.id).rate, 0.0)
        assertEquals(em.find(Review::class.java, review.id).content, review.content)
    }
}