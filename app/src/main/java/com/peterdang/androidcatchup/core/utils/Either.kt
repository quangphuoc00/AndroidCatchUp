package com.peterdang.androidcatchup.core.utils

/**
 * Represents a value of one of two possible types (a disjoint union).
 * Instances of [Either] are either an instance of [FailResult] or [SuccessResult].
 *
 *
 * @see FailResult
 * @see SuccessResult
 * Credits to Alex Hart -> https://proandroiddev.com/kotlins-nothing-type-946de7d464fb
 *
 */
sealed class Either<out F, out S> {

    data class FailResult<out L>(val a: L) : Either<L, Nothing>()

    data class SuccessResult<out R>(val b: R) : Either<Nothing, R>()

    val isSuccess get() = this is SuccessResult<S>
    val isFail get() = this is FailResult<F>

    fun <L> fail(a: L) = Either.FailResult(a)
    fun <R> success(b: R) = Either.SuccessResult(b)

    fun either(fail: (F) -> Any, response: (S) -> Any): Any =
            when (this) {
                is FailResult -> fail(a)
                is SuccessResult -> response(b)
            }
}

// Credits to Alex Hart -> https://proandroiddev.com/kotlins-nothing-type-946de7d464fb
// Composes 2 functions
fun <A, B, C> ((A) -> B).c(f: (B) -> C): (A) -> C = {
    f(this(it))
}

fun <T, L, R> Either<L, R>.flatMap(fn: (R) -> Either<L, T>): Either<L, T> =
        when (this) {
            is Either.FailResult -> Either.FailResult(a)
            is Either.SuccessResult -> fn(b)
        }

fun <T, L, R> Either<L, R>.map(fn: (R) -> (T)): Either<L, T> = this.flatMap(fn.c(::success))
