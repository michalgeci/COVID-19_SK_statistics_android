package sk.ferinaf.covidskstats.networking.models

import com.google.gson.annotations.SerializedName

data class TestedChartData(
    @SerializedName("date")
    val date: String,

    @SerializedName("tested")
    val tested: String,

    @SerializedName("infected")
    val infected: String,

    @SerializedName("day")
    val day: String
)