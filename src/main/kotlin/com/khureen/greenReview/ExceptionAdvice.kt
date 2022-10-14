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