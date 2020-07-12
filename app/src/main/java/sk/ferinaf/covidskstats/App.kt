package sk.ferinaf.covidskstats

import android.app.Application

class App : Application() {

    // Providing context for whole app
    companion object {
        lateinit var context: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }

}