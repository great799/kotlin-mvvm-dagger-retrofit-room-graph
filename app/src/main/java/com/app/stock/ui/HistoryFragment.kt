package com.app.stock.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.stock.R
import com.app.stock.base.BaseFragment
import com.app.stock.database.Stock
import com.app.stock.databinding.FragmentHistoryBinding
import com.app.stock.network.model.StockDetail
import com.app.stock.view_model.HistoryViewModel
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries


const val SID = "sid"

@Suppress("DEPRECATION")
class HistoryFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_history
    }

    override fun getViewModelClass(): Class<out ViewModel> {
        return HistoryViewModel::class.java
    }

    override fun getViewModelFactory(): ViewModelProvider.Factory? {
        return null
    }

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var viewModel: HistoryViewModel
    private var stockDetail: StockDetail? = null

    companion object {
        fun getNewInstance(stockDetail: StockDetail): HistoryFragment {
            val fragment = HistoryFragment()
            val bundle = Bundle().apply {
                putParcelable(SID, stockDetail)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        stockDetail = arguments?.getParcelable(SID)
    }

    private fun initBindingAndViewModel() {
        binding = getBinding() as FragmentHistoryBinding
        viewModel = getViewModel() as HistoryViewModel
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindingAndViewModel()
        initUI()
        if (savedInstanceState == null) {
            initObservers()
        }
    }

    private fun initUI() {
        viewModel.getHistoryOfSID(stockDetail?.sid ?: "")

        binding.txtStockPrice.text = stockDetail?.price.toString()
        if (stockDetail?.change!! > 0.0) {
            binding.txtStockPriceChange.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_baseline_arrow_drop_up,
                0
            )
            binding.txtStockPriceChange.setTextColor(resources.getColor(android.R.color.holo_green_dark))
        } else {
            binding.txtStockPriceChange.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_baseline_arrow_drop_down,
                0
            )
            binding.txtStockPriceChange.setTextColor(resources.getColor(android.R.color.holo_red_dark))
        }
        binding.txtStockPriceChange.text = stockDetail?.change.toString()
        var perChangeString = ""
        val changePer = ((stockDetail?.change!! / stockDetail?.price!!) * 100)
        perChangeString = if (changePer > 0) {
            getString(R.string.positive_change_percentage, changePer)
        } else {
            getString(R.string.negative_change_percentage, changePer)
        }
        binding.txtStockPriceChangePer.text = perChangeString
    }

    private fun populateGraph(stocks: List<Stock>) {
        try {
            var i = 0
            val points =
                arrayOfNulls<DataPoint>(stocks.size)
            for (stock in stocks) {
                points[i++] = DataPoint(
                    i*5.toDouble(),
                    stock.price
                )
            }
            val series =
                LineGraphSeries(points)
            series.isDrawDataPoints = true
            series.dataPointsRadius = 10f
            series.thickness = 8
            binding.historyGraph.gridLabelRenderer.verticalAxisTitle =
                getString(R.string.price_in_rupee)
            binding.historyGraph.gridLabelRenderer.horizontalAxisTitle =
                getString(R.string.time_in_secs)
            binding.historyGraph.addSeries(series)

            // set manual X bounds
            binding.historyGraph.viewport.isXAxisBoundsManual = true
            binding.historyGraph.viewport.setMinX(0.0)
            binding.historyGraph.viewport.setMaxX(20.0)

            // enable scrolling
            binding.historyGraph.viewport.isScrollable = true
        } catch (e: IllegalArgumentException) {
            Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun initObservers() {
        /*
        * show hide loader
        * */
        viewModel.loadingVisibilityLiveData.observe(this, Observer { isLoading ->
            if (isAdded) {
                if (isLoading) {
                    showLoading()
                } else {
                    hideLoading()
                }
            }
        })

        /*
        * records from database
        * */
        viewModel.listOfStocksLiveData.observe(this, Observer {
            if (isAdded) {
                populateGraph(it)
            }
        })
    }
}