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

import com.khureen.greenReview.model.*
import com.khureen.greenReview.repository.ProductRepository
import com.khureen.greenReview.repository.ReviewRepository
import com.khureen.greenReview.repository.dto.Product
import com.khureen.greenReview.repository.dto.ProductListElement
import com.khureen.greenReview.service.dto.ProductChecklistResponse
import com.khureen.greenReview.service.dto.ProductListResponse
import com.khureen.greenReview.service.dto.ProductMaxSizeResponse
import com.khureen.greenReview.service.dto.ProductResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

/**
 *
 * A DetailPic Url Separator. Detail pic url will join and splits with this char.
 * Workaround for Heroku free plan row limits.
 * */
const val DETAILPIC_URL_SEPARATOR : Char = '\u0713'

fun ProductListElement.toDTOWith(score: Optional<ProductScore>): ProductListElementDTO {
    return ProductListElementDTO(
        this.id,
        this.thumbnailUrl,
        this.name,
        this.vendor,
        this.price,
        score
    )
}

fun Product.toGetDTOWith(score: Optional<ProductScore>): GetProductDTO {
    return GetProductDTO(
        ProductId(this.id!!),
        ProductDTO(
            name = this.name,
            vendor = this.vendor,
            price = this.price,
            deliveryFee = this.deliveryFee,
            picUrl = this.picUrl,
            registeredDate = this.registeredDate,
            thumbnailUrl = this.thumbnailUrl,
            originalUrl = this.originalUrl,
            detailpicUrl = this.detailpicUrl.split(DETAILPIC_URL_SEPARATOR)
        ),
        score
    )
}

fun Product.toGetChecklistDTOWith(score: Optional<ProductScore>) : GetProductScoreDTO {
    return GetProductScoreDTO(
        ProductId(this.id!!),
        score
    )
}

@Service
class AddProductService {
    @Autowired
    lateinit var productRepository: ProductRepository

    fun addProduct(product: AddProductDTO) : ProductId{
        val result = Product(
            name = product.product.name,
            vendor = product.product.vendor,
            picUrl = product.product.picUrl,
            thumbnailUrl = product.product.thumbnailUrl,
            price = product.product.price,
            deliveryFee = product.product.deliveryFee,
            registeredDate = product.product.registeredDate,
            reviews = mutableListOf(),
            originalUrl = product.product.originalUrl,
            detailpicUrl = product.product.detailpicUrl.joinToString(separator = DETAILPIC_URL_SEPARATOR.toString())
        )

        productRepository.save(
            result
        )

        return ProductId(result.id!!)
    }
}

interface GetProductService {
    fun getProductById(id: Long): ProductResponse

    fun getProductChecklistById(id: Long): ProductChecklistResponse
}

@Service
class GetProductServiceImpl : GetProductService {
    @Autowired
    lateinit var productRepo: ProductRepository

    @Autowired
    lateinit var reviewRepo: ReviewRepository

    @Transactional
    override fun getProductById(id: Long): ProductResponse {
        val dto = productRepo.findById(id).orElseThrow {
            throw ApiException("can't find product matching with id", HttpStatusCode.NOT_FOUND)
        }

        val optionalScore = getOptionalScore(id)

        return ProductResponse(dto.toGetDTOWith(optionalScore))
    }

    override fun getProductChecklistById(id: Long): ProductChecklistResponse {
        val dto = productRepo.findById(id).orElseThrow {
            throw ApiException("can't find product matching with id", HttpStatusCode.NOT_FOUND)
        } // TODO: Optimize? may require custom methods for repo

        val optionalScore = getOptionalScore(id)

        return ProductChecklistResponse(dto.toGetChecklistDTOWith(optionalScore))
    }

    private fun getOptionalScore(id: Long): Optional<ProductScore> {
        val avgChecklist = reviewRepo.getAverageChecklistBy(id)
        val reviewStatistics = reviewRepo.getReviewStatisticsBy(id)

        val optionalScore = if (avgChecklist.isPresent && reviewStatistics.isPresent) {
            val s = reviewStatistics.get()
            val a = avgChecklist.get()

            Optional.of(ProductScore(s.reviewer, a))
        } else {
            Optional.empty<ProductScore>()
        }
        return optionalScore
    }
}

interface GetProductListService {
    fun getProductList(searchTerm: String, page: Pageable): ProductListResponse

    fun getProductMaxSize(searchTerm: String): ProductMaxSizeResponse
}

@Service
class GetProductListServiceImpl : GetProductListService {

    @Autowired
    lateinit var productRepo: ProductRepository

    @Autowired
    lateinit var reviewRepo: ReviewRepository

    override fun getProductList(searchTerm: String, page: Pageable): ProductListResponse {
        val productList = productRepo.findProductWith(searchTerm, page)

        val reviewList = productList.map {
            val avgChecklist = reviewRepo.getAverageChecklistBy(it.id)
            val reviewStatistics = reviewRepo.getReviewStatisticsBy(it.id)

            if (avgChecklist.isPresent && reviewStatistics.isPresent) {
                val s = reviewStatistics.get()
                val a = avgChecklist.get()

                Optional.of(ProductScore(s.reviewer, a))
            } else {
                Optional.empty<ProductScore>()
            }
        }

        val summedList = productList.zip(reviewList)

        return ProductListResponse(summedList.map {
            it.first.toDTOWith(it.second)
        })
    }

    override fun getProductMaxSize(searchTerm: String): ProductMaxSizeResponse {
        val size = productRepo.findProductMaxSize(searchTerm)

        return ProductMaxSizeResponse(size)
    }
}