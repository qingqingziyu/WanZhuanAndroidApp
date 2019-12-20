package com.cf.wanzhuanandroidapp.ui.home

import android.graphics.drawable.LevelListDrawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.cf.ktx_base.base.BaseViewModel
import com.cf.wanzhuanandroidapp.core.Result
import com.cf.wanzhuanandroidapp.model.bean.ArticleList
import com.cf.wanzhuanandroidapp.model.bean.Banner
import com.cf.wanzhuanandroidapp.model.respository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArticleViewModel(
    private val squareRepository: SquareRepository,
    private val homeRepository: HomeRepository,
    private val projectRepository: ProjectRepository,
    private val collectRepository: CollectRepository,
    private val systemRepository: SystemRepository
) : BaseViewModel() {

    sealed class ArticleType {
        object Home : ArticleType()//首页
        object Square : ArticleType()//广场
        object LatestProject : ArticleType()//最新项目
        object ProjectDetailList : ArticleType()//项目列表
        object Collection : ArticleType() //收藏
        object SystemType : ArticleType()//体系分类
        object Blog : ArticleType() //公众号
    }

    private val _uiState = MutableLiveData<ArticleUiModel>()

    val uiState: LiveData<ArticleUiModel>
        get() = _uiState

    private var currentPage = 0

    val mBanner: LiveData<List<Banner>> = liveData {
        kotlin.runCatching {
            val data = withContext(Dispatchers.IO) {
                homeRepository.getBanners()
            }
            if (data is Result.Success) emit(data.data)
        }
    }

    fun getHomeArticleList(isRefresh: Boolean = false) = getArticleList(ArticleType.Home, isRefresh)
    fun getSquareArticleList(isRefresh: Boolean = false) =
        getArticleList(ArticleType.Square, isRefresh)

    fun getLatestProjectList(isRefresh: Boolean = false) =
        getArticleList(ArticleType.LatestProject, isRefresh)

    fun getProjectTypeDetailList(isRefresh: Boolean = false, cid: Int) =
        getArticleList(ArticleType.ProjectDetailList, isRefresh, cid)

    fun getCollectArticleList(isRefresh: Boolean = false) =
        getArticleList(ArticleType.Collection, isRefresh)

    fun getSystemTypeArticleList(isRefresh: Boolean = false, cid: Int) =
        getArticleList(ArticleType.SystemType, isRefresh, cid)

    fun getBlogArticleList(isRefresh: Boolean = false, cid: Int) =
        getArticleList(ArticleType.Blog, isRefresh, cid)

    fun collectArticle(articleId: Int, boolean: Boolean) {
        launch {
            withContext(Dispatchers.IO) {
                if (boolean) collectRepository.collectArticle(articleId)
                else collectRepository.unCollectArticle(articleId)
            }
        }
    }

    private fun getArticleList(articleType: ArticleType, isRefresh: Boolean = false, cid: Int = 0) {
        viewModelScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main) {
                emitArticleUiState(true)
            }
            if (isRefresh) currentPage = if (articleType is
                        ArticleType.ProjectDetailList
            ) 1 else 0
            val result = when (articleType) {
                ArticleType.Home -> homeRepository.getArticleList(currentPage)
                ArticleType.Square -> squareRepository.getSquareArticleList(currentPage)
                ArticleType.LatestProject -> projectRepository.getLastedProject(currentPage)
                ArticleType.ProjectDetailList -> projectRepository.getProjectTypeDetailList(
                    currentPage, cid
                )
                ArticleType.Collection -> collectRepository.getCollectArticles(currentPage)
                ArticleType.SystemType -> systemRepository.getSystemTypeDetail(cid, currentPage)
                ArticleType.Blog -> systemRepository.getBlogArticle(cid, currentPage)
            }

            withContext(Dispatchers.Main) {
                if (result is Result.Success) {
                    val articleList = result.data
                    if (articleList.offset >= articleList.total) {
                        emitArticleUiState(showLoading = false, showEnd = true)
                        return@withContext
                    }
                    currentPage++
                    emitArticleUiState(
                        showLoading = false,
                        showSuccess = articleList,
                        isRefresh = isRefresh
                    )
                } else if (result is Result.Error) {
                    emitArticleUiState(showLoading = false, showError = result.exception.message)
                }
            }
        }
    }

    private fun emitArticleUiState(
        showLoading: Boolean = false,
        showError: String? = null,
        showSuccess: ArticleList? = null,
        showEnd: Boolean = false,
        isRefresh: Boolean = false,
        needLogin: Boolean? = null
    ) {
        val uiMode = ArticleUiModel(showLoading, showError, showSuccess, showEnd, isRefresh)
        _uiState.value = uiMode
    }

    data class ArticleUiModel(
        val showLoading: Boolean,
        val showError: String?,
        val showSuccess: ArticleList?,
        val showEnd: Boolean,//加载更多
        val isRefresh: Boolean,//加载刷新
        val needLogin: Boolean? = null
    )

}