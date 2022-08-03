package com.khureen.greenReview

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.sql.SQLException
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@Configuration
class QuerydslConfig (@PersistenceContext val entityManager: EntityManager) {
    @Bean
    fun jpqQueryFactory() = JPAQueryFactory(entityManager)
}
