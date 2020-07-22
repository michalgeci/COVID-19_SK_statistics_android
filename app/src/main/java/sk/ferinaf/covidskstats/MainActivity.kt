package sk.ferinaf.covidskstats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
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
    }
}
