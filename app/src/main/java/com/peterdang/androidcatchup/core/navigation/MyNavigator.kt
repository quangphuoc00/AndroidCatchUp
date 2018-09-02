package com.peterdang.androidcatchup.core.navigation

import android.view.View
import androidx.navigation.Navigation
import com.peterdang.androidcatchup.R
import com.peterdang.androidcatchup.features.login.Authenticator
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MyNavigator
@Inject constructor(private val authenticator: Authenticator) {


    private fun showLogin(fragment: View) {
//        val navController = Navigation.findNavController(fragment)
//        navController.navigate(R.id.event_details_fragment)
        //TODO add navigate to Login Page
    }

    fun showMain(currentFragment: View) {
        if (authenticator.userLoggedIn())
            showRoom(currentFragment)
        else
            showLogin(currentFragment)
    }

    fun showFragment(currentFragment: View, idFragment: Int) {
        val navController = Navigation.findNavController(currentFragment)
        navController.navigate(idFragment)
    }

    fun showRoom(currentFragment: View) {
        val navController = Navigation.findNavController(currentFragment)
        navController.navigate(R.id.roomFragment)
    }

    fun showBlurFrag(currentFragment: View) {
        val navController = Navigation.findNavController(currentFragment)
        navController.navigate(R.id.blurFragment)
    }
}

