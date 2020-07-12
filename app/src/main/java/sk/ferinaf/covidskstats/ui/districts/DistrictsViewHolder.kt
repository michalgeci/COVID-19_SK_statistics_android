package sk.ferinaf.covidskstats.ui.districts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sk.ferinaf.covidskstats.R

class DistrictsViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_district, parent, false)) {

    var onClickFavorite: ()->Unit = {}
    var favoriteButton: ImageButton? = null

    private var titleTextView: TextView? = null
    var title: String
        get() {
            return (titleTextView?.text ?: "").toString()
        }
        set(value) {
            titleTextView?.text = value
        }

    private var infectedTextView: TextView? = null
    var infected: String
        get() {
            return (infectedTextView?.text ?: "").toString()
        }
        set(value) {
            infectedTextView?.text = value
        }

    private var totalOccurrenceTextView: TextView? = null
    var totalOccurrence: String
        get() {
            return (totalOccurrenceTextView?.text ?: "").toString()
        }
        set(value) {
            totalOccurrenceTextView?.text = value
        }

    private var lastOccurrenceTextView: TextView? = null
    var lastOccurrence: String
        get() {
            return (lastOccurrenceTextView?.text ?: "").toString()
        }
        set(value) {
            lastOccurrenceTextView?.text = value
        }

    private var lastOccurrenceLayout: LinearLayout? = null
    private var mHidden = false
    var hidden: Boolean
        get() = mHidden
        set(value) {
            mHidden = value
            lastOccurrenceLayout?.visibility = if (mHidden) View.GONE else View.VISIBLE
        }

    init {
        titleTextView = itemView.findViewById(R.id.districtItem_districtTitle_textView)
        infectedTextView = itemView.findViewById(R.id.districtItem_newInfected_textView)
        lastOccurrenceTextView = itemView.findViewById(R.id.districtItem_lastOccurrence_textView)
        totalOccurrenceTextView = itemView.findViewById(R.id.districtItem_totalOccurrence_textView)
        lastOccurrenceLayout = itemView.findViewById(R.id.districtItem_lastOccurrenceRow_linearLayout)

        favoriteButton = itemView.findViewById(R.id.districtItem_favorite_imageButton)
        favoriteButton?.setOnClickListener {
            onClickFavorite()
        }
    }

    fun setFavorite(favorite: Boolean) {
        if (favorite) {
            favoriteButton?.setImageResource(R.drawable.ic_favorite_32)
        } else {
            favoriteButton?.setImageResource(R.drawable.ic_favorite_border_32)
        }
    }
}