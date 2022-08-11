package com.khureen.greenReview.controller

import com.khureen.greenReview.service.GetProductListService
import com.khureen.greenReview.service.GetProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

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
            rate = result.product.score.score,
            reviewer = result.product.score.reviewer,
            checkList = result.product.product.checklist
        )
    }

    /*
    *
    * { [
        id: 1,
        pic_url: '.../...png',
        name: '레모나',
        vendor: '일동제약',
        price: 3000,
        deliveryFee: 0,
        originalURL: 'https://...',
        rate : 3.5
        reviewer: 30,
        checklists: [{id: 1, name: '증거', num: 2}, ...]
    *
    *
    * */
    @GetMapping("/list")
    fun getProductList(
        @RequestParam(value = "q", required = true) query: String,
        @RequestParam(value = "page", required = true) page: Int,
        @RequestParam(value = "size", required = true) size: Int
    ) : List<GetProductListResponseElement> {
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
                reviewer = it.reviewer,
                checkList = it.checklist
            )
        }

        return result
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
    val rate : Double,
    val reviewer: Int,
    val checkList: List<ChecklistElement>
)

data class GetProductListResponseElement constructor(
    val id : Long,
    val picThumbnail : String,
    val name : String,
    val vendor : String,
    val price : Int,
    val reviewer : Int,
    val checkList : List<ChecklistElement>
)
