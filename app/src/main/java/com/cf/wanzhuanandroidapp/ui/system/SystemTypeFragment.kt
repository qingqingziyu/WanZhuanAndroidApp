package com.cf.wanzhuanandroidapp.ui.system


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cf.ktx_base.base.BaseVMFragment
import com.cf.ktx_base.ext.dp2px
import com.cf.wanzhuanandroidapp.CFApp

import com.cf.wanzhuanandroidapp.R
import com.cf.wanzhuanandroidapp.adapter.HomeArticleAdapter
import com.cf.wanzhuanandroidapp.ui.BrowserNormalActivity
import com.cf.wanzhuanandroidapp.ui.home.ArticleViewModel
import com.cf.wanzhuanandroidapp.ui.login.LoginActivity
import com.cf.wanzhuanandroidapp.util.Preference
import com.cf.wanzhuanandroidapp.view.CustomLoadMoreView
import com.cf.wanzhuanandroidapp.view.SharedPreferencesUtils
import com.cf.wanzhuanandroidapp.view.SpaceItemDecoration
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.fragment_system_type.*
import kotlinx.coroutines.channels.ticker
import luyao.util.ktx.ext.startKtxActivity
import luyao.util.ktx.ext.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.sql.Blob

/**
 *@作者:陈飞
 *@说明:体系下文章列表
 *@时间:2019/12/11 10:36
 */
class SystemTypeFragment : BaseVMFragment<ArticleViewModel>() {

    companion object {
        private const val CID = "cid"
        private const val BLOG = "blog"
        fun newInstance(cid: Int, isBlog: Boolean): SystemTypeFragment {
            val fragment = SystemTypeFragment()
            val bundle = Bundle()
            bundle.putBoolean(BLOG, isBlog)
            bundle.putInt(CID, cid)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val isLogin =
        SharedPreferencesUtils.getParam(CFApp.CONTEXT, Preference.IS_LOGIN, false) as Boolean

    private val mViewModel: ArticleViewModel by viewModel()

    private val cid by lazy { arguments?.getInt(CID) }

    private val isBlog by lazy {
        arguments?.getBoolean(BLOG) ?: false
    }

    private val systemTypeAdapter by lazy {
        HomeArticleAdapter()
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_system_type
    }

    override fun initView() {
        typeRefreshLayout.setOnRefreshListener { refresh() }
        systemTypeAdapter.run {
            setOnItemClickListener { adapter, view, position ->
                startKtxActivity<BrowserNormalActivity>(value = BrowserNormalActivity.URL to systemTypeAdapter.data[position].link)
            }
            onItemChildClickListener = this@SystemTypeFragment.onItemChildClickListener
            setLoadMoreView(CustomLoadMoreView())
            setOnLoadMoreListener({ loadMore() }, typeRecycleView)
        }
        typeRecycleView.run {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SpaceItemDecoration(typeRecycleView.dp2px(10)))
            adapter = systemTypeAdapter
        }
    }

    private fun loadMore() {
        loadData(false)
    }

    private fun loadData(b: Boolean) {
        cid?.let {
            if (this.isBlog)
                mViewModel.getBlogArticleList(b, it)
            else
                mViewModel.getSystemTypeArticleList(b, it)
        }
    }

    private fun refresh() {
        systemTypeAdapter.setEnableLoadMore(false)
        loadData(true)
    }

    override fun initData() {

    }

    override fun startObserve() {
        mViewModel.uiState.observe(this, Observer {
            typeRefreshLayout.isRefreshing = it.showLoading

            it.showSuccess?.let { list ->
                systemTypeAdapter.run {
                    if (it.isRefresh)
                        replaceData(list.datas)
                    else
                        addData(list.datas)
                    loadMoreComplete()
                }
            }
            if (it.showEnd) systemTypeAdapter.loadMoreEnd()

            it.showError?.let { message ->
                activity?.toast(if (message.isBlank()) "网络异常" else message)
            }
        })
    }

    private val onItemChildClickListener =
        BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.articleStar -> {
                    if (isLogin) {
                        systemTypeAdapter.run {
                            data[position].run {
                                collect = !collect
                                mViewModel.collectArticle(id, collect)
                            }
                            notifyDataSetChanged()
                        }
                    } else {
                        Intent(activity, LoginActivity::class.java).run {
                            startActivity(this)
                        }
                    }
                }
            }
        }

}
