/*
 * Copyright 2022 KHUGREEN (https://github.com/KHUGREEN)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.khureen.greenReview.service

import org.springframework.stereotype.Service

@Service
class ChecklistService {
    companion object {
        val notSufficientEvidence = ChecklistElement(1, "증거 불충분")

        val ambiguousStatement = ChecklistElement(2, "애매모호한 주장")

        val lieStatement = ChecklistElement(3, "거짓말")

        val inappropriateCertification = ChecklistElement(4, "부적절한 인증 라벨")

        val all = listOf(notSufficientEvidence, ambiguousStatement, lieStatement, inappropriateCertification)
    }
}

data class ChecklistElement constructor(
    val id : Int,
    val name : String
)