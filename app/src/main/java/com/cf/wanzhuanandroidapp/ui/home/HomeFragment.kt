package com.cf.wanzhuanandroidapp.ui.home


import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cf.ktx_base.base.BaseFragment
import com.cf.ktx_base.base.BaseVMFragment
import com.cf.ktx_base.ext.dp2px
import com.cf.ktx_base.ext.fromN

import com.cf.wanzhuanandroidapp.R
import com.cf.wanzhuanandroidapp.adapter.HomeArticleAdapter
import com.cf.wanzhuanandroidapp.util.GlideImageLoader
import com.cf.wanzhuanandroidapp.view.CustomLoadMoreView
import com.cf.wanzhuanandroidapp.view.SpaceItemDecoration
import com.chad.library.adapter.base.BaseQuickAdapter
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.fragment_home.*
import luyao.util.ktx.ext.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *@作者:陈飞
 *@说明:首页
 *@时间:2019/11/27 16:20
 */
class HomeFragment : BaseVMFragment<ArticleViewModel>() {
    private val mViewModel: ArticleViewModel by viewModel()

    private val homeArticleAdapter by lazy { HomeArticleAdapter() }

    private val banner by lazy { Banner(activity) }

    private val bannerImages = mutableListOf<String>()

    private val bannerTitles = mutableListOf<String>()

    private val bannerUrls = mutableListOf<String>()


    override fun getLayoutResId(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        //设置RecyclerView
        mRecyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(SpaceItemDecoration(mRecyclerView.dp2px(10)))
        }
        //设置适配器
        homeArticleAdapter.run {
            setOnItemClickListener { adapter, view, position ->

            }
            onItemChildClickListener = this@HomeFragment.onItemChildClickListener
            //添加头部
            addHeaderView(banner)
            setLoadMoreView(CustomLoadMoreView())
            setOnLoadMoreListener({ loadMore() }, mRecyclerView)
        }
        mRecyclerView.adapter = homeArticleAdapter
        //banner
        banner.run {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                banner.dp2px(200)
            )
            setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
            setImageLoader(GlideImageLoader())
            setOnBannerListener { position ->
                {
                    kotlin.run {

                    }
                }
            }
        }
        //刷新
        homeRefreshLayout.run {
            setOnRefreshListener {
                refresh()
            }
        }
    }

     fun refresh() {
        homeArticleAdapter.setEnableLoadMore(false)
        mViewModel.getHomeArticleList(true)
    }

    private fun loadMore() {
        mViewModel.getHomeArticleList(false)
    }

    override fun initData() {
        refresh()
    }

    override fun startObserve() {
        mViewModel.apply {
            mBanner.observe(this@HomeFragment, Observer { t ->
                t?.let {
                    setBanner(it)
                }
            })

            uiState.observe(this@HomeFragment, Observer {
                homeRefreshLayout.isRefreshing = it.showLoading
                it.showSuccess?.let { articleList ->
                    homeArticleAdapter.run {
                        if (it.isRefresh) replaceData(articleList.datas)
                        else addData(articleList.datas)
                        setEnableLoadMore(true)
                        loadMoreComplete()
                    }
                }
                if (it.showEnd) homeArticleAdapter.loadMoreEnd()

                it.showError?.let { message ->
                    activity?.toast(if (message.isBlank()) "网络异常" else message)
                }
            })
        }
    }

    override fun onStart() {
        super.onStart()
        banner.startAutoPlay()
    }

    override fun onStop() {
        super.onStop()
        banner.stopAutoPlay()
    }

    /**
     *@作者:陈飞
     *@说明:设置首页数据
     *@时间:2019/11/29 16:18
     */
    private fun setBanner(bannerList: List<com.cf.wanzhuanandroidapp.model.bean.Banner>) {
        for (banner in bannerList) {
            bannerImages.add(banner.imagePath)
            bannerTitles.add(banner.title)
            bannerUrls.add(banner.url)
        }
        banner.setImages(bannerImages)
            .setBannerTitles(bannerTitles)
            .setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
            .setDelayTime(3000)
        banner.start()
    }

    private val onItemChildClickListener =
        BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.articleStar -> {

                }
            }
        }
}
