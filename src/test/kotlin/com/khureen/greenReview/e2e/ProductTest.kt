/*
 * Copyright 2022 KHUGREEN (https://github.com/KHUGREEN)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.khureen.greenReview.e2e

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ProductTest {

    val addProduct_params = mapOf(
        "name" to "name",
        "vendor" to "vendor",
        "price" to 3000,
        "deliveryFee" to 300,
        "picUrl" to "pic_URl",
        "thumbnailUrl" to "thumb_URL",
        "originalUrl" to "original_URL",
        "detailpicUrl" to listOf("detail1", "detail2")
    )


    @Test
    fun addProductTest() {
        // given

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

        RestAssured.given().log().all()
            .body(addProduct_params)
            .contentType(ContentType.JSON)
            .`when`()
            .post("/product/add")
            .body().prettyPrint()

        // when
        val response = RestAssured.given().log().all()
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

        val checkList: List<LinkedHashMap<String, Any>> = response.body().jsonPath()
            .getList<LinkedHashMap<String, Any>>("$")[0]["checkList"] as List<LinkedHashMap<String, Any>>

        assert(
            checkList.all {
                it["num"]!!.equals(0)
            }
        )

    }

    @Test
    fun getProductListNoneTest() {
        // given

        RestAssured.given().log().all()
            .body(addProduct_params)
            .contentType(ContentType.JSON)
            .`when`()
            .post("/product/add")

        // when
        val response = RestAssured.given().log().all()
            .`when`()
            .get("/product/list?q=nope&page=0&size=1")
            .then().log().all()
            .extract()

        // then
        assertEquals(200, response.statusCode())
        assertEquals(0, response.body().jsonPath().getList<LinkedHashMap<String, Any>>("$").size)
    }

    @Test
    fun getProductChecklistTest() {
        // given

        RestAssured.given().log().all()
            .body(addProduct_params)
            .contentType(ContentType.JSON)
            .`when`()
            .post("/product/add")

        RestAssured.given().log().all()
            .`when`()
            .body(
                mapOf<String, Any>(
                    "productId" to 1,
                    "nickname" to "nickname",
                    "content" to "content",
                    "checkTypes" to arrayOf(1, 2, 3, 4)
                )
            )
            .contentType(ContentType.JSON)
            .post("/review/write")
            .then().log().all()
            .extract()


        // when
        val response = RestAssured.given().log().all()
            .`when`()
            .get("/product/review/1")
            .then().log().all()
            .extract()

        // then
        assertEquals(200, response.statusCode())
        val checkList: List<LinkedHashMap<String, Any>> =
            response.body().jsonPath().get("checkList") as List<LinkedHashMap<String, Any>>

        assert(
            checkList.all {
                it["num"]!!.equals(1)
            }
        )
    }

    @Test
    fun getProductDetailTest() {
        // given

        RestAssured.given().log().all()
            .body(addProduct_params)
            .contentType(ContentType.JSON)
            .`when`()
            .post("/product/add")


        // when
        val response = RestAssured.given().log().all()
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
        assertEquals(listOf("detail1", "detail2"), response.body().jsonPath().get("detailpicUrl"))
        assertEquals("pic_URl", response.body().jsonPath().get("pic_url"))

        val checkList: List<LinkedHashMap<String, Any>> =
            response.body().jsonPath().get("checkList") as List<LinkedHashMap<String, Any>>

        assert(
            checkList.all {
                it["num"]!!.equals(0)
            }
        )
    }


    @Test
    fun getProductSizeTest() {
        // given

        RestAssured.given().log().all()
            .body(addProduct_params)
            .contentType(ContentType.JSON)
            .`when`()
            .post("/product/add")

        RestAssured.given().log().all()
            .body(addProduct_params)
            .contentType(ContentType.JSON)
            .`when`()
            .post("/product/add")

        // when
        val response = RestAssured.given().log().all()
            .`when`()
            .get("/product/size?q=name")
            .then().log().all()
            .extract()

        // then
        assertEquals(200, response.statusCode())
        assertEquals(2, response.body().jsonPath().get("length"))

    }

}