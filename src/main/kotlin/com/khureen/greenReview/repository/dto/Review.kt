package com.khureen.greenReview.repository.dto

import java.util.*
import javax.persistence.*

@Entity
@Table(indexes = [Index(name = "i_review_date", columnList = "registeredDate"), Index(name  = "i_product_id", columnList = "product_id")])
class Review(
    @Id
    @Column(name = "review_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

    @Column(nullable = false)
    var author : String,


    @ManyToOne
    @JoinColumn(name = "product_id")
    var product: Product,

    @Column(nullable = false, length = 2048)
    var content: String,

    @Column(nullable = false)
    var rate: Double,

    @Embedded
    var checklist: Checklist,

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    var registeredDate: Date
)