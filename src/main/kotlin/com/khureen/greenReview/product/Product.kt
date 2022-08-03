package com.khureen.greenReview.product

import com.khureen.greenReview.review.Review
import javax.persistence.*

@Entity
class Product(
    @Id
    @Column(name = "product_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO) //기본 키에 자동으로 매핑
    var id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var vendor : String,

    @Column(nullable = false)
    var price : Int,

    @Column(nullable = false)
    var deliveryFee : Int,

    @ElementCollection
    @Column(nullable = false)
    var originalUrl : MutableList<String>,

    @Column(nullable = false)
    var thumbnailUrl : String,

    @OneToMany(mappedBy = "product")
    var reviews: MutableList<Review>
)