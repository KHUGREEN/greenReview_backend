package com.khureen.greenReview.model

data class AccountDTO constructor(
    val name : String,

    val imageUrl : String,
)

data class AccountId constructor(val id : Long)

data class GetAccountDTO constructor(
    val id: AccountId,
    val accountDTO: AccountDTO
)