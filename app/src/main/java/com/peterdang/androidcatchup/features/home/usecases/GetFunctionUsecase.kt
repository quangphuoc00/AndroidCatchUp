package com.peterdang.androidcatchup.features.home.usecases

import com.peterdang.androidcatchup.core.interactor.UseCase
import com.peterdang.androidcatchup.features.home.data.FunctionRepository
import com.peterdang.androidcatchup.features.home.models.FunctionModel
import javax.inject.Inject

class GetFunctionUsecase
@Inject constructor(private val functionRepository: FunctionRepository) :
        UseCase<UseCase.None, List<FunctionModel>>() {
    override suspend fun run(param: None) = functionRepository.functions()
}