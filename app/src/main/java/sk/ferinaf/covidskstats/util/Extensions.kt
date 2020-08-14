package sk.ferinaf.covidskstats.util

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import sk.ferinaf.covidskstats.BuildConfig
import sk.ferinaf.covidskstats.R
import sk.ferinaf.covidskstats.services.networking.DriveRestApi
import sk.ferinaf.covidskstats.services.networking.models.ChartData
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun Long.timestampToDateString(): String {
    val sdf = SimpleDateFormat("dd. MM. yyyy")
    return sdf.format(Date(this * 1000))
}

@SuppressLint("SimpleDateFormat")
fun Date.getNowInFormat(format: String = "yyyy-MM-dd"): String {
    val sdf = SimpleDateFormat(format)
    return sdf.format(Date())
}

fun Activity.showLoader() {
    val loadingOverlay = findViewById<View>(R.id.loading_overlay)
    loadingOverlay?.visibility = View.VISIBLE
    loadingOverlay?.alpha = 0f
    loadingOverlay?.animate()?.setDuration(200)?.alpha(0.4f)?.start()
}

fun Activity.hideLoader() {
    val loadingOverlay = findViewById<View>(R.id.loading_overlay)
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
    val title = data.date.replace("-", ". ")

    val builder = AlertDialog.Builder(this)
    builder.setTitle(title).setMessage(formattedMessage)
    builder.setPositiveButton("OK", null)

    builder.setNegativeButton("ZDIEĽAŤ") { _, _ ->
        showShareDialog()
    }

    val dialog = builder.create()
    dialog.show()
}

fun Activity.showShareDialog() {
    val builder = AlertDialog.Builder(this)
    builder.setTitle("Zdieľajte aplikáciu")

    val customView = View.inflate(this, R.layout.view_qr_code, null)
    builder.setView(customView)

    builder.setNegativeButton("Zdieľajte URL") { _, _ ->
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "URL na zdieľanie")
        intent.putExtra(Intent.EXTRA_TEXT, APP_GITHUB_URL)
        startActivity(Intent.createChooser(intent, "Zdieľajte aplikáciu"))
    }

    builder.setPositiveButton("OK", null)
    val dialog = builder.create()
    dialog.show()
}

fun RecyclerView.smoothScrollToTop() {
    val smoothScroller = object : LinearSmoothScroller(context) {
        override fun getVerticalSnapPreference(): Int {
            return SNAP_TO_START
        }

        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
            return 8f / (displayMetrics?.densityDpi ?: 1)
        }
    }
    smoothScroller.targetPosition = 0
    layoutManager?.startSmoothScroll(smoothScroller)
}

fun Context.checkCurrent(isCurrent: (Boolean)->Unit) {
    DriveRestApi.getVersion {
        val current = BuildConfig.VERSION_NAME == it.toString()
        isCurrent(current)

        if (!current) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Nová verzia").setMessage("Môžete si stiahnuť novú verziu aplikácie")

            builder.setPositiveButton("STIAHNUŤ") { _, _ ->
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(APP_GITHUB_URL))
                startActivity(browserIntent)
            }

            builder.setNegativeButton("ZRUŠIŤ", null)

            builder.create().show()
        }
    }
}
