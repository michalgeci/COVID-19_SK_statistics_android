package sk.ferinaf.covidskstats.services.notifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import sk.ferinaf.covidskstats.MainActivity
import sk.ferinaf.covidskstats.R
import sk.ferinaf.covidskstats.services.dataservice.DataService
import sk.ferinaf.covidskstats.util.SETTINGS
import java.util.*

object NotificationHelper {

    private const val ALREADY_SET = "alreadySet"
    private const val CHANNEL_ID = "notifications19"

    fun showNotificationWithData(context: Context) {
        val dataService = DataService.getInstance(context)
        dataService.fetchData { covidData ->
            val today = covidData.chart.lastOrNull() ?: return@fetchData
            val testedDaily = today.testedDaily
            val infectedDaily = today.infectedDaily
            val content = context
                .getString(R.string.notificationTitle)
                .format(testedDaily, infectedDaily)

            val favorite = dataService.getFavoriteDistrict()
            val district = favorite?.title ?: "O.o"
            val infected = favorite?.amount?.infectedDelta ?: 0
            val newCasesString = when {
                infected == 0 -> context.getString(R.string.newCase5_)
                infected == 1 -> context.getString(R.string.newCase1)
                infected in 2..4 -> context.getString(R.string.newCase2_4)
                infected > 4 -> context.getString(R.string.newCase5_)
                else -> "¯\\_(ツ)_/¯"
            }
            val detail = context.getString(R.string.notificationContent)
                .format(district, infected, newCasesString)

            showNotification(context, "COVID-19 SK: ${today.day}", content, detail)
        }
    }

    fun showNotification(context: Context, title: String, content: String, detail: String? = null) {
        createNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val builder = if (detail == null) {
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_app_logo)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        } else {
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_app_logo)
                .setContentTitle(title)
                .setContentText("$content $detail")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(NotificationCompat.BigTextStyle()
                    .bigText("$content\n$detail"))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        }

        with(NotificationManagerCompat.from(context)) {
            notify(19, builder.build())
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notificationName)
            val descriptionText = context.getString(R.string.notificationDescription)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }



    fun setAlarm(context: Context) {
        val sp = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
        val alreadySet = sp.getBoolean(ALREADY_SET, false)

        if (!alreadySet) {
            val alarmIntent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)

            val alarmManager = (context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager) ?: return

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.set(Calendar.HOUR_OF_DAY, 10)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 1)

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

            val spEdit = sp.edit()
            spEdit.putBoolean(ALREADY_SET, true)
            spEdit.apply()
        }
    }

    fun cancelAlarm(context: Context) {
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)
        val alarmManager = (context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager) ?: return
        alarmManager.cancel(pendingIntent)

        val sp = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
        val spEdit = sp.edit()
        spEdit.putBoolean(ALREADY_SET, false)
        spEdit.apply()
    }
}