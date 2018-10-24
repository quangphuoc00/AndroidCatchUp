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

package com.peterdang.androidcatchup.features.home

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.MutableLiveData
import com.peterdang.androidcatchup.BR
import com.peterdang.androidcatchup.R
import com.peterdang.androidcatchup.core.blueprints.BaseViewModel
import com.peterdang.androidcatchup.core.blueprints.ItemRecyclerViewClick
import com.peterdang.androidcatchup.core.interactor.UseCase
import com.peterdang.androidcatchup.features.home.models.FunctionModel
import com.peterdang.androidcatchup.features.home.usecases.GetFunctionUsecase
import com.peterdang.androidcatchup.features.home.usecases.NavigateUsecase
import me.tatarka.bindingcollectionadapter2.ItemBinding
import javax.inject.Inject

class HomeViewModel
@Inject constructor(private val getFunctionUsecase: GetFunctionUsecase,
                    private val navigateUsecase: NavigateUsecase) : BaseViewModel() {
    //    var datas: MutableLiveData<List<FunctionModel>> = MutableLiveData()
    var navFragment: MutableLiveData<Int> = MutableLiveData()
    var functions: ObservableList<FunctionModel> = ObservableArrayList<FunctionModel>()

    val onItemClick = object : ItemRecyclerViewClick<FunctionModel> {
        override fun onItemClick(item: FunctionModel) {
            navigateUsecase(item.functionCode) { it ->
                it.either(::handleFailure, ::handleNavigate)
            }
        }
    }

    val itemBinding: ItemBinding<FunctionModel> = ItemBinding.of<FunctionModel>(BR.row, R.layout.item_function)
            .bindExtra(BR.listener, onItemClick)

    private fun handleNavigate(layoutId: Int) {
        navFragment.value = layoutId
    }

    fun loadListFunction() {
        getFunctionUsecase(UseCase.None(), isLoading) {
            it.either(::handleFailure, ::handleLoadSuccess)
        }
    }

    private fun handleLoadSuccess(listFunctions: List<FunctionModel>) {
        this.functions.addAll(listFunctions)
    }
}

