package sk.ferinaf.covidskstats.ui.districts

data class DistrictsItemModel (
    val id: Int,
    val name: String,
    val newInfected: Int,
    val lastOccurrence: String,
    val totalOccurrence: Int,
    var favorite: Boolean
)
