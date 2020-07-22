package sk.ferinaf.covidskstats.ui.districts

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_district.*
import sk.ferinaf.covidskstats.MainActivity
import sk.ferinaf.covidskstats.R
import sk.ferinaf.covidskstats.util.ONLY_NEW
import sk.ferinaf.covidskstats.util.SETTINGS
import sk.ferinaf.covidskstats.util.SavesRecyclerViewState
import sk.ferinaf.covidskstats.util.smoothScrollToTop

class DistrictsFragment : Fragment(), SavesRecyclerViewState {

    companion object {
        const val DISTRICTS_FRAGMENT = "districtsFragment"
    }

    private lateinit var viewModel: DistrictsViewModel
    override var offsetFixed = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_district, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DistrictsViewModel::class.java)

        val mActivity = (activity as? MainActivity) ?: return

        mActivity.title = getString(R.string.districts)

        // Init recycler adapter and layout manager
        val adapter = DistrictsListAdapter(listOf(), true)
        districtsFragment_recyclerView?.adapter = adapter
        districtsFragment_recyclerView?.layoutManager = LinearLayoutManager(context)

        // Handle favorite toggle
        adapter.onFavoriteToggle = { id ->
            viewModel.toggleFavorite(id)
        }

        // Populate data
        viewModel.getData().observe(mActivity, Observer {
            adapter.data = it
            adapter.notifyDataSetChanged()

            fixOffset(DISTRICTS_FRAGMENT, districtsFragment_recyclerView)
        })

        // Load switch state
        val sp = context?.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
        val onlyNew = sp?.getBoolean(ONLY_NEW, false) ?: false
        districtsFragment_onlyNew_switch?.isChecked = onlyNew
        if (onlyNew) {
            viewModel.toggleRecent(true)
        }

        // Configure switch
        districtsFragment_onlyNew_switch?.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleRecent(isChecked)
            val edit = sp?.edit()
            edit?.putBoolean(ONLY_NEW, isChecked)
            edit?.apply()
            districtsFragment_recyclerView?.layoutManager?.scrollToPosition(0)
        }

    }

    override fun wasReselected() {
        districtsFragment_recyclerView?.smoothScrollToTop()
    }

    override fun onPause() {
        super.onPause()
        saveRecyclerState(DISTRICTS_FRAGMENT, districtsFragment_recyclerView)
    }

}
