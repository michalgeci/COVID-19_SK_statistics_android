package sk.ferinaf.covidskstats.services.networking.models

import com.google.gson.annotations.SerializedName

data class EventsData(
    @SerializedName("id")
    val id: Int,

    @SerializedName("day")
    val day: String,

    @SerializedName("time")
    val time: Long,

    @SerializedName("title")
    val title: String,

    @SerializedName("major")
    val major: Int
)