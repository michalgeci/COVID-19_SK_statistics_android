package sk.ferinaf.covidskstats.ui.history

data class HistoryItemModel(
    val date: String,
    val day: String,
    val tested: Int,
    val positive: Int,
    val promile: Float
)