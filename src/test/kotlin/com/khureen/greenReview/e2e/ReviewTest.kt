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
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ReviewTest {

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
    fun reviewAddTest() {
        // given
        RestAssured.given().log().all()
            .body(addProduct_params)
            .contentType(ContentType.JSON)
            .`when`()
            .post("/product/add")


        // when
        val response = RestAssured.given().log().all()
            .`when`()
            .body(
                mapOf<String, Any>(
                    "productId" to 1,
                    "nickname" to "nickname",
                    "content" to "content",
                    "checkTypes" to arrayOf(2)
                )
            )
            .contentType(ContentType.JSON)
            .post("/review/write")
            .then().log().all()
            .extract()

        // then

        Assertions.assertEquals(200, response.statusCode())
        Assertions.assertEquals(2, response.body().jsonPath().get("id"))
    }

    @Test
    fun reviewCacheInvalidateTest() {
        // given

        RestAssured.given().log().all()
            .body(addProduct_params)
            .contentType(ContentType.JSON)
            .`when`()
            .post("/product/add")

        val firstResponse = RestAssured.given().log().all() // Write to the cache
            .`when`()
            .get("/product/list?q=name&page=0&size=1")
            .then().log().all()
            .extract()

        RestAssured.given().log().all()
            .`when`()
            .body(
                mapOf<String, Any>(
                    "productId" to 1,
                    "nickname" to "nickname",
                    "content" to "content",
                    "checkTypes" to arrayOf(2)
                )
            )
            .contentType(ContentType.JSON)
            .post("/review/write")
            .then().log().all()
            .extract()

        val secondResponse = RestAssured.given().log().all()
            .`when`()
            .get("/product/list?q=name&page=0&size=1")
            .then().log().all()
            .extract()

        assertEquals(0, firstResponse.body().jsonPath().getList<LinkedHashMap<String, Any>>("$")[0]["reviewer"])
        assertEquals(1, secondResponse.body().jsonPath().getList<LinkedHashMap<String, Any>>("$")[0]["reviewer"])

        val firstCheckList: List<LinkedHashMap<String, Any>> = firstResponse.body().jsonPath()
            .getList<LinkedHashMap<String, Any>>("$")[0]["checkList"] as List<LinkedHashMap<String, Any>>

        assert(
            firstCheckList.all {
                it["num"]!!.equals(0)
            }
        )

        val secondChecklist: List<LinkedHashMap<String, Any>> = secondResponse.body().jsonPath()
            .getList<LinkedHashMap<String, Any>>("$")[0]["checkList"] as List<LinkedHashMap<String, Any>>

        assert(
            secondChecklist.map {
                it["num"]
            }.contains(1)
        )

    }

    @Test
    fun reviewListTest() {
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
                    "checkTypes" to arrayOf(2)
                )
            )
            .contentType(ContentType.JSON)
            .post("/review/write")
            .then().log().all()
            .extract()


        // when

        val response = RestAssured.given().log().all()
            .`when`()
            .get("/review/list/1?page=0&size=1")
            .then().log().all()
            .extract()

        // then
        Assertions.assertEquals(200, response.statusCode())
        Assertions.assertEquals(2, response.body().jsonPath().getList<LinkedHashMap<String, Any>>("$")[0]["id"])
        Assertions.assertEquals(
            "content",
            response.body().jsonPath().getList<LinkedHashMap<String, Any>>("$")[0]["content"]
        )
        Assertions.assertEquals(
            "nickname",
            response.body().jsonPath().getList<LinkedHashMap<String, Any>>("$")[0]["nickname"]
        )
        Assertions.assertEquals(
            listOf(2),
            response.body().jsonPath().getList<LinkedHashMap<String, Any>>("$")[0]["checkTypes"]
        )
    }
}
