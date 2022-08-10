package com.khureen.greenReview.service

import com.khureen.greenReview.model.*
import com.khureen.greenReview.repository.ProductRepository
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

fun Product.toDTO(): GetProductDTO {
    return GetProductDTO(
        ProductId(this.id!!),
        ProductDTO(
            name = this.name,
            vendor = this.vendor,
            price = this.price,
            deliveryFee = this.deliveryFee,
            picUrl = this.picUrl.map { it }.toList(),
            registeredDate = this.registeredDate,
            thumbnailUrl = this.thumbnailUrl
        )
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
            reviews = mutableListOf()
        ))
    }
}

@Service
class GetProductServiceImpl {
    @Autowired
    lateinit var productRepo: ProductRepository

    fun getProductById(id: Long): ProductResponse {
        val dto = productRepo.findById(id).get()
        return ProductResponse(dto.toDTO())
    }
}

@Service
class GetProductListServiceImpl {
    @Autowired
    lateinit var productRepo: ProductRepository

    fun getProductList(name: String, page: Pageable): ProductListResponse {
        return ProductListResponse(productRepo.findProductWith(name, page).map { it.toDTO() })
    }
}