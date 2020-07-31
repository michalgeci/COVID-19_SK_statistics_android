package sk.ferinaf.covidskstats.services.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DeviceBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals("android.intent.action.BOOT_COMPLETED")) {
            context?.let {
                NotificationHelper.setAlarm(it)
            }
        }
    }

}