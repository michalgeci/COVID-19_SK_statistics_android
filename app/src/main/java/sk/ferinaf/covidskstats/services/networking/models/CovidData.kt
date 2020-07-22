package sk.ferinaf.covidskstats.services.networking.models

import com.google.gson.annotations.SerializedName

data class CovidData(
    @SerializedName("tested")
    val tested: Int,

    @SerializedName("active")
    val active: Int,

    @SerializedName("infected")
    val infected: Int,

    @SerializedName("recovered")
    val recovered: Int,

    @SerializedName("deaths")
    val deaths: Int,

    @SerializedName("cache")
    val cache: String,

    @SerializedName("updated")
    val updated: String,

    @SerializedName("chart")
    val chart: List<ChartData>,

    @SerializedName("tested_chart")
    val testedChart: List<TestedChartData>,

    @SerializedName("districts")
    val districts: List<DistrictData>
)
