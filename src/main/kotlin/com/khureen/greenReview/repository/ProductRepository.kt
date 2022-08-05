package com.khureen.greenReview.repository

import com.khureen.greenReview.repository.dto.Product
import com.khureen.greenReview.repository.dto.ProductListElement
import com.khureen.greenReview.repository.dto.QProduct
import com.querydsl.core.types.Projections
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

interface ProductRepositoryCustom {
    fun findProductWith(name : String, page: Pageable) : List<ProductListElement>
    fun getProduct(id : Long) : Product
    fun addProduct(product: Product)
}

@Repository
@Transactional
@Component
@PersistenceContext
class ProductRepositoryCustomImpl : ProductRepositoryCustom, QuerydslRepositorySupport(Product::class.java) {

    override fun findProductWith(name: String, page: Pageable) : List<ProductListElement> {
        val qProduct = QProduct.product

        val result = from(qProduct)
            .where(qProduct.name.contains(name))
            .orderBy(qProduct.registeredDate.desc())
            .offset(page.offset)
            .limit(page.pageSize.toLong())
            .select(Projections.constructor(
                ProductListElement::class.java,
                qProduct.id,
                qProduct.thumbnailUrl,
                qProduct.name,
                qProduct.vendor,
                qProduct.price
            ))
            .fetch()

        return result
    }

    override fun getProduct(id : Long) : Product {
        val qProduct = QProduct.product

        val result = from(qProduct)
            .where(qProduct.id.eq(id))
            .fetchFirst()

        return result
    }

    override fun addProduct(product: Product) {
        entityManager!!.persist(product)
    }
}