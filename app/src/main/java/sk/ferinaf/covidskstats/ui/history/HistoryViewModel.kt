package sk.ferinaf.covidskstats.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import sk.ferinaf.covidskstats.services.dataservice.DataService
import sk.ferinaf.covidskstats.services.networking.models.ChartData

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val dataService by lazy { DataService.getInstance(application) }
    private val historyData = MutableLiveData<List<HistoryItemModel>>()
    private var chartData: List<ChartData> = listOf()

    init {
        dataService.getData { covidData ->
            chartData = covidData.chart.reversed()

            val data = chartData.map { item ->
                HistoryItemModel(
                    item.date.replace("-", ". "),
                    item.day,
                    item.testedDaily,
                    item.infectedDaily,
                    (item.infectedDaily.toFloat() / item.testedDaily.toFloat()) * 1000f
                )
            }

            historyData.postValue(data)
        }
    }

    fun getData(): LiveData<List<HistoryItemModel>> {
        return historyData
    }

    fun getDetail(position: Int): ChartData {
        return chartData[position]
    }
}