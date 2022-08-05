package com.khureen.greenReview.repository

import com.khureen.greenReview.repository.dto.Product
import com.khureen.greenReview.repository.dto.Review
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.Repository


interface ReviewRepository : Repository<Review, Long>, QuerydslPredicateExecutor<Review> {
    fun findByProduct(product: Product, pageable: Pageable) : Page<Review>
}