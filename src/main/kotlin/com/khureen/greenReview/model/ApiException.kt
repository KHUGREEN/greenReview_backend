package com.khureen.greenReview.model

enum class HttpStatusCode {
    REQUEST_ERROR,
    NOT_FOUND,
    INTERNAL_ERROR
}

class ApiException(message: String?, val statusCode: HttpStatusCode) : Exception(message) {
}