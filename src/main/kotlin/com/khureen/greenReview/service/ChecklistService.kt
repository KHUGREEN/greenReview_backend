package com.khureen.greenReview.service

import org.springframework.stereotype.Service

@Service
class ChecklistService {
    companion object {
        val notSufficientEvidence = ChecklistElement(2, "증거 불충분")

        val ambiguousStatement = ChecklistElement(3, "애매모호한 주장")

        val lieStatement = ChecklistElement(5, "거짓말")

        val inappropriateCertification = ChecklistElement(7, "부적절한 인증 라벨")

        val all = listOf(notSufficientEvidence, ambiguousStatement, lieStatement, inappropriateCertification)
    }
}

data class ChecklistElement constructor(
    val id : Int,
    val name : String
)