package com.peterdang.androidcatchup.core.interactor

import com.peterdang.androidcatchup.AndroidTest
import com.peterdang.androidcatchup.core.exception.Failure
import com.peterdang.androidcatchup.core.utils.Either
import kotlinx.coroutines.experimental.runBlocking
import org.amshove.kluent.shouldEqual
import org.junit.Test

class UseCaseTest : AndroidTest() {
    private val RESPONSE = "test usecase"

    private val useCase = TestUseCase()

    @Test
    fun `use case should return 'Either' of use case type`() {
        val result = runBlocking { useCase.run("param") }

        result shouldEqual Either.SuccessResult(RESPONSE)
    }

    @Test
    fun `should return correct data when executing use case`() {
        var result: Either<Failure, String>? = null

        val params = "TestParam"
        val onResult = { myResult: Either<Failure, String> -> result = myResult }

        runBlocking { useCase(params, onResult) }

        result shouldEqual Either.SuccessResult(RESPONSE)
    }


    private inner class TestUseCase : UseCase<String, String>() {
        override suspend fun run(params: String) = Either.SuccessResult(RESPONSE)
    }
}