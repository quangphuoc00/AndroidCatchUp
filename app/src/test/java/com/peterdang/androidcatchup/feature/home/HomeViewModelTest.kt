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

package com.peterdang.androidcatchup.feature.home

import com.peterdang.androidcatchup.AndroidTest
import com.peterdang.androidcatchup.R
import com.peterdang.androidcatchup.features.home.HomeViewModel
import com.peterdang.androidcatchup.features.home.models.FunctionModel
import com.peterdang.androidcatchup.features.home.usecases.GetFunctionUsecase
import com.peterdang.androidcatchup.features.home.usecases.NavigateUsecase
import kotlinx.coroutines.experimental.runBlocking
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class HomeViewModelTest : AndroidTest() {
    private lateinit var homeViewModel: HomeViewModel

    @Mock
    private lateinit var getFunctionUsecase: GetFunctionUsecase
    @Mock
    private lateinit var navigationUsecase: NavigateUsecase

    private val obj1 = FunctionModel(0, "function_name", "tech_name", "function_code")

    @Before
    fun setUp() {
        homeViewModel = HomeViewModel(getFunctionUsecase, navigationUsecase)
    }

    @Test
    fun `should navigate to function detail`() {
        val layoutId = R.id.roomFragment
//        given { runBlocking { navigationUsecase.run(eq(any())) } }
//                .willReturn(Either.SuccessResult(layoutId))

        homeViewModel.navFragment.observeForever {
            it shouldBe layoutId
        }

        runBlocking { homeViewModel.onItemClick.onItemClick(obj1) }
    }
}