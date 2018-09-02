package com.peterdang.androidcatchup.core.ui

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.peterdang.androidcatchup.R

/**
 * Move to the right actionbar
 *
 * Dependency: Toolbar
 * Child: Material Button
 *
 * default pivot = center
 *
 * Credit at: http://saulmm.github.io/mastering-coordinator
 */
class MoveToToolbarBehavior(private val context: Context, attributeSet: AttributeSet)
    : CoordinatorLayout.Behavior<MaterialButton>(context, attributeSet) {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun layoutDependsOn(parent: CoordinatorLayout, child: MaterialButton, dependency: View): Boolean {
        return dependency is Toolbar
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: MaterialButton, dependency: View): Boolean {
        child.pivotY = dependency.pivotY
//        child.pivotY = dependency.pivotY
//        child.right = context.resources.getDimension(R.dimen.s_padding).toInt()

        child.setBackgroundColor(ContextCompat.getColor(context, R.color.black_button))
        child.setTextColor(ContextCompat.getColor(context, android.R.color.white))
        return true
    }
}