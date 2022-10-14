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

package com.khureen.greenReview.model

enum class HttpStatusCode {
    REQUEST_ERROR,
    NOT_FOUND,
    INTERNAL_ERROR
}

open class ApiException(message: String?, val statusCode: HttpStatusCode) : Exception(message)

class ProductIdNotFoundException(id: Long) : ApiException("can't found product id: $id", HttpStatusCode.NOT_FOUND)