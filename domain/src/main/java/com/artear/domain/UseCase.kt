package com.artear.domain

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


abstract class UseCase<PARAM_TYPE, RETURN_TYPE> {

    /**
     * Returns an observable without params
     *
     * @param async true if you want execute in background thread
     * @return A defer Observable
     */
    fun getObservable(async: Boolean): Observable<RETURN_TYPE> {
        return getObservable(null, async)
    }

    /**
     * Returns an observable with params of type param_type. The input in UseCase
     *
     * @param param The input param to make the use case call
     * @return A defer Observable
     */
    fun getObservable(param: PARAM_TYPE): Observable<RETURN_TYPE> {
        return getObservable(param, false)
    }

    /**
     * Returns a defer observable that can execute in background thread.
     *
     * @param param The input param to make the use case call
     * @param async true if you want execute in background thread
     * @return A defer observable
     */
    fun getObservable(param: PARAM_TYPE?, async: Boolean): Observable<RETURN_TYPE> {
        val observable = getDeferObservable(param)
        return if (async) makeAsyncPerformObservable(observable) else observable
    }

    /**
     * Make the observable execute in IO thread from Schedulers and returns the response in
     * main thread.
     *
     * @param observable The observable to change
     * @return The same observable modified
     */
    protected fun makeAsyncPerformObservable(observable: Observable<RETURN_TYPE>): Observable<RETURN_TYPE> {
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Returns an observable that emits another observable for each new observer that subscribe
     *
     * @param param The input param to make the use case call
     * @return A defer observable
     */
    private fun getDeferObservable(param: PARAM_TYPE?): Observable<RETURN_TYPE> {
        return Observable.defer { getObservableFactory(param) }
    }

    /**
     * Returns an observable that execute every interval specified of time
     *
     * @param param The input param to make the use case call
     * @param milliseconds The interval time in milliseconds
     * @return An interval observable
     */
    fun getIntervalObservable(param: PARAM_TYPE,
                              milliseconds: Int): Observable<RETURN_TYPE> {
        return Observable
                .interval(milliseconds.toLong(), TimeUnit.MILLISECONDS)
                .startWith(0L)
                .timeInterval()
                .flatMap { getObservableFactory(param) }
    }

    /**
     * The factory needed to generate an observable.
     *
     * @param param The input param to make the use case call
     * @return The observer
     */
    protected abstract fun getObservableFactory(param: PARAM_TYPE?): Observable<RETURN_TYPE>

}