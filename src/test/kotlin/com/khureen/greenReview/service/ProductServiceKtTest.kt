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

package com.khureen.greenReview.service

import com.khureen.greenReview.TestUtil
import com.khureen.greenReview.repository.dto.ProductListElement
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.util.*
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
        val result = product.toGetDTOWith(Optional.empty())

        // then
        assertEquals(product.id, result.id.id)
        assertEquals(product.registeredDate, result.product.registeredDate)
        assertEquals(product.picUrl.map { it }.toList(), result.product.picUrl.map { it }.toList())
        assertEquals(product.price, result.product.price)
        assertEquals(product.deliveryFee, result.product.deliveryFee)
        assertEquals(product.vendor, result.product.vendor)
        assertEquals(product.name, result.product.name)
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
        val result = productListItem.toDTOWith(Optional.empty())

        assertEquals(productListItem.id, result.id)
        assertEquals(productListItem.thumbnailUrl, result.thumbnailUrl)
        assertEquals(productListItem.name, result.name)
        assertEquals(productListItem.vendor, result.vendor)
        assertEquals(productListItem.price, result.price)
    }
}