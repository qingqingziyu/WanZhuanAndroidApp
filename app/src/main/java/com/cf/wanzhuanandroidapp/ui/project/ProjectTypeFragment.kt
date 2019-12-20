package com.cf.wanzhuanandroidapp.ui.project


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cf.ktx_base.base.BaseVMFragment
import com.cf.ktx_base.ext.dp2px
import com.cf.wanzhuanandroidapp.BR

import com.cf.wanzhuanandroidapp.R
import com.cf.wanzhuanandroidapp.adapter.BaseBindAdapter
import com.cf.wanzhuanandroidapp.model.bean.Article
import com.cf.wanzhuanandroidapp.ui.home.ArticleViewModel
import com.cf.wanzhuanandroidapp.view.CustomLoadMoreView
import com.cf.wanzhuanandroidapp.view.SpaceItemDecoration
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.fragment_project_type.*
import kotlinx.coroutines.internal.AddLastDesc
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *@作者:陈飞
 *@说明:最新项目
 *@时间:2019/12/2 13:38
 */
class ProjectTypeFragment : BaseVMFragment<ArticleViewModel>() {

    private val mViewModel: ArticleViewModel by viewModel()

    private val cid by lazy { arguments?.getInt(CID) }

    private val isLasted by lazy { arguments?.getBoolean(LASTED) }

    private val projectAdapter by lazy {
        BaseBindAdapter<Article>(
            R.layout.item_project,
            BR.article
        )
    }

    companion object {
        private const val CID = "projectCid"
        private const val LASTED = "lasted"
        fun newInstance(cid: Int, isLasted: Boolean): ProjectTypeFragment {
            val fragment = ProjectTypeFragment()
            val bundle = Bundle()
            bundle.putInt(CID, cid)
            bundle.putBoolean(LASTED, isLasted)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_project_type
    }

    override fun initView() {
        //设置刷新事件
        projectRefreshLayout.setOnRefreshListener { refresh() }
        projectAdapter.run {
            setOnItemClickListener { adapter, view, position ->

            }
            setLoadMoreView(CustomLoadMoreView())
            setOnLoadMoreListener({ loadMore() }, projectRecycleView)
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.articleStar -> {

                    }
                }
            }
        }
        projectRecycleView.run {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(SpaceItemDecoration(projectRecycleView.dp2px(10)))
            adapter = projectAdapter
        }
    }

    private fun loadMore() {
        loadData(false)
    }

    fun refresh() {
        projectAdapter.setEnableLoadMore(false)
        loadData(true)
    }

    private fun loadData(boolean: Boolean) {
        isLasted?.run {
            if (this) {
                mViewModel.getLatestProjectList(boolean)
            } else {
                cid?.let {
                    mViewModel.getProjectTypeDetailList(boolean, it)
                }
            }
        }
    }

    override fun initData() {
        refresh()
    }

    override fun startObserve() {
        mViewModel.uiState.observe(this, Observer {
            projectRefreshLayout.isRefreshing = it.showLoading
            it.showSuccess?.let { list ->
                projectAdapter.run {
                    if (it.isRefresh) replaceData(list.datas)
                    else addData(list.datas)
                    setEnableLoadMore(true)
                    loadMoreComplete()
                }
            }
            if (it.showEnd) projectAdapter.loadMoreEnd()
        })
    }


}
