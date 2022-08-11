package com.khureen.greenReview.repository.dto

import java.util.*
import javax.persistence.*

@Entity
@Table(indexes = [Index(name = "i_date", columnList = "registeredDate")])
class Review(
    @Id
    @Column(name = "review_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

    //@ManyToOne
    //@JoinColumn(name = "account_id")
    //var author: Account, //id로 참조가 더 나을 수 있음 - https://www.popit.kr/id%EB%A1%9C-%EB%8B%A4%EB%A5%B8-%EC%95%A0%EA%B7%B8%EB%A6%AC%EA%B2%8C%EC%9E%87%EC%9D%84-%EC%B0%B8%EC%A1%B0%ED%95%98%EB%9D%BC/

    @Column(nullable = false)
    var author : String,


    @ManyToOne
    @JoinColumn(name = "product_id")
    var product: Product,

    @Column(nullable = false)
    var content: String,

    @Column(nullable = false)
    var rate: Double,

    @Embedded
    var checklist: Checklist,

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    var registeredDate: Date
)