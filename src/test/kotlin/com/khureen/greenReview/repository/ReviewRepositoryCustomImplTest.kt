package com.khureen.greenReview.repository

import com.khureen.greenReview.TestUtil
import com.khureen.greenReview.repository.dto.Product
import com.khureen.greenReview.repository.dto.Review
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

@SpringBootTest
internal class ReviewRepositoryCustomImplTest {
    @Autowired
    lateinit var reviewRepository: ReviewRepository

    @PersistenceContext
    lateinit var entityManager: EntityManager

    @Test
    @Transactional
    fun findByProductId() {
        //given
        val review = TestUtil.getReview()
        entityManager.persist(review.product)
        entityManager.persist(review.author)
        entityManager.persist(review)

        //when
        val result = reviewRepository.findByProductId(review.product.id!!, PageRequest.of(0, 10))

        assertEquals(result.size, 1)
        assertEquals(result.first(), review)
    }

    @Test
    @Transactional
    fun getAverageByProductId() {

        //given
        val product = TestUtil.getProduct()
        val account = TestUtil.getAccount()

        val zeroScoredReview = Review(
            author = account,
            product = product,
            content = "",
            registeredDate = Date(),
            rate = 0.0,
            checklist = TestUtil.getChecklist()
        )

        val oneScoredReview = Review(
            author = account,
            product = product,
            content = "",
            registeredDate = Date(),
            rate = 1.0,
            checklist = TestUtil.getChecklist()
        )

        entityManager.persist(product)
        entityManager.persist(account)

        reviewRepository.save(zeroScoredReview)
        reviewRepository.save(oneScoredReview)

        //when
        val score = reviewRepository.getAverageByProduct(product.id!!)

        //then
        assertEquals(0.5, score)
    }
}