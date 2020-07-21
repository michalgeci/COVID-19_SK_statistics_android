package sk.ferinaf.covidskstats.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class HistoryListAdapter(var data: List<HistoryItemModel>): RecyclerView.Adapter<HistoryItemViewHolder>() {

    var onClicked: (position: Int)->Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HistoryItemViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        val itemData = data[position]
        holder.date = itemData.date
        holder.day = itemData.day
        holder.tested = itemData.tested
        holder.positive = itemData.positive
        holder.promile = itemData.promile
        holder.onClick = {
            onClicked(position)
        }
    }

}