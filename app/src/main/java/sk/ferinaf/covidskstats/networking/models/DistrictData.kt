package sk.ferinaf.covidskstats.networking.models

import com.google.gson.annotations.SerializedName

data class DistrictData (
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("amount")
    val amount: DistrictAmountData,

    @SerializedName("last_occurrence_timestamp")
    val lastOccurrenceTimestamp: Long,

    @SerializedName("last_occurrence")
    val lastOccurrence: String,

    @SerializedName("last_occurrence_day")
    val lastOccurrenceDay: String
)