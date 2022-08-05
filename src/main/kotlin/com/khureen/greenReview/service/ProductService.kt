package com.khureen.greenReview.service

import com.khureen.greenReview.model.ProductDTO
import com.khureen.greenReview.model.ProductListElementDTO
import com.khureen.greenReview.repository.ProductRepositoryCustom
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

fun Product.toDTO(): ProductDTO {
    return ProductDTO(
        this.id!!,
        this.name,
        this.vendor,
        this.price,
        this.deliveryFee,
        this.originalUrl.map { it }.toList(),
        this.registeredDate
    )
}

@Service
class ProductServiceImpl {
    @Autowired
    lateinit var productRepo: ProductRepositoryCustom

    fun getProductList(name: String, page: Pageable): ProductListResponse {
        return ProductListResponse(productRepo.findProductWith(name, page).map { it.toDTO() })
    }

    fun getProductById(id: Long): ProductResponse {
        val dto = productRepo.getProduct(id)
        return ProductResponse(dto.toDTO())
    }
}