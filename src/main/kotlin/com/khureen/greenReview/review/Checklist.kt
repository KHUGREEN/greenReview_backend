package com.khureen.greenReview.review

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
    var hidingSideEffects : Boolean,

    @Column
    var notSufficientEvidence : Boolean,

    @Column
    var ambiguousStatement: Boolean,

    @Column
    var notRelatedStatement : Boolean,

    @Column
    var lieStatement : Boolean,

    @Column
    var justifyingHarmingProduct : Boolean,

    @Column
    var inappropriateCertification : Boolean
)