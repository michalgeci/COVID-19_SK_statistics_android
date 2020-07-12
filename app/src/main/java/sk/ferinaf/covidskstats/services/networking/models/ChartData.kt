package sk.ferinaf.covidskstats.services.networking.models

import com.google.gson.annotations.SerializedName

data class ChartData(
    @SerializedName("date")
    val date: String,

    @SerializedName("deaths")
    val deaths: Int,

    @SerializedName("infected")
    val infected: Int,

    @SerializedName("recovered")
    val recovered: Int,

    @SerializedName("active")
    val active: Int,

    @SerializedName("tested_daily")
    val testedDaily: Int,

    @SerializedName("infected_daily")
    val infectedDaily: Int,

    @SerializedName("day")
    val day: String
)