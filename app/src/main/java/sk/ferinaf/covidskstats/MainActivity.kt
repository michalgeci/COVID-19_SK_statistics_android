package sk.ferinaf.covidskstats

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import sk.ferinaf.covidskstats.services.dataservice.DataService
import sk.ferinaf.covidskstats.services.notifications.NotificationHelper
import sk.ferinaf.covidskstats.util.*
import java.util.*

class MainActivity : AppCompatActivity() {

    var states: MutableMap<String, Bundle> = mutableMapOf()

    private val dataService by lazy { DataService.getInstance(application) }

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

        isCurrent()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.refresh_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refresh_item -> {
                showLoader()
                dataService.fetchData { hideLoader() }
                this.checkCurrent {  }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun isCurrent() {
        val sp = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
        val lastChecked = sp.getString("lastChecked", "2000-01-01")
        val today = Date().getNowInFormat()

        if (lastChecked != today) {
            this.checkCurrent { _ ->
                sp.edit().putString("lastChecked", today).apply()
            }
        }

    }
}
