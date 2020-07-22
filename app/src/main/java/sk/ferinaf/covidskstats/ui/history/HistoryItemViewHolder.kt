package sk.ferinaf.covidskstats.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sk.ferinaf.covidskstats.R

class HistoryItemViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_history_entry, parent, false)) {

    var onClick: ()->Unit = {}

    private var dateTextView: TextView? = null
    var date: String
        get() = dateTextView?.text?.toString() ?: ""
        set(value) {
            dateTextView?.text = value
        }

    private var testedTextView: TextView? = null
    var tested: Int
        get() = testedTextView?.text?.toString()?.toInt() ?: 0
        set(value) {
            testedTextView?.text = value.toString()
        }

    private var positiveTextView: TextView? = null
    var positive: Int
        get() = positiveTextView?.text?.toString()?.toInt() ?: 0
        set(value) {
            positiveTextView?.text = value.toString()
        }

    private var promileTextView: TextView? = null
    private var mPromile = 0f
    var promile: Float
        get() = mPromile
        set(value) {
            mPromile = value
            val promileString = "%.2f‰".format(value)
            promileTextView?.text = promileString
        }

    private var dayTextView: TextView? = null
    private var mDay = ""
    var day: String
        get() = mDay
        set(value) {
            mDay = value
            val dayString = "($value)"
            dayTextView?.text = dayString
        }

    init {
        dateTextView = itemView.findViewById(R.id.historyItem_date)
        dayTextView = itemView.findViewById(R.id.historyItem_day)
        testedTextView = itemView.findViewById(R.id.historyItem_tested)
        positiveTextView = itemView.findViewById(R.id.historyItem_positive)
        promileTextView = itemView.findViewById(R.id.historyItem_promile)

        itemView.setOnClickListener { onClick() }
    }

}
