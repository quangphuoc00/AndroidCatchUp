package com.peterdang.androidcatchup.features

import android.os.Bundle
import com.peterdang.androidcatchup.R
import com.peterdang.androidcatchup.core.blueprints.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
