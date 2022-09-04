package com.khureen.greenReview.repository.dto

import java.util.Date
import javax.persistence.*

@Entity
@Table(indexes = [Index(name = "i_product_date", columnList = "registeredDate")])
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
    @Column(nullable = false, length = 2048)
    var picUrl : MutableList<String>,

    @Column(nullable = false)
    var thumbnailUrl : String,

    @OneToMany(mappedBy = "product")
    var reviews: MutableList<Review>,

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    var registeredDate : Date,

    @Column(nullable = false, length = 2048)
    var originalUrl : String,

    @ElementCollection
    @Column(nullable = false, length = 2048)
    var detailpicUrl : MutableList<String>

)