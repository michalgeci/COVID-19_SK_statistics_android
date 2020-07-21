package sk.ferinaf.covidskstats.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.layout_warning_banner.*
import sk.ferinaf.covidskstats.R
import sk.ferinaf.covidskstats.util.hideLoader
import sk.ferinaf.covidskstats.util.showLoader
import sk.ferinaf.covidskstats.util.timestampToDateString

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val mActivity = activity ?: return
        val mContext = context ?: return

        val red =  ContextCompat.getColor(mContext, R.color.pieChartRed)
        val blue = ContextCompat.getColor(mContext, R.color.pieChartBule)
        val green =  ContextCompat.getColor(mContext, R.color.green)

        val sp = mContext.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val favoriteDistrictId = sp.getInt("favoriteDistrict", -1)

        mainFragment_pieChart?.description = Description().apply { text = "" }
        mainFragment_pieChart?.legend?.isEnabled = false
        mainFragment_pieChart?.isHighlightPerTapEnabled = false
        mainFragment_pieChart?.isRotationEnabled = false

        updateData_button?.setOnClickListener {
            showLoader()
            updateData_button?.isEnabled = false
            viewModel.fetchData {
                hideLoader()
                updateData_button?.isEnabled = true
                mainFragment_warningBanner?.visibility = View.GONE
            }
        }

        viewModel.getData().observe(mActivity, Observer {
            val lastDay = it.chart.last()

            // Set day
            val dayText = lastDay.day + " " + lastDay.date.replace("-", ". ")
            mainFragment_dayOfStatistics?.text = dayText

            // Check if current
            if (!viewModel.current) {
                mainFragment_dayOfStatistics?.setTextColor(red)
                mainFragment_warningBanner?.visibility = View.VISIBLE
            } else {
                mainFragment_dayOfStatistics?.setTextColor(green)
                mainFragment_warningBanner?.visibility = View.GONE
            }

            // Set last day data
            mainFragment_numOfTested_textView?.text = lastDay.testedDaily.toString()
            mainFragment_numOfInfected_textView?.text = lastDay.infectedDaily.toString()

            // Set favorite city
            val favoriteDistrict = viewModel.getFavoriteCity()
            if (favoriteDistrict != null) {
                mainFragment_favoriteCity_cardView?.visibility = View.VISIBLE
                mainFragment_districtTitle_textView?.text = favoriteDistrict.title
                mainFragment_newInfected_textView?.text = favoriteDistrict.amount.infectedDelta.toString()
                mainFragment_totalOccurrence_textView?.text = favoriteDistrict.amount.infected.toString()
                mainFragment_lastOccurrence_textView?.text = favoriteDistrict.lastOccurrenceTimestamp.timestampToDateString()
            } else {
                mainFragment_favoriteCity_cardView?.visibility = View.GONE
            }

            // Set pie chart
            val testedPie = lastDay.testedDaily - lastDay.infectedDaily
            val infectedPie = lastDay.infectedDaily
            val entries = mutableListOf(PieEntry(testedPie.toFloat()), PieEntry(infectedPie.toFloat()))
            val pieDataSet = PieDataSet(entries, "")
            pieDataSet.setDrawValues(false)
            pieDataSet.colors = listOf(blue, red)

            val centerText = "%.2f‰\npozitívnych\ntestov".format(viewModel.promile)
            mainFragment_pieChart?.centerText = centerText
            mainFragment_pieChart?.data = PieData(pieDataSet)
            mainFragment_pieChart?.invalidate()
        })

    }

}