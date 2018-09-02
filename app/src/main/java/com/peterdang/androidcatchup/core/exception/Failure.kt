package com.peterdang.androidcatchup.core.exception

sealed class Failure {
    class NetworkConnection : Failure()
    class ServerError : Failure()

    //NOTE: feature specific failures
    abstract class FeatureFailure : Failure()
}