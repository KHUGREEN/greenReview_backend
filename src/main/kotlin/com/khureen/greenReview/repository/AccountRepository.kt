package com.khureen.greenReview.repository

import com.khureen.greenReview.repository.dto.Account
import org.springframework.data.repository.Repository


interface AccountRepository  : Repository<Account, Long> {

}