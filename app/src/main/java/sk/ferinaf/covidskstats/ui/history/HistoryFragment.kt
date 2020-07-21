package sk.ferinaf.covidskstats.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_history.*
import sk.ferinaf.covidskstats.R
import sk.ferinaf.covidskstats.util.showDayDetailDialog

class HistoryFragment : Fragment() {

    companion object {
        fun newInstance() = HistoryFragment()
    }

    private lateinit var viewModel: HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        val mActivity = activity ?: return

        val adapter = HistoryListAdapter(listOf())
        historyFragment_recyclerView?.adapter = adapter
        historyFragment_recyclerView?.layoutManager = LinearLayoutManager(context)

        adapter.onClicked = { position ->
            val data = viewModel.getDetail(position)
            activity?.showDayDetailDialog(data)
        }

        viewModel.getData().observe(mActivity, Observer { historyData ->
            adapter.data = historyData
            adapter.notifyDataSetChanged()
        })

    }

}
