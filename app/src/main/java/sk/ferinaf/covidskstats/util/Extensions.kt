package sk.ferinaf.covidskstats.util

import android.animation.Animator
import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.Fragment
import sk.ferinaf.covidskstats.R
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun Long.timestampToDateString(): String {
    val sdf = SimpleDateFormat("dd. MM. yyyy")
    return sdf.format(Date(this * 1000))
}

fun Fragment.showLoader() {
    val loadingOverlay = view?.findViewById<View>(R.id.loading_overlay)
    loadingOverlay?.visibility = View.VISIBLE
    loadingOverlay?.alpha = 0f
    loadingOverlay?.animate()?.setDuration(200)?.alpha(0.4f)?.start()
}

fun Fragment.hideLoader() {
    val loadingOverlay = view?.findViewById<View>(R.id.loading_overlay)
    loadingOverlay?.animate()
        ?.setDuration(200)
        ?.alpha(0f)
        ?.setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                loadingOverlay.visibility = View.GONE
            }
        })
        ?.start()
}