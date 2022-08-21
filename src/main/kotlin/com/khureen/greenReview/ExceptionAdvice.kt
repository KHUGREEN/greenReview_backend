package com.khureen.greenReview

import com.khureen.greenReview.model.ApiException
import com.khureen.greenReview.model.HttpStatusCode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class ExceptionAdvice {
    @ExceptionHandler(Exception::class)
    protected fun handleAllException(ex: Exception): ResponseEntity<*>? {
        return ResponseEntity<Any?>(
            ExceptionResponse(ex.message ?: "no message provided", ex.stackTraceToString()),
            if (ex is ApiException) {
                statusCodeMapping(ex.statusCode)
            } else {
                HttpStatus.INTERNAL_SERVER_ERROR
            }
        )
    }

    private fun statusCodeMapping(status: HttpStatusCode): HttpStatus {
        return when (status) {
            HttpStatusCode.REQUEST_ERROR -> HttpStatus.BAD_REQUEST
            HttpStatusCode.NOT_FOUND -> HttpStatus.NOT_FOUND
            HttpStatusCode.INTERNAL_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR
        }
    }
}

data class ExceptionResponse constructor(
    val message: String,
    val stacktrace: String
)