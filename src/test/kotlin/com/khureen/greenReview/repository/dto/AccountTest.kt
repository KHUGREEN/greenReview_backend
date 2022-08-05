package com.khureen.greenReview.repository.dto

import com.khureen.greenReview.TestUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@DataJpaTest
class AccountTest {

    @PersistenceContext
    lateinit var em: EntityManager

    @Test
    fun ctor_PersistenceTest() {
        // given
        val account = TestUtil.getAccount()

        // when
        em.persist(account)
        em.flush()

        // then
        assertEquals(em.find(Account::class.java, account.id), account)
    }
}