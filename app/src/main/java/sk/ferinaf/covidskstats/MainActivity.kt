package sk.ferinaf.covidskstats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)
        bottom_navigation.setupWithNavController(navController)

//        bottom_navigation?.setOnNavigationItemReselectedListener { item ->
//            when (item.itemId) {
//                R.id.action_home -> {
//
//                }
//                R.id.action_districts -> {
//
//                }
//                R.id.action_history -> {
//
//                }
//            }
//        }

//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, MainFragment.newInstance())
//                    .commitNow()
//        }
    }
}