package com.cf.wanzhuanandroidapp.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cf.ktx_base.base.BaseVMActivity
import com.cf.ktx_base.ext.dp2px
import com.cf.wanzhuanandroidapp.CFApp
import com.cf.wanzhuanandroidapp.R
import com.cf.wanzhuanandroidapp.adapter.HomeArticleAdapter
import com.cf.wanzhuanandroidapp.model.bean.Hot
import com.cf.wanzhuanandroidapp.ui.BrowserNormalActivity
import com.cf.wanzhuanandroidapp.ui.login.LoginActivity
import com.cf.wanzhuanandroidapp.util.Preference
import com.cf.wanzhuanandroidapp.view.CustomLoadMoreView
import com.cf.wanzhuanandroidapp.view.SharedPreferencesUtils
import com.cf.wanzhuanandroidapp.view.SpaceItemDecoration
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.activity_search.*
import luyao.util.ktx.ext.startKtxActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : BaseVMActivity<SearchViewModel>() {

    private val isLogin =
        SharedPreferencesUtils.getParam(CFApp.CONTEXT, Preference.IS_LOGIN, false) as Boolean

    private val mViewModel: SearchViewModel by viewModel()

    private val searchAdapter by lazy { HomeArticleAdapter() }

    private var key = ""

    private val hotList = mutableListOf<Hot>()
    private val webSitesList = mutableListOf<Hot>()

    override fun getLayoutResId() = R.layout.activity_search

    override fun initView() {
        //设置返回
        searchToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)

        //初始化tag
        hotTagLayout.run {
            adapter = object : TagAdapter<Hot>(hotList) {
                override fun getView(parent: FlowLayout?, position: Int, t: Hot?): View {
                    val tv = LayoutInflater.from(parent!!.context).inflate(
                        R.layout.tag,
                        parent, false
                    ) as TextView
                    tv.text = t?.name
                    return tv
                }

                override fun getCount() = hotList.size
            }
            setOnTagClickListener { view, position, parent ->
                key = hotList[position].name
                startSearch(key)
                true
            }
        }

        webTagLayout.run {
            adapter = object : TagAdapter<Hot>(webSitesList) {
                override fun getCount() = webSitesList.size

                override fun getView(parent: FlowLayout?, position: Int, t: Hot?): View {
                    val tv = LayoutInflater.from(parent!!.context).inflate(
                        R.layout.tag,
                        parent, false
                    ) as TextView
                    tv.text = t?.name
                    return tv
                }
            }

            setOnTagClickListener { view, position, parent ->
                parent.context.startKtxActivity<BrowserNormalActivity>(value = BrowserNormalActivity.URL to webSitesList[position].link)
                true
            }
        }

        //初始化recycleView
        searchRecycleView.run {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            addItemDecoration(SpaceItemDecoration(searchRecycleView.dp2px(10)))
        }
        //初始化适配器
        searchAdapter.run {
            onItemChildClickListener =
                BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
                    when (view.id) {
                        R.id.articleStar -> {
                            if (isLogin) {
                                searchAdapter.run {
                                    data[position].run {
                                        collect = !collect
                                        mViewModel.collectArticle(id, collect)
                                    }
                                    notifyDataSetChanged()
                                }
                            } else {
                                startKtxActivity<LoginActivity>()
                            }
                        }
                        R.id.main_item_controll -> {
                            startKtxActivity<BrowserNormalActivity>(value = BrowserNormalActivity.URL to searchAdapter.data[position].link)
                        }
                    }
                }
            setLoadMoreView(CustomLoadMoreView())
            setOnLoadMoreListener({ loadMore() }, searchRecycleView)
        }
        searchRecycleView.adapter = searchAdapter
        val emptyView = layoutInflater.inflate(
            R.layout.empty_view,
            searchRecycleView.parent as ViewGroup,
            false
        )
        val emptyTv = emptyView.findViewById<TextView>(R.id.emptyTv)
        emptyTv.text = "换个关键词试试！"
        searchAdapter.emptyView = emptyView

        searchRefreshLayout.setOnRefreshListener { refresh() }

        searchView.run {
            isIconified = false
            onActionViewExpanded()
            setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        key = query
                        startSearch(key)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?) = false

            })
        }
    }

    private fun refresh() {
        searchAdapter.setEnableLoadMore(false)
        mViewModel.searchHot(true, key)
    }

    private fun loadMore() {
        mViewModel.searchHot(false, key)
    }

    private fun startSearch(key: String) {
        searchView.clearFocus()
        mViewModel.searchHot(true, key)
    }

    override fun initData() {
        searchToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        mViewModel.getHotSearch()
        mViewModel.getWebSites()
    }

    override fun onBackPressed() {
        if (hotContent.visibility == View.GONE) {
            hotContent.visibility = View.VISIBLE
            searchRecycleView.visibility = View.GONE
            searchAdapter.setNewData(null)
        } else
            finish()
    }

    override fun startObserve() {
        mViewModel.uiState.observe(this, Observer {
            searchRecycleView.visibility = if (it.showHot) View.GONE else View.VISIBLE
            hotContent.visibility = if (!it.showHot) View.GONE else View.VISIBLE
            searchRefreshLayout.isRefreshing = it.showLoading
            it.showSuccess?.let { list ->
                searchAdapter.run {
                    if (it.isRefresh) replaceData(list.datas)
                    else addData(list.datas)
                    setEnableLoadMore(true)
                    loadMoreComplete()
                }
            }
            if (it.showEnd) searchAdapter.loadMoreEnd()

            it.showHotSearch?.let { data ->
                hotList.clear()
                hotList.addAll(data)
                hotTagLayout.adapter.notifyDataChanged()
            }
            it.showWebSites?.let { data ->
                webSitesList.clear()
                webSitesList.addAll(data)
                webTagLayout.adapter.notifyDataChanged()
            }
        })
    }
}
