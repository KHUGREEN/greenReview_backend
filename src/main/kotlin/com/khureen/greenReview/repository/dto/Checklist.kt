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

package com.khureen.greenReview.repository.dto

import javax.persistence.Column
import javax.persistence.Embeddable


@Embeddable
class Checklist(
    /*
    * 전체 체크 수 반환할 때 @ElementCollection을 이용하면 성능 문제가 발생할 가능성이 있음
    * 비트마스크 이용할 경우 SQL 내 function 이용 불가
    * 
    * => 하드코딩이 가장 괜찮아 보임
    */

    @Column
    var hidingSideEffects : Int,

    @Column
    var notSufficientEvidence : Int,

    @Column
    var ambiguousStatement: Int,

    @Column
    var notRelatedStatement : Int,

    @Column
    var lieStatement : Int,

    @Column
    var justifyingHarmingProduct : Int,

    @Column
    var inappropriateCertification : Int
)