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

import com.khureen.greenReview.model.ChecklistStatisticsDTO
import com.khureen.greenReview.repository.dto.QReview
import com.khureen.greenReview.repository.dto.Review
import com.querydsl.core.types.Projections
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.ConcurrentLruCache
import java.util.*
import javax.persistence.PersistenceContext


interface ReviewRepository : JpaRepository<Review, Long>, ReviewRepositoryCustom {

}

interface ReviewRepositoryCustom {
    fun findByProductId(productId: Long, page: Pageable): List<Review>

    fun getAverageChecklistBy(productId: Long): Optional<ChecklistStatisticsDTO>

    fun getReviewStatisticsBy(productId: Long) : Optional<ReviewStatistics>

    fun invalidateCache(productId: Long)
}

@Repository
@Transactional
@Component
@PersistenceContext
class ReviewRepositoryCustomImpl : ReviewRepositoryCustom, QuerydslRepositorySupport(Review::class.java) {
    val reviewerCache = ConcurrentLruCache<Long, Optional<ReviewStatistics>>(100) {
        getReviewStatisticsBy_Internal(it)
    }

    val checklistCache = ConcurrentLruCache<Long, Optional<ChecklistStatisticsDTO>>(100) {
        getAverageChecklistBy_Internal(it)
    }

    override fun findByProductId(productId: Long, page: Pageable): List<Review> {
        val qReview = QReview.review

        val result = from(qReview)
            .where(qReview.product.id.eq(productId))
            .orderBy(qReview.registeredDate.desc())
            .offset(page.offset)
            .limit(page.pageSize.toLong())
            .fetch()

        return result
    }

    private fun getAverageChecklistBy_Internal(productId: Long) : Optional<ChecklistStatisticsDTO> {
        val qReview = QReview.review

        val avgChecklist = from(qReview)
            .where(qReview.product.id.eq(productId))
            .select(
                Projections.constructor(
                    ChecklistStatisticsDTO::class.java,
                    qReview.checklist.hidingSideEffects.sum(),
                    qReview.checklist.notSufficientEvidence.sum(),
                    qReview.checklist.ambiguousStatement.sum(),
                    qReview.checklist.notRelatedStatement.sum(),
                    qReview.checklist.lieStatement.sum(),
                    qReview.checklist.justifyingHarmingProduct.sum(),
                    qReview.checklist.inappropriateCertification.sum()
                )
            )
            .fetchOne()

        return Optional.ofNullable(avgChecklist)
    }

    override fun getAverageChecklistBy(productId: Long): Optional<ChecklistStatisticsDTO> {
        return checklistCache.get(productId)
    }

    private fun getReviewStatisticsBy_Internal(productId: Long): Optional<ReviewStatistics> {
        val qReview = QReview.review

        val result = from(qReview)
            .where(qReview.product.id.eq(productId))
            .select(
                Projections.constructor(
                    ReviewStatistics::class.java,
                    qReview.count()
                )
            )
            .fetchOne()

        return Optional.ofNullable(result)
    }

    override fun getReviewStatisticsBy(productId: Long): Optional<ReviewStatistics> {
        return reviewerCache.get(productId)
    }

    override fun invalidateCache(productId: Long) {
        reviewerCache.remove(productId)
        checklistCache.remove(productId)
    }

}

class ReviewStatistics constructor(
    val reviewer: Long
)