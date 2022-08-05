package com.khureen.greenReview

import com.khureen.greenReview.repository.ProductRepositoryCustomImpl
import com.khureen.greenReview.service.ProductServiceImpl
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@Configuration
class SpringConfiguration (@PersistenceContext val entityManager: EntityManager) {
    @Bean
    fun jpqQueryFactory() = JPAQueryFactory(entityManager)

    @Bean
    fun productService() = ProductServiceImpl()
}
