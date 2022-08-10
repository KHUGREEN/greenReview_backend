package com.khureen.greenReview.repository

import com.khureen.greenReview.repository.dto.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.Repository


interface AccountRepository  : JpaRepository<Account, Long> {

}