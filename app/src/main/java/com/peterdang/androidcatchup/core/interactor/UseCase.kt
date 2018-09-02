package com.peterdang.androidcatchup.core.interactor

import androidx.databinding.ObservableBoolean
import com.peterdang.androidcatchup.core.exception.Failure
import com.peterdang.androidcatchup.core.utils.Either
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

/**
 * inherit this class for new use case
 *
 *
 * Tech term: 1/ coroutines (a library supporting to control thread in Kotlin, similar with Rx)
 *                . tutorial: https://kotlinlang.org/docs/tutorials/coroutines-basic-jvm.html
 *                . compare with RxJava: https://proandroiddev.com/kotlin-coroutines-vs-rxjava-an-initial-performance-test-68160cfc6723
 *
 *             2/ keyword "operator": marks a function as overloading an operator or implementing a convention
 *
 *             3/ keyword "in" & "out" : marks a type parameter as contravariant & marks a type parameter as covariant
 *             https://kotlinlang.org/docs/reference/keyword-reference.html
 *
 *             4/ suspend: calls to them may suspend a coroutine
 *
 * How to create a new UseCase:
 *  Step 1. Make your own usecase by extend this class with <Type> - the output, <Params> - the input
 *          NOTE: using UseCase.None() for an usecase required no Input
 *  Step 2. Override run method with your logic
 *  @see FunctionUseCase
 */
abstract class UseCase<in Params, out Type> where Type : Any {

    abstract suspend fun run(param: Params): Either<Failure, Type>

    /**
     * Processing in background using thread pool, then Sending the result to UI thread
     */
    operator fun invoke(params: Params,
                        isLoading: ObservableBoolean,
                        onResult: (Either<Failure, Type>) -> Unit = {}) {
        isLoading.set(true)
        val job = async(CommonPool) {
            run(params)
        }

        launch(UI) {
            onResult(job.await())
            isLoading.set(false)
        }
    }

    operator fun invoke(params: Params,
                        onResult: (Either<Failure, Type>) -> Unit = {}) {
        val job = async(CommonPool) {
            run(params)
        }

        launch(UI) {
            onResult(job.await())
        }
    }

    /** For Usecase without params */
    class None
}