package sk.ferinaf.covidskstats.util

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import sk.ferinaf.covidskstats.MainActivity

interface SavesRecyclerViewState {

    var offsetFixed: Boolean

    fun Fragment.saveRecyclerState(from: String, recyclerView: RecyclerView?) {
        val mActivity = (activity as? MainActivity) ?: return
        val bundle = Bundle()
        bundle.putParcelable("recyclerState", recyclerView?.layoutManager?.onSaveInstanceState())
        mActivity.states[from] = bundle
    }

    fun Fragment.fixOffset(from: String, recyclerView: RecyclerView?) {
        if (offsetFixed) return
        val mActivity = (activity as? MainActivity) ?: return
        val bundle = mActivity.states[from]
        val parcelable = bundle?.getParcelable<Parcelable?>("recyclerState")
        recyclerView?.layoutManager?.onRestoreInstanceState(parcelable)
        offsetFixed = true
    }

    fun wasReselected() {
        Log.d("reselected", "was reselected")
    }

}

