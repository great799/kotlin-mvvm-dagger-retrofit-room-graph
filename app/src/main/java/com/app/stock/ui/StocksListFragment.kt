package com.app.stock.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.stock.R
import com.app.stock.base.BaseFragment
import com.app.stock.databinding.FragmentListOfStocksBinding
import com.app.stock.network.model.StockDetail
import com.app.stock.ui.adapter.StocksListAdapter
import com.app.stock.view_model.ListOfStocksViewModel


class StocksListFragment : BaseFragment(), StocksListAdapter.StockItemClickListener {
    override fun getLayoutId(): Int {
        return R.layout.fragment_list_of_stocks
    }

    override fun getViewModelClass(): Class<out ViewModel> {
        return ListOfStocksViewModel::class.java
    }

    override fun getViewModelFactory(): ViewModelProvider.Factory? {
        return null
    }

    private var stockListingAdapter: StocksListAdapter? = null
    private var stocks = mutableListOf<StockDetail>()
    private lateinit var binding: FragmentListOfStocksBinding
    private lateinit var viewModel: ListOfStocksViewModel
    private val sids = arrayListOf<String>("TCS", "RELI", "HDBK", "INFY", "ITC")

    companion object {
        fun getNewInstance(): StocksListFragment {
            val fragment = StocksListFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        retainInstance = true
    }

    private fun initBindingAndViewModel() {
        binding = getBinding() as FragmentListOfStocksBinding
        viewModel = getViewModel() as ListOfStocksViewModel
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
        binding.rvStocks.layoutManager = LinearLayoutManager(activity)
        binding.rvStocks.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
        /*
        * call api to fetch list of all stocks
        * */
        if (stocks.isNullOrEmpty()) {
            callApiToFetchStocks()
        } else {
            binding.rvStocks.adapter = stockListingAdapter
        }
    }

    private fun callApiToFetchStocks() {
        viewModel.fetchListOfStocks(sids)
    }

    private fun setStockListingAdapter(stocks: List<StockDetail>) {
        if (stockListingAdapter == null) {
            this.stocks.addAll(stocks)
            stockListingAdapter = StocksListAdapter(this.stocks)
            binding.rvStocks.adapter = stockListingAdapter
            stockListingAdapter?.setItemClickListener(this)
        } else {
            this.stocks.clear()
            this.stocks.addAll(stocks)
        }
        stockListingAdapter?.notifyDataSetChanged()
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
        * list of stocks updated
        * */
        viewModel.listOfStocksLiveData.observe(this, Observer { stocks ->
            if (isAdded) {
                setStockListingAdapter(stocks)
            }
        })

        /*
        * Handle Api errors here
        * */
        viewModel.apiErrorLiveData.observe(this, Observer {
            if (isAdded) {
                handleApiError(it)
            }
        })

        /*
        * Handle other Api errors here
        * */
        viewModel.otherApiErrorsLiveData.observe(this, Observer {
            if (isAdded) {
                handleOtherApiErrors(it)
            }
        })
    }

    override fun onItemClick(stockDetail: StockDetail) {
        moveToHistoryFragment(stockDetail)
    }

    private fun moveToHistoryFragment(stockDetail: StockDetail) {
        replaceFragment(
            HistoryFragment.getNewInstance(stockDetail),
            true,
            HistoryFragment::class.java.simpleName
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        val pollItem = menu.findItem(R.id.menuPlay)
        if (viewModel.isPolling) {
            pollItem.setIcon(R.drawable.ic_baseline_pause)
        } else {
            pollItem.setIcon(R.drawable.ic_baseline_play_arrow)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuHistory -> {
                if (viewModel.highestPriceStockDetail != null) {
                    moveToHistoryFragment(viewModel.highestPriceStockDetail!!)
                }
            }
            R.id.menuPlay -> {
                if (viewModel.isPolling) {
                    viewModel.isPolling = false
                    item.setIcon(R.drawable.ic_baseline_play_arrow)
                    viewModel.cancelStocksPollingCoroutine()
                } else {
                    viewModel.isPolling = true
                    item.setIcon(R.drawable.ic_baseline_pause)
                    viewModel.pollLatestStockDetails(sids)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}