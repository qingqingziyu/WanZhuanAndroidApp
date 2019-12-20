package com.cf.wanzhuanandroidapp.ui.square


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
import com.cf.wanzhuanandroidapp.model.bean.Article
import com.cf.wanzhuanandroidapp.ui.home.ArticleViewModel
import com.cf.wanzhuanandroidapp.ui.login.LoginActivity
import com.cf.wanzhuanandroidapp.ui.share.ShareActivity
import com.cf.wanzhuanandroidapp.view.CustomLoadMoreView
import com.cf.wanzhuanandroidapp.view.SpaceItemDecoration
import com.tencent.smtt.utils.s
import kotlinx.android.synthetic.main.fragment_square.*
import luyao.util.ktx.ext.startKtxActivity
import luyao.util.ktx.ext.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *@作者:陈飞
 *@说明:广场
 *@时间:2019/12/2 11:15
 */
class SquareFragment : BaseVMFragment<ArticleViewModel>() {

    //适配器
    private val squareAdapter by lazy { BaseBindAdapter<Article>(R.layout.item_square, BR.article) }

    private val mViewModel: ArticleViewModel by viewModel()


    override fun getLayoutResId(): Int {
        return R.layout.fragment_square
    }

    override fun initView() {
        //设置适配器
        squareAdapter.run {
            //点击item跳转到别处
            setOnItemChildClickListener { adapter, view, position ->

            }
            setLoadMoreView(CustomLoadMoreView())
            setOnLoadMoreListener({ LoadMore() }, squareRecycleView)
        }
        //设置RecyclerView
        squareRecycleView.run {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(SpaceItemDecoration(squareRecycleView.dp2px(10)))
            adapter = squareAdapter
        }
        //设置刷新
        squareRefreshLayout.setOnRefreshListener {
            refresh()
        }
    }

    //刷新
    private fun refresh() {
        squareAdapter.setEnableLoadMore(false)
        mViewModel.getSquareArticleList(true)
    }

    private fun LoadMore() {
        mViewModel.getSquareArticleList(false)
    }

    override fun initData() {
        refresh()
    }

    override fun startObserve() {
        mViewModel.uiState.observe(this, Observer {
            squareRefreshLayout.isRefreshing = it.showLoading
            it.showSuccess?.let { list ->
                squareAdapter.run {
                    if (it.isRefresh) replaceData(list.datas)
                    else addData(list.datas)
                    setEnableLoadMore(true)
                    loadMoreComplete()
                }
            }
            if (it.showEnd) squareAdapter.loadMoreEnd()
            it.needLogin?.let { needLogin ->
                if (needLogin) startKtxActivity<LoginActivity>()
                else startKtxActivity<ShareActivity>()
            }
            it.showError?.let { message ->
                activity?.toast(if (message.isBlank()) "网络异常" else message)
            }
        })
    }


}
