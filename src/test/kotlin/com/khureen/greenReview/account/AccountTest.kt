package com.khureen.greenReview.account

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional


@DataJpaTest
class AccountTest {

    @PersistenceContext
    lateinit var em: EntityManager

    @Test
    fun ctor_PersistenceTest() {
        val account = Account(name = "abc", imageUrl = "unused")

        em.persist(account)
        em.flush()

        assertEquals(em.find(Account::class.java, account.id), account)
    }
}