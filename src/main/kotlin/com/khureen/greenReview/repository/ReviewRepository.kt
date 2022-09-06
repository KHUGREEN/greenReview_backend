package com.khureen.greenReview.repository

import com.khureen.greenReview.model.ChecklistStatisticsDTO
import com.khureen.greenReview.repository.dto.Checklist
import com.khureen.greenReview.repository.dto.QReview
import com.khureen.greenReview.repository.dto.Review
import com.querydsl.core.types.Projections
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.persistence.PersistenceContext


interface ReviewRepository : JpaRepository<Review, Long>, ReviewRepositoryCustom {

}

interface ReviewRepositoryCustom {
    fun findByProductId(productId: Long, page: Pageable): List<Review>

    fun getAverageChecklistBy(productId: Long): Optional<ChecklistStatisticsDTO>

    fun getReviewStatisticsBy(productId: Long) : Optional<ReviewStatistics>
}

@Repository
@Transactional
@Component
@PersistenceContext
class ReviewRepositoryCustomImpl : ReviewRepositoryCustom, QuerydslRepositorySupport(Review::class.java) {
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

    override fun getAverageChecklistBy(productId: Long): Optional<ChecklistStatisticsDTO> {
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

    override fun getReviewStatisticsBy(productId: Long): Optional<ReviewStatistics> {
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

}

class ReviewStatistics constructor(
    val reviewer: Long
)