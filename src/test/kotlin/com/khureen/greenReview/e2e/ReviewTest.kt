package com.khureen.greenReview.e2e

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ReviewTest {

    @Test
    fun reviewAddTest() {
        // given
        val addProduct_params = mapOf<String, Any>(
            "name" to "name",
            "vendor" to "vendor",
            "price" to 3000,
            "deliveryFee" to 300,
            "picUrl" to listOf("pic_URl"),
            "registeredDate" to "2019-05-30T08:36:47.966274Z",
            "thumbnailUrl" to "thumb_URL",
            "originalUrl" to "original_URL",
            "detailpicUrl" to listOf("detail")
        )

        val addReview_params = mapOf<String, Any>(
            "productId" to 1,
            "nickname" to "nickname",
            "content" to "content",
            "rate" to 0.1,
            "checkTypes" to arrayOf(2)
        )

        RestAssured.given().log().all()
            .body(addProduct_params)
            .contentType(ContentType.JSON)
            .`when`()
            .post("/product/add")


        // when
        val response = RestAssured.given().log().all()
            .`when`()
            .body(addReview_params)
            .contentType(ContentType.JSON)
            .post("/review/write")
            .then().log().all()
            .extract()

        // then

        Assertions.assertEquals(200, response.statusCode())
        Assertions.assertEquals(2, response.body().jsonPath().get("id"))
    }

    @Test
    fun reviewListTest() {
        // given
        val addProduct_params = mapOf<String, Any>(
            "name" to "name",
            "vendor" to "vendor",
            "price" to 3000,
            "deliveryFee" to 300,
            "picUrl" to listOf("pic_URl"),
            "registeredDate" to "2019-05-30T08:36:47.966274Z",
            "thumbnailUrl" to "thumb_URL",
            "originalUrl" to "original_URL",
            "detailpicUrl" to listOf("detail")
        )

        val addReview_params = mapOf<String, Any>(
            "productId" to 1,
            "nickname" to "nickname",
            "content" to "content",
            "rate" to 0.1,
            "checkTypes" to arrayOf(2)
        )

        RestAssured.given().log().all()
            .body(addProduct_params)
            .contentType(ContentType.JSON)
            .`when`()
            .post("/product/add")


        RestAssured.given().log().all()
            .`when`()
            .body(addReview_params)
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
        Assertions.assertEquals("content", response.body().jsonPath().getList<LinkedHashMap<String, Any>>("$")[0]["content"])
        Assertions.assertEquals("nickname", response.body().jsonPath().getList<LinkedHashMap<String, Any>>("$")[0]["nickname"])
        Assertions.assertEquals(0.1f, response.body().jsonPath().getList<LinkedHashMap<String, Any>>("$")[0]["rate"])
    }
}
