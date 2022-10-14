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
        assertEquals(product, result)
    }
}