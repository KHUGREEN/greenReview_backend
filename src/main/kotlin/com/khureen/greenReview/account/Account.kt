package com.khureen.greenReview.account

import javax.persistence.*

@Entity
class Account(
    @Id
    @Column(name = "account_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

    @Column(name = "name")
    var name : String,

    @Column(name = "profile_image")
    var imageUrl : String
)