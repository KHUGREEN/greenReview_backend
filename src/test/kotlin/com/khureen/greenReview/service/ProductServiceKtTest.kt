package com.khureen.greenReview.service

import com.khureen.greenReview.TestUtil
import com.khureen.greenReview.repository.dto.ProductListElement
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@SpringBootTest
internal class ProductServiceKtTest {

    @PersistenceContext
    lateinit var entityManager: EntityManager

    @Test
    @Transactional
    fun product_toDTO_test() {
        // given
        val product = TestUtil.getProduct()
        entityManager.persist(product) // to get id

        // when
        val result = product.toDTO()

        // then
        assertEquals(product.id, result.id)
        assertEquals(product.registeredDate, result.registeredDate)
        assertEquals(product.originalUrl.map { it }.toList(), result.originalUrl.map { it }.toList())
        assertEquals(product.price, result.price)
        assertEquals(product.deliveryFee, result.deliveryFee)
        assertEquals(product.vendor, result.vendor)
        assertEquals(product.name, result.name)
    }

    @Test
    @Transactional
    fun product_list_toDto() {
        // given
        val product = TestUtil.getProduct()
        entityManager.persist(product) // to get id

        // when
        val productListItem =
            ProductListElement(product.id!!, product.thumbnailUrl, product.name, product.vendor, product.price)

        // then
        val result = productListItem.toDTO()

        assertEquals(productListItem.id, result.id)
        assertEquals(productListItem.thumbnailUrl, result.thumbnailUrl)
        assertEquals(productListItem.name, result.name)
        assertEquals(productListItem.vendor, result.vendor)
        assertEquals(productListItem.price, result.price)
    }
}