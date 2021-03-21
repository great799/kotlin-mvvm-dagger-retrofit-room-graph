package com.app.stock

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import com.app.stock.base.BaseActivity
import com.app.stock.databinding.ActivityMainBinding
import com.app.stock.ui.StocksListFragment
import com.app.stock.view_model.MainActivityViewModel

class MainActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getViewModelClass(): Class<out ViewModel> {
        return MainActivityViewModel::class.java
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SmallCaseApp.getAppComponent().inject(this)
        initBindingAndViewModel()
        initUI()

        /*
        * Do not add to back stack as it is first fragment
        * */
        if (savedInstanceState == null) {
            addFragment(
                StocksListFragment.getNewInstance(),
                false,
                StocksListFragment::class.java.simpleName
            )
        }
    }

    private fun initUI() {
        setUpActionBar()
    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.mainToolBar)
        supportActionBar?.elevation = 0f
    }

    private fun initBindingAndViewModel() {
        binding = getBinding() as ActivityMainBinding
        viewModel = getViewModel() as MainActivityViewModel
        binding.viewModel = viewModel
    }

    override fun addFragment(fragment: Fragment, addToBackStack: Boolean, tag: String) {
        var transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(binding.mainFrame.id, fragment, tag)
        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }
        transaction.commit()
    }

    override fun replaceFragment(fragment: Fragment, addToBackStack: Boolean, tag: String) {
        var transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.mainFrame.id, fragment, tag)
        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }
        transaction.commit()
    }
}