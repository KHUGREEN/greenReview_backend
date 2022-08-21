package com.khureen.greenReview.controller

import com.khureen.greenReview.model.ChecklistDTO
import com.khureen.greenReview.model.ChecklistStatisticsDTO
import com.khureen.greenReview.service.GetProductListService
import com.khureen.greenReview.service.GetProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

@Controller
@RequestMapping("product")
class ProductController {

    @Autowired
    lateinit var getProductListService: GetProductListService

    @Autowired
    lateinit var getProductDetailService: GetProductService

    @GetMapping("/detail/{id}")
    fun getProductDetail(@PathVariable("id") productId: Long) : GetProductDetailResponse {
        val result = getProductDetailService.getProductById(productId)

        return GetProductDetailResponse(
            id = result.product.id.id,
            pic_url = result.product.product.originalUrl,
            name = result.product.product.name,
            vendor = result.product.product.vendor,
            price = result.product.product.price,
            deliveryFee = result.product.product.deliveryFee,
            originalURL = result.product.product.originalUrl,
            rate = result.product.score.map { it.score },
            reviewer = result.product.score.map { it.reviewer },
            checkList = result.product.score.map { getChecklistResponse(it.checklist) }
        )
    }

    @GetMapping("/list")
    fun getProductList(
        @RequestParam(value = "q", required = true) query: String,
        @RequestParam(value = "page", required = true) page: Int,
        @RequestParam(value = "size", required = true) size: Int
    ) : ResponseEntity<List<GetProductListResponseElement>> {
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

    fun getChecklistResponse(checklist: ChecklistStatisticsDTO): List<ChecklistStatistics> {
        return listOf(
            ChecklistStatistics(1, "hidingSideEffects", checklist.hidingSideEffects),
            ChecklistStatistics(2, "notSufficientEvidence", checklist.notSufficientEvidence),
            ChecklistStatistics(3, "ambiguousStatement", checklist.ambiguousStatement),
            ChecklistStatistics(4, "notRelatedStatement", checklist.notRelatedStatement),
            ChecklistStatistics(5, "lieStatement", checklist.lieStatement),
            ChecklistStatistics(6, "justifyingHarmingProduct", checklist.justifyingHarmingProduct),
            ChecklistStatistics(7, "inappropriateCertification", checklist.inappropriateCertification)
        )
    }
}




data class GetProductDetailResponse constructor(
    val id : Long,
    val pic_url : String,
    val name : String,
    val vendor : String,
    val price : Int,
    val deliveryFee : Int,
    val originalURL : String,
    val rate : Optional<Double>,
    val reviewer: Optional<Long>,
    val checkList: Optional<List<ChecklistStatistics>>
)

data class GetProductListResponseElement constructor(
    val id : Long,
    val picThumbnail : String,
    val name : String,
    val vendor : String,
    val price : Int,
    val rate : Optional<Double>,
    val reviewer : Optional<Long>,
    val checkList : Optional<List<ChecklistStatistics>>
)

data class ChecklistStatistics constructor(
    val id: Int, // 1 ~ 7
    val name: String, // id와 같은 의미의 값
    val num : Int // 선택한 사람의 수
)