package sk.ferinaf.covidskstats

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import sk.ferinaf.covidskstats.services.dataservice.DataService
import sk.ferinaf.covidskstats.services.notifications.AlarmReceiver
import sk.ferinaf.covidskstats.services.notifications.NotificationHelper
import sk.ferinaf.covidskstats.util.SavesRecyclerViewState

class MainActivity : AppCompatActivity() {

    var states: MutableMap<String, Bundle> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)
        bottom_navigation.setupWithNavController(navController)
        bottom_navigation.setOnNavigationItemReselectedListener {
            if (it.itemId != bottom_navigation.selectedItemId) {
                NavigationUI.onNavDestinationSelected(it, navController)
            } else {
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                navHostFragment?.childFragmentManager?.fragments?.firstOrNull()?.let { fragment ->
                    (fragment as? SavesRecyclerViewState)?.wasReselected()
                }
            }
        }

        NotificationHelper.setAlarm(this)
    }
}
