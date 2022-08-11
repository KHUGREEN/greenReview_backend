package com.khureen.greenReview.repository.dto

import com.khureen.greenReview.TestUtil
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
        // given
        val review = TestUtil.getReview()

        // when
        em.persist(review.product)
        em.persist(review)
        em.flush()

        // then
        assertEquals(em.find(Review::class.java, review.id), review)
    }
}