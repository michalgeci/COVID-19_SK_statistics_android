package sk.ferinaf.covidskstats.ui.districts

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import sk.ferinaf.covidskstats.services.dataservice.DataService
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class DistrictsViewModel(application: Application) : AndroidViewModel(application) {

    private val dataService by lazy { DataService.getInstance(application) }
    private val districtData = MutableLiveData<List<DistrictsItemModel>>()
    val sp by lazy {  application.getSharedPreferences("settings", Context.MODE_PRIVATE) }

    private var baseData: List<DistrictsItemModel> = listOf()

    private var mShowFullList = false
    var showFullList: Boolean
        get() = mShowFullList
        set(value) {
            mShowFullList = value
            // TODO: filter data
        }

    init {
        val sdf = SimpleDateFormat("dd. MM. yyyy")
        val favoriteDistrictId = sp.getInt("favoriteDistrict", -1)

        dataService.getData { covidData ->
            val mappedDistricts = covidData.districts.map { districtData ->
                DistrictsItemModel(
                    districtData.id,
                    if (districtData.title == "Košice I") {
                        "Košice"
                    } else if (districtData.title == "Košice II"
                        || districtData.title == "Košice III"
                        || districtData.title == "Košice IV"
                        || districtData.title == "Bratislava II"
                        || districtData.title == "Bratislava III"
                        || districtData.title == "Bratislava IV"
                        || districtData.title == "Bratislava V"
                    ) {
                        "xxx"
                    } else if (districtData.title == "Bratislava I") {
                        "Bratislava"
                    } else {
                        districtData.title
                    },
                    districtData.amount.infectedDelta,
                    sdf.format(Date(districtData.lastOccurrenceTimestamp * 1000)),
                    districtData.amount.infected,
                    favoriteDistrictId == districtData.id
                )
            }

            baseData = mappedDistricts.filter { districtsItemModel ->
                districtsItemModel.name != "xxx"
            }

            districtData.postValue(baseData)
        }
    }

    fun toggleFavorite(id: Int) {
        val favoriteDistrictId = sp.getInt("favoriteDistrict", -1)
        val edit = sp.edit()
        var favoriteId = -1
        if (favoriteDistrictId != id) {
            favoriteId = id

        }
        edit.putInt("favoriteDistrict", favoriteId)
        edit.apply()

        baseData.forEach { item ->
            item.favorite = (item.id == favoriteId)
        }

        if (sp.getBoolean("onlyNew", false)) {
            val filtered = baseData.filter { item ->
                item.newInfected != 0
            }
            districtData.postValue(filtered)
        } else {
            districtData.postValue(baseData)
        }
    }

    fun toggleRecent(onlyNew: Boolean) {
        if (onlyNew) {
            val filtered = baseData.filter { item ->
                item.newInfected != 0
            }
            districtData.postValue(filtered)
        } else {
            districtData.postValue(baseData)
        }
    }

    fun getData(): LiveData<List<DistrictsItemModel>> {
        return districtData
    }

}