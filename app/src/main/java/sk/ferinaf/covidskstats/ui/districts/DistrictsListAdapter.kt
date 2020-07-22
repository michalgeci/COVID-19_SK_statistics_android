package sk.ferinaf.covidskstats.ui.districts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class DistrictsListAdapter(var data: List<DistrictsItemModel>, var showAll: Boolean): RecyclerView.Adapter<DistrictsViewHolder>() {

    var onFavoriteToggle: (id: Int) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistrictsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DistrictsViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: DistrictsViewHolder, position: Int) {
        holder.title = data[position].name
        holder.infected = data[position].newInfected.toString()
        holder.totalOccurrence = data[position].totalOccurrence.toString()
        holder.lastOccurrence = data[position].lastOccurrence
        holder.hidden = !showAll
        holder.setFavorite(data[position].favorite)
        holder.onClickFavorite = {
            onFavoriteToggle(data[position].id)
        }
    }

}
