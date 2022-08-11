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

fun ProductListElement.toDTO(): ProductListElementDTO {
    return ProductListElementDTO(this.id, this.thumbnailUrl, this.name, this.vendor, this.price)
}

fun Product.toGetDTOWith(score: Double): GetProductDTO {
    return GetProductDTO(
        ProductId(this.id!!),
        ProductDTO(
            name = this.name,
            vendor = this.vendor,
            price = this.price,
            deliveryFee = this.deliveryFee,
            picUrl = this.picUrl.map { it }.toList(),
            registeredDate = this.registeredDate,
            thumbnailUrl = this.thumbnailUrl,
            originalUrl = this.originalUrl
        ),
        ProductScore(score)
    )
}

@Service
class AddProductService {
    @Autowired
    lateinit var productRepository: ProductRepository

    fun addProduct(product: AddProductDTO) {
        productRepository.save(Product(
            name = product.product.name,
            vendor = product.product.vendor,
            picUrl = product.product.picUrl.toMutableList(),
            thumbnailUrl = product.product.thumbnailUrl,
            price = product.product.price,
            deliveryFee = product.product.deliveryFee,
            registeredDate = product.product.registeredDate,
            reviews = mutableListOf(),
            originalUrl = product.product.originalUrl
        ))
    }
}

interface GetProductService {
    fun getProductById(id: Long): ProductResponse
}

@Service
class GetProductServiceImpl : GetProductService{
    @Autowired
    lateinit var productRepo: ProductRepository

    @Autowired
    lateinit var reviewRepo: ReviewRepository

    override fun getProductById(id: Long): ProductResponse {
        val dto = productRepo.findById(id).get()
        val score = reviewRepo.getAverageByProduct(id)
        return ProductResponse(dto.toGetDTOWith(score))
    }
}

interface GetProductListService {
    fun getProductList(searchTerm: String, page: Pageable): ProductListResponse
}

@Service
class GetProductListServiceImpl : GetProductListService {
    @Autowired
    lateinit var productRepo: ProductRepository

    override fun getProductList(searchTerm: String, page: Pageable): ProductListResponse {
        return ProductListResponse(productRepo.findProductWith(searchTerm, page).map { it.toDTO() })
    }
}