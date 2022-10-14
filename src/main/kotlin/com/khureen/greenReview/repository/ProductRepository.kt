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

import com.khureen.greenReview.repository.dto.Product
import com.khureen.greenReview.repository.dto.ProductListElement
import com.khureen.greenReview.repository.dto.QProduct
import com.querydsl.core.types.Projections
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.PersistenceContext

interface ProductRepository : JpaRepository<Product, Long>, ProductRepositoryCustom {

}

interface ProductRepositoryCustom {
    fun findProductWith(searchTerm : String, page: Pageable) : List<ProductListElement>
}

@Repository
@Transactional
@Component
@PersistenceContext
class ProductRepositoryCustomImpl : ProductRepositoryCustom, QuerydslRepositorySupport(Product::class.java) {
    override fun findProductWith(searchTerm: String, page: Pageable) : List<ProductListElement> {
        val qProduct = QProduct.product

        val result = from(qProduct)
            .where(qProduct.name.contains(searchTerm))
            .orderBy(qProduct.registeredDate.desc())
            .offset(page.offset)
            .limit(page.pageSize.toLong())
            .select(Projections.constructor(
                ProductListElement::class.java,
                qProduct.id,
                qProduct.thumbnailUrl,
                qProduct.name,
                qProduct.vendor,
                qProduct.price
            ))
            .fetch()

        return result
    }
}