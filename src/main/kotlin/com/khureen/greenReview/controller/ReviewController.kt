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
                checklist = getChecklistResponse(it.review.checklist)
            )
        }

        return ResponseEntity.ok(result)
    }


    @PostMapping("/write")
    fun createReview(@RequestBody json: String): ReviewWriteResponse {
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
        return ChecklistDTO( // O(1) - no optimization needed
            checklist.contains(hidingSideEffects),
            checklist.contains(notSufficientEvidence),
            checklist.contains(ambiguousStatement),
            checklist.contains(notRelatedStatement),
            checklist.contains(lieStatement),
            checklist.contains(justifyingHarmingProduct),
            checklist.contains(inappropriateCertification)
        )
    }

    val hidingSideEffects = ChecklistElement(1, "hidingSideEffects")

    val notSufficientEvidence = ChecklistElement(2, "notSufficientEvidence")

    val ambiguousStatement = ChecklistElement(3, "ambiguousStatement")

    val notRelatedStatement = ChecklistElement(4, "notRelatedStatement")

    val lieStatement = ChecklistElement(5, "lieStatement")

    val justifyingHarmingProduct = ChecklistElement(6, "justifyingHarmingProduct")

    val inappropriateCertification = ChecklistElement(7, "inappropriateCertification")

    fun getChecklistResponse(checklist: ChecklistDTO): List<ChecklistElement> {
        val list = mutableListOf<ChecklistElement>()

        if (checklist.hidingSideEffects) {
            list.add(hidingSideEffects)
        }

        if (checklist.notSufficientEvidence) {
            list.add(notSufficientEvidence)
        }

        if (checklist.ambiguousStatement) {
            list.add(ambiguousStatement)
        }

        if (checklist.notRelatedStatement) {
            list.add(notRelatedStatement)
        }

        if (checklist.lieStatement) {
            list.add(lieStatement)
        }

        if (checklist.justifyingHarmingProduct) {
            list.add(justifyingHarmingProduct)
        }

        if (checklist.inappropriateCertification) {
            list.add(inappropriateCertification)
        }

        return list
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
    val id: Int, // 1 ~ 7
    val name: String // id와 같은 의미의 값
)

data class ReviewWriteResponse constructor(
    val id: Long
)