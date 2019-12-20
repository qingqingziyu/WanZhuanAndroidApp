package com.cf.wanzhuanandroidapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cf.ktx_base.base.BaseViewModel
import com.cf.wanzhuanandroidapp.core.Result
import com.cf.wanzhuanandroidapp.model.bean.ArticleList
import com.cf.wanzhuanandroidapp.model.bean.Hot
import com.cf.wanzhuanandroidapp.model.respository.CollectRepository
import com.cf.wanzhuanandroidapp.model.respository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(
    private val searchRepository: SearchRepository,
    private val collectRepository: CollectRepository
) : BaseViewModel() {

    private var currentPage = 0

    private val _uiState = MutableLiveData<SearchUIModel>()
    val uiState: LiveData<SearchUIModel>
        get() = _uiState

    /**
     *@作者:陈飞
     *@说明:获取搜索热点
     *@时间:2019/12/3 10:26
     */
    fun searchHot(isRefresh: Boolean = false, key: String) {
        viewModelScope.launch(Dispatchers.Default) {
            //开启进度条
            withContext(Dispatchers.Main) {
                emitArticleUiState(shoeLoading = true)
            }
            //如果是刷新，当前页赋值为0
            if (isRefresh) currentPage = 0
            val result = searchRepository.searchHot(currentPage, key)
            withContext(Dispatchers.Main) {
                if (result is Result.Success) {
                    val articleList = result.data
                    //如果已经到了底，那么不往下执行
                    if (articleList.offset > articleList.total) {
                        emitArticleUiState(shoeLoading = false, showEnd = true)
                        return@withContext
                    }
                    currentPage++;
                    //返回请求的结果
                    emitArticleUiState(
                        shoeLoading = false,
                        showSuccess = articleList,
                        isRefresh = isRefresh
                    )

                } else if (result is Result.Error) {
                    emitArticleUiState(shoeLoading = false, showError = result.exception.message)
                }
            }
        }
    }

    /**
     *@作者:陈飞
     *@说明:获取webSite
     *@时间:2019/12/3 10:30
     */
    fun getWebSites() {
        viewModelScope.launch(Dispatchers.Main) {
            //获取webSite
            val result = withContext(Dispatchers.IO) {
                searchRepository.getWebSite()
            }
            if (result is Result.Success) {
                emitArticleUiState(showHot = true, showWebSites = result.data)
            }
        }
    }

    fun getHotSearch() {
        viewModelScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                searchRepository.getHotSearch()
            }
            if (result is Result.Success)
                emitArticleUiState(showHot = true, showWebSites = result.data)
        }
    }

    fun collectArticle(articleId: Int, boolean: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                if (boolean) collectRepository.collectArticle(articleId)
                else collectRepository.unCollectArticle(articleId)
            }
        }
    }

    private fun emitArticleUiState(
        showHot: Boolean = false,
        shoeLoading: Boolean = false,
        showError: String? = null,
        showSuccess: ArticleList? = null,
        showEnd: Boolean = false,
        isRefresh: Boolean = false,
        showWebSites: List<Hot>? = null,
        showHotSearch: List<Hot>? = null
    ) {
        val uiModel = SearchUIModel(
            showHot,
            shoeLoading,
            showError,
            showSuccess,
            showEnd,
            isRefresh,
            showWebSites,
            showHotSearch
        )
        _uiState.value = uiModel
    }

    data class SearchUIModel(
        val showHot: Boolean,
        val showLoading: Boolean,
        val showError: String?,
        val showSuccess: ArticleList?,
        val showEnd: Boolean,
        val isRefresh: Boolean,
        val showWebSites: List<Hot>?,
        val showHotSearch: List<Hot>?
    )

}