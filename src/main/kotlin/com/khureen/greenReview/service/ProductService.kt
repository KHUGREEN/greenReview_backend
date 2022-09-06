package com.khureen.greenReview.service

import com.khureen.greenReview.model.*
import com.khureen.greenReview.repository.ProductRepository
import com.khureen.greenReview.repository.ReviewRepository
import com.khureen.greenReview.repository.dto.Product
import com.khureen.greenReview.repository.dto.ProductListElement
import com.khureen.greenReview.service.dto.ProductListResponse
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

        val avgChecklist = reviewRepo.getAverageChecklistBy(id)
        val reviewStatistics = reviewRepo.getReviewStatisticsBy(id)

        val optionalScore = if (avgChecklist.isPresent && reviewStatistics.isPresent) {
            val s = reviewStatistics.get()
            val a = avgChecklist.get()

            Optional.of(ProductScore(s.reviewer, a))
        } else {
            Optional.empty<ProductScore>()
        }

        return ProductResponse(dto.toGetDTOWith(optionalScore))
    }
}

interface GetProductListService {
    fun getProductList(searchTerm: String, page: Pageable): ProductListResponse
}

@Service
class GetProductListServiceImpl : GetProductListService {

    @Autowired
    lateinit var productRepo: ProductRepository

    @Autowired
    lateinit var reviewRepo: ReviewRepository

    override fun getProductList(searchTerm: String, page: Pageable): ProductListResponse {
        val productList = productRepo.findProductWith(searchTerm, page)

        // TODO: CACHE it
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
}