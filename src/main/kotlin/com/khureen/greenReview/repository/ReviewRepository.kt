package com.khureen.greenReview.repository

import com.khureen.greenReview.repository.dto.*
import com.querydsl.core.types.Projections
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.persistence.PersistenceContext
import org.springframework.stereotype.Repository


interface ReviewRepository : JpaRepository<Review, Long>, ReviewRepositoryCustom {

}

interface ReviewRepositoryCustom {
    fun findByProductId(productId: Long, page: Pageable) : List<Review>
}

@Repository
@Transactional
@Component
@PersistenceContext
class ReviewRepositoryCustomImpl : ReviewRepositoryCustom, QuerydslRepositorySupport(Review::class.java){
    override fun findByProductId(productId: Long, page: Pageable) : List<Review> {
        val qReview = QReview.review

        val result = from(qReview)
            .where(qReview.product.id.eq(productId))
            .orderBy(qReview.registeredDate.desc())
            .offset(page.offset)
            .limit(page.pageSize.toLong())
            .fetch()

        return result
    }
}