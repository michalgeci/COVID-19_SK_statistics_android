package sk.ferinaf.covidskstats.services.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        // Show notification
        context ?: return
        NotificationHelper.showNotificationWithData(context)
    }

}