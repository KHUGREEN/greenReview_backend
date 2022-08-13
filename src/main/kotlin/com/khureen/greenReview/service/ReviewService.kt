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
        hidingSideEffects = hidingSideEffects == 1.0,
        notSufficientEvidence = notSufficientEvidence == 1.0,
        ambiguousStatement = ambiguousStatement == 1.0,
        notRelatedStatement = notRelatedStatement == 1.0,
        lieStatement = lieStatement == 1.0,
        justifyingHarmingProduct = justifyingHarmingProduct == 1.0,
        inappropriateCertification = inappropriateCertification == 1.0
    )
}

fun ChecklistDTO.toEntity(): Checklist {
    return Checklist(
        hidingSideEffects = if(hidingSideEffects) 1.0 else 0.0,
        notSufficientEvidence = if(notSufficientEvidence) 1.0 else 0.0,
        ambiguousStatement = if(ambiguousStatement) 1.0 else 0.0,
        notRelatedStatement = if(notRelatedStatement) 1.0 else 0.0,
        lieStatement = if(lieStatement) 1.0 else 0.0,
        justifyingHarmingProduct = if(justifyingHarmingProduct) 1.0 else 0.0,
        inappropriateCertification = if(inappropriateCertification) 1.0 else 0.0
    )
}


interface GetReviewService {
    fun getReviewWith(product: ProductId, page: Pageable): List<GetReviewDTO>
}

@Service
class GetReviewServiceImpl : GetReviewService {
    @Autowired
    lateinit var reviewRepository: ReviewRepository

    override fun getReviewWith(product: ProductId, page: Pageable): List<GetReviewDTO> {
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
        val product = productRepository.findById(review.product.id).get()

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
}