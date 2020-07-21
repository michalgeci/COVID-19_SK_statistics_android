package sk.ferinaf.covidskstats.util

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*
import sk.ferinaf.covidskstats.R
import sk.ferinaf.covidskstats.services.networking.models.ChartData
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

fun Activity.showSimpleDialog(title: String, message: String) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title).setMessage(message)
    builder.setPositiveButton("OK", null)
    val dialog = builder.create()
    dialog.show()
}

fun Activity.showDayDetailDialog(data: ChartData) {
    val message = "%s\n\nTestovaných: %d\nPozitívnych: %d\nAktívne prípady: %d\n\nSPOLU\nNakazených: %d\nVyliečených: %d\nÚmrtia: %d"
    val formattedMessage = message.format(data.day, data.testedDaily, data.infectedDaily, data.active, data.infected, data.recovered, data.deaths)
    showSimpleDialog(data.date.replace("-", ". "), formattedMessage)
}
