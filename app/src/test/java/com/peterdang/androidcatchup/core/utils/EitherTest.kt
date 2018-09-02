package com.peterdang.androidcatchup.core.utils

import com.peterdang.androidcatchup.UnitTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqualTo
import org.junit.Test

class EitherTest : UnitTest() {
    @Test
    fun `Either Success should return correct type`() {
        val response = "correct"
        val result = Either.SuccessResult(response)

        result shouldBeInstanceOf Either::class.java
        result.isSuccess shouldBe true
        result.isFail shouldBe false
        result.either({},
                { successResponse ->
                    successResponse shouldBeInstanceOf String::class.java
                    successResponse shouldEqualTo response
                })
    }

    @Test
    fun `Either Fail should return correct type`() {
        val response = "fail"
        val result = Either.FailResult(response)

        result shouldBeInstanceOf Either::class.java
        result.isSuccess shouldBe false
        result.isFail shouldBe true
        result.either({ failResponse ->
            failResponse shouldBeInstanceOf String::class.java
            failResponse shouldEqualTo response
        }, {})
    }
}