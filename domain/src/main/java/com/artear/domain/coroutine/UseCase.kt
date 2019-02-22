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

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers


abstract class UseCase<Param, Return> {

    private var deferred: Deferred<Return>? = null

    operator fun invoke(receiver: SimpleReceiver<Return>) = execute(null, receiver)

    operator fun invoke(param: Param, receiver: SimpleReceiver<Return>) = execute(param, receiver)

    protected abstract suspend fun execute(param: Param?): Return

    //Not use default null param just for clean invoke call
    private fun execute(param: Param?, receiver: SimpleReceiver<Return>) {
        deferred?.cancel()
        deferred = async(Dispatchers.IO) { execute(param) }
        launch(Dispatchers.Main) {
            try {
                val result = deferred!!.await()
                receiver.onSuccess(result)
            } catch (ex: Exception) {
                receiver.onError(ex)
            }
        }
    }

    fun dispose() {
        deferred?.cancel()
    }


}