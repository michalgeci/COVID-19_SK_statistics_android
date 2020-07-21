package sk.ferinaf.covidskstats.ui.main

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import sk.ferinaf.covidskstats.services.dataservice.DataService
import sk.ferinaf.covidskstats.services.networking.models.CovidData
import sk.ferinaf.covidskstats.services.networking.models.DistrictData
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val dataService by lazy { DataService.getInstance(application) }
    private val data = MutableLiveData<CovidData>()
    val sp by lazy {  application.getSharedPreferences("settings", Context.MODE_PRIVATE) }

    val promile: Float
    get() {
        return data.value?.chart?.last()?.let { lastDay ->
            (lastDay.infectedDaily.toFloat() / lastDay.testedDaily.toFloat()) * 1000f
        } ?: 0f
    }

    val current: Boolean
    get() {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return data.value?.cache?.startsWith(sdf.format(Date())) ?: false
    }

    init {
        dataService.getData {
            data.postValue(it)
        }
    }

    fun getData(): LiveData<CovidData> {
        return data
    }

    fun fetchData(onFetched: ()->Unit = {}) {
        dataService.fetchData {
            data.postValue(it)
            onFetched()
        }
    }

    fun getFavoriteCity(): DistrictData? {
        val favoriteId = sp.getInt("favoriteDistrict", -1)
        val favorite = data.value?.districts?.firstOrNull {
            it.id == favoriteId
        }

        if (favorite?.title == "Košice I") {
            favorite.title = "Košice"
        } else if (favorite?.title == "Bratislava I") {
            favorite.title = "Bratislava"
        }

        return favorite
    }

}