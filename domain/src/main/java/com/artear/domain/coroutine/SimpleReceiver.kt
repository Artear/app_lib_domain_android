/*
 * Copyright 2019 Artear S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.artear.domain.coroutine

import com.artear.tools.error.NestError
import com.artear.tools.error.NestErrorFactory

/**
 * A simple interface between the domain and the UI.
 *
 * Usually used in ViewModel or Presenter to interact and connect the business logic result
 * and serve this to presentation view.
 *
 */
class SimpleReceiver<T>(private var onNextDelegate: (data: T) -> Unit,
                        private var onErrorDelegate: (e: NestError) -> Unit) {

    fun onSuccess(data: T) {
        onNextDelegate(data)
    }

    fun onError(e: Throwable) {
        val error = NestErrorFactory.create(e)
        onErrorDelegate(error)
    }

}
