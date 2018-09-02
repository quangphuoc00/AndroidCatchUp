package com.peterdang.androidcatchup.features.login

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Authenticator
@Inject constructor() {
    //login to check login
    fun userLoggedIn() = true
}
