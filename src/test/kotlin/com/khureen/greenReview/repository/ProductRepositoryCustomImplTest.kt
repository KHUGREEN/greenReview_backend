package com.khureen.greenReview.repository

import com.khureen.greenReview.TestUtil
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
internal class ProductRepositoryCustomImplTest {
    @Autowired
    lateinit var productRepository : ProductRepository

    @Test
    @Transactional // Roll-back after test
    fun getProductListTest() {
        // given
        val product = TestUtil.getProduct()
        productRepository.save(product)

        // when
        val result = productRepository.findProductWith(product.name, PageRequest.of(0, 10)).first()

        // then
        assertEquals(product.id, result.id)
        assertEquals(product.name, result.name)
        assertEquals(product.price, result.price)
        assertEquals(product.thumbnailUrl, result.thumbnailUrl)
        assertEquals(product.vendor, result.vendor)
    }

    @Test
    @Transactional // Roll-back after test
    fun getProductTest() {
        // given
        val product = TestUtil.getProduct()
        productRepository.save(product)

        // when
        val result = productRepository.findById(product.id!!).get()

        // then
        assertEquals(product.id, result.id)
        assertEquals(product.name, result.name)
        assertEquals(product.vendor, result.vendor)
        assertEquals(product.price, result.price)
        assertEquals(product.thumbnailUrl, result.thumbnailUrl)
        assertEquals(product.deliveryFee, result.deliveryFee)
        assertEquals(product.picUrl, result.picUrl)
        assertEquals(product.registeredDate, result.registeredDate)
        assertEquals(product.reviews, result.reviews)
    }
}