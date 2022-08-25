package com.khureen.greenReview.service

import com.khureen.greenReview.model.*
import com.khureen.greenReview.repository.ProductRepository
import com.khureen.greenReview.repository.ReviewRepository
import com.khureen.greenReview.repository.dto.Checklist
import com.khureen.greenReview.repository.dto.Review
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


fun Review.toDto(): GetReviewDTO {
    return GetReviewDTO(
        ReviewId(this.id!!), ReviewDTO(
            author = this.author,
            content = this.content,
            rate = this.rate,
            checklist = this.checklist.toDto(),
            registeredDate = this.registeredDate
        )
    )
}

fun Checklist.toDto(): ChecklistDTO {
    return ChecklistDTO(
        hidingSideEffects = hidingSideEffects == 1,
        notSufficientEvidence = notSufficientEvidence == 1,
        ambiguousStatement = ambiguousStatement == 1,
        notRelatedStatement = notRelatedStatement == 1,
        lieStatement = lieStatement == 1,
        justifyingHarmingProduct = justifyingHarmingProduct == 1,
        inappropriateCertification = inappropriateCertification == 1
    )
}

fun ChecklistDTO.toEntity(): Checklist {
    return Checklist(
        hidingSideEffects = if (hidingSideEffects) 1 else 0,
        notSufficientEvidence = if (notSufficientEvidence) 1 else 0,
        ambiguousStatement = if (ambiguousStatement) 1 else 0,
        notRelatedStatement = if (notRelatedStatement) 1 else 0,
        lieStatement = if (lieStatement) 1 else 0,
        justifyingHarmingProduct = if (justifyingHarmingProduct) 1 else 0,
        inappropriateCertification = if (inappropriateCertification) 1 else 0
    )
}


interface GetReviewService {
    fun getReviewWith(product: ProductId, page: Pageable): List<GetReviewDTO>
}

@Service
class GetReviewServiceImpl : GetReviewService {
    @Autowired
    lateinit var reviewRepository: ReviewRepository

    @Autowired
    lateinit var productRepository: ProductRepository

    override fun getReviewWith(product: ProductId, page: Pageable): List<GetReviewDTO> {
        productRepository.findById(product.id).orElseThrow {
            throw ProductIdNotFoundException(product.id)
        } // just for check (remove?)

        return reviewRepository.findByProductId(product.id, page).map {
            it.toDto()
        }
    }
}


interface AddReviewService {
    fun addReview(review: AddReviewDTO): ReviewId
}

@Service
class AddReviewServiceImpl : AddReviewService {
    @Autowired
    lateinit var reviewRepository: ReviewRepository

    @Autowired
    lateinit var productRepository: ProductRepository


    override fun addReview(review: AddReviewDTO): ReviewId {
        val product = productRepository.findById(review.product.id).orElseThrow {
            throw ProductIdNotFoundException(review.product.id)
        }

        if (isMalformedReview(review)) {
            throw ApiException(
                "malformed input: author and content should not be empty and rate is between 0.0 and 5.0",
                HttpStatusCode.REQUEST_ERROR
            )
        }

        val reviewEntity = Review(
            author = review.review.author,
            product = product,
            content = review.review.content,
            checklist = review.review.checklist.toEntity(),
            rate = review.review.rate,
            registeredDate = review.review.registeredDate
        )

        reviewRepository.save(reviewEntity)
        product.reviews.add(reviewEntity)

        return ReviewId(reviewEntity.id!!)
    }

    private fun isMalformedReview(review: AddReviewDTO): Boolean {
        return review.review.author.isBlank() or review.review.content.isBlank() or ((review.review.rate >= 5.0) or (review.review.rate <= 0.0))
    }
}