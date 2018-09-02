/*
 * *
 *  * Copyright (C) 2018 Peter Dang Open Source Project
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.peterdang.androidcatchup.feature.home.data

import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.peterdang.androidcatchup.UnitTest
import com.peterdang.androidcatchup.core.exception.Failure
import com.peterdang.androidcatchup.core.utils.Either
import com.peterdang.androidcatchup.core.utils.NetworkUtil
import com.peterdang.androidcatchup.features.home.data.FunctionRepository
import com.peterdang.androidcatchup.features.home.data.FunctionService
import com.peterdang.androidcatchup.features.home.models.FunctionModel
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import retrofit2.Call
import retrofit2.Response

class FunctionRepositoryTest : UnitTest() {
    private lateinit var networkRepository: FunctionRepository.Network

    @Mock
    private lateinit var networkHandler: NetworkUtil
    @Mock
    private lateinit var service: FunctionService

    @Mock
    private lateinit var functionsCall: Call<List<FunctionModel>>
    @Mock
    private lateinit var functionsResponse: Response<List<FunctionModel>>

    @Before
    fun setUp() {
        networkRepository = FunctionRepository.Network(networkHandler, service)
    }

    @Test
    fun `should return empty list by default`() {
        given { networkHandler.isNetworkAvailable() }.willReturn(true)
        given { functionsResponse.body() }.willReturn(null)
        given { functionsResponse.isSuccessful }.willReturn(true)
        given { functionsCall.execute() }.willReturn(functionsResponse)
        given { service.functions() }.willReturn(functionsCall)

        val functions = networkRepository.functions()

        functions shouldEqual Either.SuccessResult(emptyList<FunctionModel>())
        verify(service).functions()
    }

    @Test
    fun `should get function list from service`() {
        val lstFunction = listOf(FunctionModel(0, "function_name", "tech_name", "function_code"))
        given { networkHandler.isNetworkAvailable() }.willReturn(true)
        given { functionsResponse.body() }.willReturn(lstFunction)
        given { functionsResponse.isSuccessful }.willReturn(true)
        given { functionsCall.execute() }.willReturn(functionsResponse)
        given { service.functions() }.willReturn(functionsCall)

        val functions = networkRepository.functions()

        functions shouldEqual Either.SuccessResult(lstFunction)
        verify(service).functions()
    }

    @Test
    fun `functions service should return network failure when no connection`() {
        given { networkHandler.isNetworkAvailable() }.willReturn(false)

        val functions = networkRepository.functions()

        functions shouldBeInstanceOf Either::class.java
        functions.isFail shouldEqual true
        functions.either({ failure -> failure shouldBeInstanceOf Failure.NetworkConnection::class.java }, {})
        verifyZeroInteractions(service)
    }

    @Test
    fun `functions service should return server error if no successful response`() {
        given { networkHandler.isNetworkAvailable() }.willReturn(true)

        val functions = networkRepository.functions()

        functions shouldBeInstanceOf Either::class.java
        functions.isFail shouldEqual true
        functions.either({ failure -> failure shouldBeInstanceOf Failure.ServerError::class.java }, {})
    }
}