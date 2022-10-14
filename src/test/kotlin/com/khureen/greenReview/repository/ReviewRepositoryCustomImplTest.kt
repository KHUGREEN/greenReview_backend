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

package com.khureen.greenReview.repository

import com.khureen.greenReview.TestUtil
import com.khureen.greenReview.repository.dto.Checklist
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

        val product = Product(
            name = "name",
            vendor = "vendor",
            price = 0,
            deliveryFee = 0,
            picUrl = "list",
            thumbnailUrl = "thumbnail",
            reviews = mutableListOf(),
            registeredDate = Date(),
            originalUrl = "originalUrl",
            detailpicUrl = "detailUrl"
        )

        product.reviews.add(
            Review(
                author = "account",
                product = product,
                content = "content",
                rate = 0.0,
                checklist = TestUtil.getChecklist(),
                registeredDate = Date()
            )
        )

        val review = product.reviews.first()
        entityManager.persist(review.product)
        entityManager.persist(review)

        //when
        val result = reviewRepository.findByProductId(review.product.id!!, PageRequest.of(0, 10))

        assertEquals(result.size, 1)
        assertEquals(result.first(), review)
    }

    @Test
    @Transactional
    fun getAverageByProductIdWhen() {

        // given
        val a_product = TestUtil.getProduct()
        val a_all_review = TestUtil.getReview(a_product, checklist = Checklist(
            1,
            1,
            1,
            1,
            1,
            1,
            1
        ))

        val a_some_review = TestUtil.getReview(a_product, checklist = Checklist(
            1,
            0,
            0,
            0,
            0,
            0,
            0
        ))

        entityManager.persist(a_product)

        reviewRepository.save(a_all_review)
        reviewRepository.save(a_some_review)

        val b_product = TestUtil.getProduct()
        val b_all_review = TestUtil.getReview(b_product, checklist = Checklist(
            1,
            1,
            1,
            1,
            1,
            1,
            1
        )
        )

        entityManager.persist(b_product)
        reviewRepository.save(b_all_review)

        // when

        val result = reviewRepository.getAverageChecklistBy(a_product.id!!)

        // then

        assertEquals(1, result.get().ambiguousStatement)
        assertEquals(2, result.get().hidingSideEffects)
    }

    @Test
    @Transactional
    fun getAverageByProductId() {

        //given
        val a_product = TestUtil.getProduct(name = "a") // should not be affected from other product's review
        val b_product = TestUtil.getProduct(name = "b")

        val a_zeroScoredReview = TestUtil.getReview(a_product, rate = 0.0)

        val a_oneScoredReview = TestUtil.getReview(a_product, rate = 1.0)

        val b_zeroScoredReview = TestUtil.getReview(b_product, rate = 0.0)

        reviewRepository.save(a_zeroScoredReview)
        reviewRepository.save(a_oneScoredReview)
        reviewRepository.save(b_zeroScoredReview)

        entityManager.persist(a_product)
        entityManager.persist(b_product)

        //when
        val score = reviewRepository.getReviewStatisticsBy(a_product.id!!)

        //then
        assertEquals(2, score.get().reviewer)
    }

    @Test
    @Transactional
    fun getReviewListByProductId() {

        //given

        val product = TestUtil.getProduct()
        entityManager.persist(product)

        for (i in 0 until 11) {
            val review = TestUtil.getReview(product)

            reviewRepository.save(review)
        }

        //when
        val result = reviewRepository.findByProductId(product.id!!, PageRequest.of(0, 10))

        assertEquals(10, result.size)
    }
}