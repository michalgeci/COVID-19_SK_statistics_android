package sk.ferinaf.covidskstats.ui.main

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import sk.ferinaf.covidskstats.services.dataservice.DataService
import sk.ferinaf.covidskstats.services.networking.models.CovidData
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val dataService by lazy { DataService.getInstance(application) }
    private val data = MutableLiveData<CovidData>()

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

    fun fetchData() {
        dataService.fetchData {
            data.postValue(it)
        }
    }

}