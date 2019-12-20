package com.cf.wanzhuanandroidapp.ui.navigation


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cf.ktx_base.base.BaseVMFragment
import com.cf.ktx_base.ext.dp2px

import com.cf.wanzhuanandroidapp.R
import com.cf.wanzhuanandroidapp.adapter.NavigationAdapter
import com.cf.wanzhuanandroidapp.adapter.VerticalTabAdapter
import com.cf.wanzhuanandroidapp.model.bean.Navigation
import com.cf.wanzhuanandroidapp.view.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_navigation.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import q.rorbin.verticaltablayout.VerticalTabLayout
import q.rorbin.verticaltablayout.widget.TabView
import java.text.FieldPosition

/**
 *@作者:陈飞
 *@说明:导航页面
 *@时间:2019/12/2 15:46
 */
class NavigationFragment : BaseVMFragment<NavigationViewModel>() {

    private val mViewModel: NavigationViewModel by viewModel()

    private val navigationList = mutableListOf<Navigation>()

    private val tabAdapter by lazy { VerticalTabAdapter(navigationList.map { it.name }) }

    private val mLayoutManager by lazy { LinearLayoutManager(activity) }

    private val navigationAdapter by lazy { NavigationAdapter() }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_navigation
    }

    override fun initView() {
        navigationRecycleView.run {
            layoutManager = mLayoutManager
            addItemDecoration(SpaceItemDecoration(this.dp2px(10)))
            adapter = navigationAdapter
        }

        tabLayout.addOnTabSelectedListener(object : VerticalTabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabView?, position: Int) {
            }

            override fun onTabSelected(tab: TabView?, position: Int) {
                scrollTOPosition(position)
            }

        })
    }

    private fun scrollTOPosition(position: Int) {
        val firstPositon = mLayoutManager.findFirstVisibleItemPosition()
        val lastPosition = mLayoutManager.findLastVisibleItemPosition()
        when {
            position <= firstPositon || position >= lastPosition -> navigationRecycleView.smoothScrollToPosition(
                position
            )
            else -> navigationRecycleView.run {
                smoothScrollBy(0, this.getChildAt(position - firstPositon).top - this.dp2px(8))
            }
        }
    }

    override fun initData() {
        mViewModel.getNavigation()
    }

    override fun startObserve() {
        mViewModel.run {
            navigationListState.observe(this@NavigationFragment, Observer {
                it?.run {
                    getNavigation(it)
                }
            })
        }
    }

    private fun getNavigation(navigationList: List<Navigation>) {
        this.navigationList.clear()
        this.navigationList.addAll(navigationList)
        tabLayout.setTabAdapter(tabAdapter)
        navigationAdapter.setNewData(navigationList)
    }

}
