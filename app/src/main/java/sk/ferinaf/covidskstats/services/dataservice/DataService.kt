package sk.ferinaf.covidskstats.services.dataservice

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import sk.ferinaf.covidskstats.services.networking.RestApi
import sk.ferinaf.covidskstats.services.networking.models.CovidData

class DataService {

    companion object {
        @Volatile private var INSTANCE: DataService? = null

        const val DATA = "data"

        fun getInstance(context: Context): DataService {
            if (INSTANCE == null) {
                INSTANCE = DataService()
            }

            INSTANCE?.mContext = context
            INSTANCE?.sp = context.getSharedPreferences(DATA, Context.MODE_PRIVATE)

            if (INSTANCE?.data == null) {
                INSTANCE?.data = INSTANCE?.loadData()
                if (INSTANCE?.data == null) {
                    INSTANCE?.fetchData()
                }
            }

            return INSTANCE!!
        }
    }

    var mContext: Context? = null
    var sp: SharedPreferences? = null
    private val gson by lazy { Gson() }
    private var data: CovidData? = null

    fun getData(onLoaded: (CovidData)->Unit) {
        if (data != null) {
            onLoaded(data!!)
        } else {
            fetchData {
                onLoaded(it)
            }
        }
    }

    fun fetchData(onFetched: ((CovidData)->Unit)? = null) {
        RestApi.getData {
            saveData(it)
            data = it
            onFetched?.invoke(it)
        }
    }

    private fun loadData(): CovidData? {
        val string = sp?.getString(DATA, null)
        string?.let {
            return gson.fromJson(it, CovidData::class.java)
        }
        return null
    }

    private fun saveData(data: CovidData) {
        val edit = sp?.edit()
        val dataString = gson.toJson(data)
        edit?.putString(DATA, dataString)
        edit?.apply()
    }

}
