package com.khureen.greenReview.e2e

import com.khureen.greenReview.controller.GetProductListResponseElement
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
class ProductTest {
    @Test
    fun addProductTest() {
        // given
        val addProduct_params = mapOf<String, Any>(
            "name" to "name",
            "vendor" to "vendor",
            "price" to 3000,
            "deliveryFee" to 300,
            "picUrl" to "pic_URl",
            "thumbnailUrl" to "thumb_URL",
            "originalUrl" to "original_URL",
            "detailpicUrl" to listOf("detail")
        )

        // when
        val response = RestAssured.given().log().all()
            .body(addProduct_params)
            .contentType(ContentType.JSON)
            .`when`()
            .post("/product/add")
            .then().log().all()
            .extract()


        // then

        assertEquals(200, response.statusCode())
        assertEquals(1, response.body().jsonPath().get("id"))
    }

    @Test
    fun getProductListExistsTest() {
        // given
        val addProduct_params = mapOf<String, Any>(
            "name" to "name",
            "vendor" to "vendor",
            "price" to 3000,
            "deliveryFee" to 300,
            "picUrl" to "pic_URl",
            "thumbnailUrl" to "thumb_URL",
            "originalUrl" to "original_URL",
            "detailpicUrl" to listOf("detail")
        )

        RestAssured.given().log().all()
            .body(addProduct_params)
            .contentType(ContentType.JSON)
            .`when`()
            .post("/product/add")
            .body().prettyPrint()

        // when
        val response =RestAssured.given().log().all()
            .`when`()
            .get("/product/list?q=name&page=0&size=1")
            .then().log().all()
            .extract()

        // then
        assertEquals(200, response.statusCode())
        assertEquals("name", response.body().jsonPath().getList<LinkedHashMap<String, Any>>("$")[0]["name"])
        assertEquals("vendor", response.body().jsonPath().getList<LinkedHashMap<String, Any>>("$")[0]["vendor"])
        assertEquals(3000, response.body().jsonPath().getList<LinkedHashMap<String, Any>>("$")[0]["price"])
        assertEquals(0, response.body().jsonPath().getList<LinkedHashMap<String, Any>>("$")[0]["reviewer"])

        val checkList : List<LinkedHashMap<String, Any>> = response.body().jsonPath().getList<LinkedHashMap<String, Any>>("$")[0]["checkList"] as List<LinkedHashMap<String, Any>>

        assert(
            checkList.all {
                it["num"]!!.equals(0)
            }
        )

    }

    @Test
    fun getProductListNoneTest() {
        // given
        val addProduct_params = mapOf<String, Any>(
            "name" to "name",
            "vendor" to "vendor",
            "price" to 3000,
            "deliveryFee" to 300,
            "picUrl" to "pic_URl",
            "thumbnailUrl" to "thumb_URL",
            "originalUrl" to "original_URL",
            "detailpicUrl" to listOf("detail")
        )

        RestAssured.given().log().all()
            .body(addProduct_params)
            .contentType(ContentType.JSON)
            .`when`()
            .post("/product/add")

        // when
        val response =RestAssured.given().log().all()
            .`when`()
            .get("/product/list?q=nope&page=0&size=1")
            .then().log().all()
            .extract()

        // then
        assertEquals(200, response.statusCode())
        assertEquals(0, response.body().jsonPath().getList<LinkedHashMap<String, Any>>("$").size)
    }

    @Test
    fun getProductDetailTest() {
        // given
        val addProduct_params = mapOf<String, Any>(
            "name" to "name",
            "vendor" to "vendor",
            "price" to 3000,
            "deliveryFee" to 300,
            "picUrl" to "pic_URl",
            "thumbnailUrl" to "thumb_URL",
            "originalUrl" to "original_URL",
            "detailpicUrl" to listOf("detail")
        )

        RestAssured.given().log().all()
            .body(addProduct_params)
            .contentType(ContentType.JSON)
            .`when`()
            .post("/product/add")

        // when
        val response =RestAssured.given().log().all()
            .`when`()
            .get("/product/detail/1")
            .then().log().all()
            .extract()

        // then
        assertEquals(200, response.statusCode())
        assertEquals("name", response.body().jsonPath().get("name"))
        assertEquals("vendor", response.body().jsonPath().get("vendor"))
        assertEquals(3000, response.body().jsonPath().get("price"))
        assertEquals(0, response.body().jsonPath().get("reviewer"))
        assertEquals("original_URL", response.body().jsonPath().get("originalURL"))
        assertEquals(300, response.body().jsonPath().get("deliveryFee"))
        assertEquals("pic_URl", response.body().jsonPath().get("pic_url"))
        assertEquals(listOf("detail"), response.body().jsonPath().get("detailpicUrl"))

        val checkList : List<LinkedHashMap<String, Any>> = response.body().jsonPath().get("checkList") as List<LinkedHashMap<String, Any>>

        assert(
            checkList.all {
                it["num"]!!.equals(0)
            }
        )
    }

}