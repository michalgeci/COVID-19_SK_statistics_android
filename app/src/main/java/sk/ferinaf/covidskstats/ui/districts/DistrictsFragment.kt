package sk.ferinaf.covidskstats.ui.districts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_district.*
import sk.ferinaf.covidskstats.R
import sk.ferinaf.covidskstats.ui.main.MainViewModel

class DistrictsFragment : Fragment() {

    companion object {
        fun newInstance() = DistrictsFragment()
    }

    private lateinit var viewModel: DistrictsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_district, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DistrictsViewModel::class.java)

        val mActivity = activity ?: return

        val adapter = DistrictsListAdapter(listOf(), true)

        adapter.onFavoriteToggle = { id ->
            viewModel.toggleFavorite(id)
        }

        districtsFragment_recyclerView?.adapter = adapter
        districtsFragment_recyclerView?.layoutManager = LinearLayoutManager(context)

        viewModel.getData().observe(mActivity, Observer {
            adapter.data = it
            adapter.notifyDataSetChanged()
        })

    }

}
