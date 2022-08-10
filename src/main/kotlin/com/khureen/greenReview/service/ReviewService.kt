package com.khureen.greenReview.service

import com.khureen.greenReview.model.AddReviewDTO
import com.khureen.greenReview.model.ChecklistDTO
import com.khureen.greenReview.model.ProductId
import com.khureen.greenReview.repository.AccountRepository
import com.khureen.greenReview.repository.ProductRepository
import com.khureen.greenReview.repository.ReviewRepository
import com.khureen.greenReview.repository.dto.Checklist
import com.khureen.greenReview.repository.dto.Review
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service



fun ChecklistDTO.toEntity() : Checklist {
    return Checklist(
        hidingSideEffects = hidingSideEffects,
        notSufficientEvidence = notSufficientEvidence,
        ambiguousStatement = ambiguousStatement,
        notRelatedStatement = notRelatedStatement,
        lieStatement = lieStatement,
        justifyingHarmingProduct = justifyingHarmingProduct,
        inappropriateCertification = inappropriateCertification
    )
}

@Service
class GetReviewServiceImpl {
    @Autowired
    lateinit var reviewRepository: ReviewRepository

    @Autowired
    lateinit var accountRepository: AccountRepository

    fun getReviewWith(product: ProductId, page: Pageable) : List<Review> {
        return reviewRepository.findByProductId(product.id, page)
    }
}


@Service
class AddReviewService {
    @Autowired
    lateinit var reviewRepository: ReviewRepository

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var productRepository: ProductRepository


    fun addReview(review: AddReviewDTO) {
        val account = accountRepository.findById(review.account.id).get()
        val product = productRepository.findById(review.product.id).get()

        val reviewEntity = Review(
            author = account,
            product = product,
            content = review.review.content,
            checklist = review.review.checklist.toEntity(),
            rate = review.review.rate,
            registeredDate = review.review.registeredDate
        )

        reviewRepository.save(reviewEntity)
        product.reviews.add(reviewEntity)
    }
}