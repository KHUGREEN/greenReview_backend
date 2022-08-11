package com.khureen.greenReview.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.khureen.greenReview.model.AddReviewDTO
import com.khureen.greenReview.model.ChecklistDTO
import com.khureen.greenReview.model.ProductId
import com.khureen.greenReview.model.ReviewDTO
import com.khureen.greenReview.service.AddReviewService
import com.khureen.greenReview.service.GetReviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController // @Controller를 포함함
@RequestMapping("review")
class ReviewController {
    @Autowired
    lateinit var addReviewService: AddReviewService

    @Autowired
    lateinit var getReviewService: GetReviewService

    @GetMapping("/list/{id}")
    fun readReviewList(
        @PathVariable("id") productId: Long,
        @RequestParam(value = "page", required = true) page: Int,
        @RequestParam(value = "size", required = true) size: Int
    )  : List<ReviewListResponseElement> {
        val result = getReviewService.getReviewWith(ProductId(productId), PageRequest.of(page, size)).map {
            ReviewListResponseElement(
                id = it.id.id,
                nickname = it.review.author,
                content = it.review.content,
                rate = it.review.rate,
                checklist = getChecklistResponse(it.review.checklist)
            )
        }

        return result
    }


    @PostMapping("/write")
    fun createReview(@RequestBody json: String) : ReviewWriteResponse{
        val mapper = jacksonObjectMapper()
        val request = mapper.readValue(json, ReviewWriteRequest::class.java)

        val result = addReviewService.addReview(
            AddReviewDTO(
                ProductId(request.productId),
                ReviewDTO(
                    request.nickname,
                    request.content,
                    request.rate,
                    getChecklistDto(request.checklists),
                    Date()
                )
            )
        )

        return ReviewWriteResponse(result.id)
    }

    fun getChecklistDto(checklist: List<ChecklistElement>): ChecklistDTO {
        //???
    }

    fun getChecklistResponse(checklist: ChecklistDTO): List<ChecklistElement> {
        //???
    }

}

data class ReviewWriteRequest constructor(
    val nickname: String,
    val content: String,
    val rate: Double,
    val productId: Long,
    val checklists: List<ChecklistElement>
)


data class ReviewListResponseElement constructor(
    val id: Long,
    val nickname: String,
    val content: String,
    val rate: Double,
    val checklist: List<ChecklistElement>
)

data class ChecklistElement constructor(
    val id: Int,
    val name: String
)

data class ReviewWriteResponse constructor(
    val id: Long
)