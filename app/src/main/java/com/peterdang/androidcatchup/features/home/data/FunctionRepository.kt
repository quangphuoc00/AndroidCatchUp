package com.peterdang.androidcatchup.features.home.data

import com.peterdang.androidcatchup.core.blueprints.BaseRepository
import com.peterdang.androidcatchup.core.exception.Failure
import com.peterdang.androidcatchup.core.exception.Failure.NetworkConnection
import com.peterdang.androidcatchup.core.utils.Either
import com.peterdang.androidcatchup.core.utils.Either.FailResult
import com.peterdang.androidcatchup.core.utils.NetworkUtil
import com.peterdang.androidcatchup.features.home.models.FunctionModel
import javax.inject.Inject

interface FunctionRepository : BaseRepository {
    fun functions(): Either<Failure, List<FunctionModel>>

    class Network
    @Inject constructor(private val networkUtil: NetworkUtil,
                        private val service: FunctionService) : FunctionRepository {
        override fun functions(): Either<Failure, List<FunctionModel>> {
            return if (networkUtil.isNetworkAvailable())
                request(service.functions(),
                        emptyList())
            else
                FailResult(NetworkConnection())
        }
    }
}