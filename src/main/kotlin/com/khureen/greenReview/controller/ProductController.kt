package com.khureen.greenReview.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.khureen.greenReview.model.*
import com.khureen.greenReview.service.AddProductService
import com.khureen.greenReview.service.ChecklistService
import com.khureen.greenReview.service.GetProductListService
import com.khureen.greenReview.service.GetProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*

@Controller
@RequestMapping("product")
class ProductController {

    @Autowired
    lateinit var getProductListService: GetProductListService

    @Autowired
    lateinit var getProductDetailService: GetProductService

    @Autowired
    lateinit var addProductService: AddProductService

    @GetMapping("/detail/{id}")
    fun getProductDetail(@PathVariable("id") productId: Int): ResponseEntity<GetProductDetailResponse> {
        val result = getProductDetailService.getProductById(productId.toLong())

        return ResponseEntity.ok(GetProductDetailResponse(
            id = result.product.id.id,
            pic_url = result.product.product.originalUrl,
            name = result.product.product.name,
            vendor = result.product.product.vendor,
            price = result.product.product.price,
            deliveryFee = result.product.product.deliveryFee,
            originalURL = result.product.product.originalUrl,
            detailpicUrl = result.product.product.detailpicUrl,
            rate = result.product.score.map { it.score },
            reviewer = result.product.score.map { it.reviewer },
            checkList = result.product.score.map { getChecklistResponse(it.checklist) }
        ))
    }

    @GetMapping("/list")
    fun getProductList(
        @RequestParam(value = "q", required = true) query: String,
        @RequestParam(value = "page", required = true) page: Int,
        @RequestParam(value = "size", required = true) size: Int
    ): ResponseEntity<List<GetProductListResponseElement>> {
        val result = getProductListService.getProductList(
            query,
            PageRequest.of(page, size)
        ).productList.map {
            GetProductListResponseElement(
                id = it.id,
                picThumbnail = it.thumbnailUrl,
                name = it.name,
                vendor = it.vendor,
                price = it.price,
                reviewer = it.score.map { it.reviewer },
                checkList = it.score.map { getChecklistResponse(it.checklist) },
                rate = it.score.map { it.score }
            )
        }

        return ResponseEntity.ok(result) // Enforces empty array response otherwise get error response
    }

    @PostMapping("/add")
    fun addProduct(@RequestBody json: String): ResponseEntity<AddProductResponse> {
        val mapper = jacksonObjectMapper()
        val request = try {
            mapper.readValue(json, AddProductRequest::class.java)
        } catch (e: Exception) {
            throw ApiException("invaild json: ${e.message}, ${e.stackTraceToString()}", HttpStatusCode.REQUEST_ERROR)
        }

        val result = addProductService.addProduct(
            AddProductDTO(
                ProductDTO(
                    name = request.name,
                    vendor = request.vendor,
                    price = request.price,
                    deliveryFee = request.deliveryFee,
                    picUrl = request.picUrl,
                    registeredDate = request.registeredDate,
                    thumbnailUrl = request.thumbnailUrl,
                    originalUrl = request.originalUrl,
                    detailpicUrl = request.detailpicUrl
                )
            )
        )

        return ResponseEntity.ok(AddProductResponse(result.id))
    }

    fun getChecklistResponse(checklist: ChecklistStatisticsDTO): List<ChecklistStatistics> {
        return listOf(
            ChecklistStatistics(ChecklistService.notSufficientEvidence.id, checklist.notSufficientEvidence),
            ChecklistStatistics(ChecklistService.ambiguousStatement.id, checklist.ambiguousStatement),
            ChecklistStatistics(ChecklistService.lieStatement.id, checklist.lieStatement),
            ChecklistStatistics(ChecklistService.inappropriateCertification.id, checklist.inappropriateCertification)
        )
    }
}


data class AddProductResponse constructor(
    val id: Long
)

data class AddProductRequest constructor(
    val name: String,

    val vendor: String,

    val price: Int,

    val deliveryFee: Int,

    val picUrl: List<String>,

    val registeredDate: Date,

    val thumbnailUrl: String,

    val originalUrl: String,

    val detailpicUrl: List<String>
)

data class GetProductDetailResponse constructor(
    val id: Long,
    val pic_url: String,
    val name: String,
    val vendor: String,
    val price: Int,
    val deliveryFee: Int,
    val originalURL: String,
    val detailpicUrl: List<String>,
    val rate: Optional<Double>,
    val reviewer: Optional<Long>,
    val checkList: Optional<List<ChecklistStatistics>>
)

data class GetProductListResponseElement constructor(
    val id: Long,
    val picThumbnail: String,
    val name: String,
    val vendor: String,
    val price: Int,
    val rate: Optional<Double>,
    val reviewer: Optional<Long>,
    val checkList: Optional<List<ChecklistStatistics>>
)

data class ChecklistStatistics constructor(
    val id: Int, // 1 ~ 7
    val num: Int // 선택한 사람의 수
)