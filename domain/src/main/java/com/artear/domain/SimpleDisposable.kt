package com.artear.domain

import com.artear.tools.error.NestError
import com.artear.tools.error.NestErrorFactory
import io.reactivex.observers.DisposableObserver

/**
 * A simple observer interface between the domain and the UI.
 *
 * Usually used in ViewModel or Presenter to interact and connect the business logic result
 * and serve this to presentation view.
 *
 * When you activity or fragment will destroyed you must to dispose this observer.
 */
class SimpleDisposable<T>(var onNextDelegate: (data: T) -> Unit,
                          var onErrorDelegate: (e: NestError) -> Unit) : DisposableObserver<T>() {

    override fun onComplete() {}

    override fun onNext(data: T) {
        onNextDelegate(data)
    }

    override fun onError(e: Throwable) {
        val error = NestErrorFactory.create(e)
        onErrorDelegate(error)
    }

}
