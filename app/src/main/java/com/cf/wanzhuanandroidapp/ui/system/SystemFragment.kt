package com.cf.wanzhuanandroidapp.ui.system


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cf.ktx_base.base.BaseVMFragment
import com.cf.ktx_base.ext.dp2px
import com.cf.wanzhuanandroidapp.BR

import com.cf.wanzhuanandroidapp.R
import com.cf.wanzhuanandroidapp.adapter.BaseBindAdapter
import com.cf.wanzhuanandroidapp.model.bean.SystemParent
import com.cf.wanzhuanandroidapp.view.SpaceItemDecoration
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.fragment_system.*
import luyao.util.ktx.ext.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *@作者:陈飞
 *@说明:体系
 *@时间:2019/12/2 14:58
 */
class SystemFragment : BaseVMFragment<SystemViewModel>() {

    private val mViewModel: SystemViewModel by viewModel()

    private val systemAdapter by lazy {
        BaseBindAdapter<SystemParent>(
            R.layout.item_system,
            BR.systemParent
        )
    }


    override fun getLayoutResId(): Int {
        return R.layout.fragment_system
    }

    override fun initView() {
        systemRecycleView.run {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(SpaceItemDecoration(systemRecycleView.dp2px(10)))
            adapter = systemAdapter
        }

        systemAdapter.onItemClickListener =
            BaseQuickAdapter.OnItemClickListener { adapter, view, position ->

            }
        systemRefreshLayout.setOnRefreshListener { refresh() }
    }

    private fun refresh() {
        mViewModel.getSystemTypes()
    }

    override fun initData() {
        refresh()
    }

    override fun startObserve() {
        mViewModel.run {
            uiState.observe(this@SystemFragment, Observer {
                systemRefreshLayout.isRefreshing = it.showLoading
                it.showSuccess?.let { list ->
                    systemAdapter.replaceData(list)
                }
                it.showError?.let { message ->
                    activity?.toast(message)
                }
            })
        }
    }


}
