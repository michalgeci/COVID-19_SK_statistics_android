package sk.ferinaf.covidskstats.ui.main

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonSerializer
import sk.ferinaf.covidskstats.networking.RestApi
import sk.ferinaf.covidskstats.networking.models.CovidData
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val data = MutableLiveData<CovidData>()
    private val sp by lazy { application.getSharedPreferences("data", Context.MODE_PRIVATE) }
    private val gson by lazy { Gson() }

    var current = true

    init {
        val cachedData = loadData()
        if (cachedData == null) {
            fetchData()
        } else {
            checkCurrent(cachedData)
            data.postValue(cachedData)
        }
    }

    fun fetchData() {
        RestApi.getData {
            saveData(it)
            checkCurrent(it)
            data.postValue(it)
        }
    }

    fun getData(): LiveData<CovidData> {
        return data
    }

    private fun checkCurrent(data: CovidData) {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        current = data.cache.startsWith(sdf.format(Date()))
    }

    private fun loadData(): CovidData? {
        val string = sp.getString("data", null)
        string?.let {
            return gson.fromJson(it, CovidData::class.java)
        }
        return null
    }

    private fun saveData(data: CovidData) {
        val edit = sp.edit()
        val dataString = gson.toJson(data)
        edit.putString("data", dataString)
        edit.apply()
    }

}