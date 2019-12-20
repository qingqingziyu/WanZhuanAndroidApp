package com.cf.wanzhuanandroidapp.ui.collect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cf.ktx_base.base.BaseVMActivity
import com.cf.ktx_base.ext.dp2px
import com.cf.wanzhuanandroidapp.R
import com.cf.wanzhuanandroidapp.adapter.HomeArticleAdapter
import com.cf.wanzhuanandroidapp.ui.BrowserNormalActivity
import com.cf.wanzhuanandroidapp.ui.home.ArticleViewModel
import com.cf.wanzhuanandroidapp.view.CustomLoadMoreView
import com.cf.wanzhuanandroidapp.view.SpaceItemDecoration
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.activity_my_collect.*
import kotlinx.android.synthetic.main.title_layout.*
import luyao.util.ktx.ext.startKtxActivity
import luyao.util.ktx.ext.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyCollectActivity : BaseVMActivity<ArticleViewModel>() {

    private val mViewModel: ArticleViewModel by viewModel()

    private val articleAdapter by lazy { HomeArticleAdapter() }

    override fun getLayoutResId(): Int {
        return R.layout.activity_my_collect
    }

    override fun initView() {
        mToolbar.title = "我的收藏"
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        collectRecycleView.run {
            layoutManager = LinearLayoutManager(this@MyCollectActivity)
            addItemDecoration(SpaceItemDecoration(collectRecycleView.dp2px(10)))
        }
        articleAdapter.run {
            setOnItemClickListener { adapter, view, position ->
                startKtxActivity<BrowserNormalActivity>(value = BrowserNormalActivity.URL to articleAdapter.data[position].link)
            }
            onItemChildClickListener = itemChildClickListener
            setLoadMoreView(CustomLoadMoreView())
            setOnLoadMoreListener({
                loadMore()
            }, collectRecycleView)
            collectRecycleView.adapter = articleAdapter
        }

    }

    private fun loadMore() {
        mViewModel.getCollectArticleList(false)
    }

    override fun initData() {
        mToolbar.setNavigationOnClickListener { onBackPressed() }
        refresh()
    }

    private fun refresh() {
        articleAdapter.setEnableLoadMore(false)
        mViewModel.getCollectArticleList(true)
    }

    override fun startObserve() {
        mViewModel.apply {
            uiState.observe(this@MyCollectActivity, Observer {
                collectRefreshLayout.isRefreshing = it.showLoading
                it.showSuccess?.let { list ->
                    {
                        list.datas.forEach { it.collect = true }
                        articleAdapter.run {
                            if (it.isRefresh) replaceData(list.datas)
                            else addData(list.datas)
                            setEnableLoadMore(true)
                            loadMoreComplete()
                        }
                    }
                }
                if (it.showEnd) articleAdapter.loadMoreEnd()
                it.showError?.let { message ->
                    toast(if (message.isBlank()) "网络异常" else message)
                }
            })
        }
    }

    private val itemChildClickListener =
        BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.articleStar -> {
                    articleAdapter.run {
                        data[position].run {
                            collect = !collect
                            mViewModel.collectArticle(originId, collect)
                        }
                        notifyItemRemoved(position)
                    }
                }
            }
        }


}
