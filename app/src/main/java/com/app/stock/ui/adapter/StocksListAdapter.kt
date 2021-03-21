package com.app.stock.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.stock.R
import com.app.stock.databinding.ItemStockBinding
import com.app.stock.network.model.AppCurrency
import com.app.stock.network.model.StockDetail
import com.app.stock.utils.AppCurrencyFormat

class StocksListAdapter(private val stocks: List<StockDetail>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var stockItemClickListener: StockItemClickListener? = null

    fun setItemClickListener(stockItemClickListener: StockItemClickListener?) {
        this.stockItemClickListener = stockItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StockItemViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_stock,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return stocks.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holder = holder as StockItemViewHolder
        holder.bind(stocks[position])
        holder.binding.root.setOnClickListener {
            stockItemClickListener?.onItemClick(stocks[position])
        }
    }

    inner class StockItemViewHolder(val binding: ItemStockBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(stockDetail: StockDetail) {
            binding.txtStockName.text = stockDetail.sid
            binding.txtStockValue.text =
                AppCurrencyFormat.addCurrencySymbolToNumber(
                    binding.txtStockValue.context,
                    AppCurrency.RUPEES,
                    stockDetail.price
                )

            if (stockDetail.change > 0) {
                binding.txtStockValue.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_baseline_arrow_drop_up,
                    0
                )
            } else {
                binding.txtStockValue.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_baseline_arrow_drop_down,
                    0
                )
            }
        }
    }

    interface StockItemClickListener {
        fun onItemClick(stockDetail: StockDetail)
    }
}