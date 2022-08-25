package com.khureen.greenReview.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.khureen.greenReview.model.*
import com.khureen.greenReview.service.AddReviewService
import com.khureen.greenReview.service.ChecklistService
import com.khureen.greenReview.service.GetReviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
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
    ): ResponseEntity<List<ReviewListResponseElement>> {
        val result = getReviewService.getReviewWith(ProductId(productId), PageRequest.of(page, size)).map {
            ReviewListResponseElement(
                id = it.id.id,
                nickname = it.review.author,
                content = it.review.content,
                rate = it.review.rate,
                checkTypes = getChecklistResponse(it.review.checklist)
            )
        }

        return ResponseEntity.ok(result)
    }


    @PostMapping("/write")
    fun createReview(@RequestBody json: String): ReviewWriteResponse {
        val mapper = jacksonObjectMapper()
        val request = try {
            mapper.readValue(json, ReviewWriteRequest::class.java)
        } catch (e: Exception) {
            throw ApiException("invaild json: ${e.message}, ${e.stackTraceToString()}", HttpStatusCode.REQUEST_ERROR)
        }

        val result = addReviewService.addReview(
            AddReviewDTO(
                ProductId(request.productId),
                ReviewDTO(
                    request.nickname,
                    request.content,
                    request.rate,
                    getChecklistDto(request.checkTypes),
                    Date()
                )
            )
        )

        return ReviewWriteResponse(result.id)
    }

    fun getChecklistDto(checklist: List<Int>): ChecklistDTO {
        return ChecklistDTO( // O(1) - no optimization needed
            false,
            checklist.contains(ChecklistService.notSufficientEvidence.id),
            checklist.contains(ChecklistService.ambiguousStatement.id),
            false,
            checklist.contains(ChecklistService.lieStatement.id),
            false,
            checklist.contains(ChecklistService.inappropriateCertification.id)
        )
    }

    fun getChecklistResponse(checklist: ChecklistDTO): List<Int> {
        val list = mutableListOf<Int>()

        if (checklist.notSufficientEvidence) {
            list.add(ChecklistService.notSufficientEvidence.id)
        }

        if (checklist.ambiguousStatement) {
            list.add(ChecklistService.ambiguousStatement.id)
        }

        if (checklist.lieStatement) {
            list.add(ChecklistService.lieStatement.id)
        }

        if (checklist.inappropriateCertification) {
            list.add(ChecklistService.inappropriateCertification.id)
        }

        return list
    }
}

data class ReviewWriteRequest constructor(
    val nickname: String,
    val content: String,
    val rate: Double,
    val productId: Long,
    val checkTypes: List<Int>
)


data class ReviewListResponseElement constructor(
    val id: Long,
    val nickname: String,
    val content: String,
    val rate: Double,
    val checkTypes: List<Int>
)

data class ReviewWriteResponse constructor(
    val id: Long
)