package com.khureen.greenReview.model

enum class HttpStatusCode {
    REQUEST_ERROR,
    NOT_FOUND,
    INTERNAL_ERROR
}

open class ApiException(message: String?, val statusCode: HttpStatusCode) : Exception(message)

class ProductIdNotFoundException(id: Long) : ApiException("can't found product id: $id", HttpStatusCode.NOT_FOUND)