package sk.ferinaf.covidskstats.networking.models

import com.google.gson.annotations.SerializedName

data class DistrictAmountData(
    @SerializedName("deaths")
    val deaths: Int,

    @SerializedName("recovered")
    val recovered: Int,

    @SerializedName("infected")
    val infected: Int,

    @SerializedName("infected_delta")
    val infectedDelta: Int
)