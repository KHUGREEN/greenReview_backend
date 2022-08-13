package com.khureen.greenReview

import com.khureen.greenReview.service.GetProductServiceImpl
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@Configuration
class SpringConfiguration (@PersistenceContext val entityManager: EntityManager) {
    @Bean
    fun jpqQueryFactory() = JPAQueryFactory(entityManager)
}
