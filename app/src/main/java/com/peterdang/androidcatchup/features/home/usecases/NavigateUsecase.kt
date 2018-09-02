package com.peterdang.androidcatchup.features.home.usecases

import com.peterdang.androidcatchup.R
import com.peterdang.androidcatchup.core.constant.Constants
import com.peterdang.androidcatchup.core.exception.Failure
import com.peterdang.androidcatchup.core.interactor.UseCase
import com.peterdang.androidcatchup.core.utils.Either
import javax.inject.Inject

class NavigateUsecase
@Inject constructor() : UseCase<String, Int>() {
    override suspend fun run(functionCode: String): Either<Failure, Int> {
        val layoutId = when (functionCode) {
            Constants.FunctionCode.ROOM -> R.id.roomFragment
            Constants.FunctionCode.WORKMANAGER -> R.id.blurFragment
            else -> null
        }

        layoutId?.let {
            return Either.SuccessResult(layoutId)
        }
        return Either.FailResult(FunctionNotImp())
    }

    class FunctionNotImp : Failure.FeatureFailure()
}