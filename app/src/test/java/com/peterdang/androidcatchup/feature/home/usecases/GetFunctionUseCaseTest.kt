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
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either exprcompileess or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.peterdang.androidcatchup.feature.home.usecases

import com.peterdang.androidcatchup.UnitTest
import com.peterdang.androidcatchup.core.interactor.UseCase
import com.peterdang.androidcatchup.core.utils.Either
import com.peterdang.androidcatchup.features.home.data.FunctionRepository
import com.peterdang.androidcatchup.features.home.models.FunctionModel
import com.peterdang.androidcatchup.features.home.usecases.GetFunctionUsecase
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

class GetFunctionUseCaseTest : UnitTest() {
    private lateinit var getFunctionUsecase: GetFunctionUsecase

    @Mock
    private lateinit var functionRepository: FunctionRepository

    @Before
    fun setUp() {
        getFunctionUsecase = GetFunctionUsecase(functionRepository)
        given(functionRepository.functions()).willReturn(
                Either.SuccessResult(
                        listOf(
                                FunctionModel(0,
                                        "functionName",
                                        "techName",
                                        "functionCode"))))
    }


    @Test
    fun `should get data from repository`() {
        runBlocking { getFunctionUsecase.run(UseCase.None()) }

        //verify call to functionRepository is made or not with same arguments
        verify(functionRepository).functions()
        verifyNoMoreInteractions(functionRepository)
    }
}