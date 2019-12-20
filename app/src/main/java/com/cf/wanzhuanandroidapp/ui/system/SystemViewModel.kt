package com.cf.wanzhuanandroidapp.ui.system

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cf.ktx_base.base.BaseViewModel
import com.cf.wanzhuanandroidapp.core.Result
import com.cf.wanzhuanandroidapp.model.bean.Article
import com.cf.wanzhuanandroidapp.model.bean.SystemParent
import com.cf.wanzhuanandroidapp.model.respository.CollectRepository
import com.cf.wanzhuanandroidapp.model.respository.SystemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SystemViewModel(
    private val systemRepository: SystemRepository,
    private val collectRepository: CollectRepository
) : BaseViewModel() {
    private val _MSystemParentList: MutableLiveData<SystemUiModel> = MutableLiveData()

    val uiState: LiveData<SystemUiModel>
        get() = _MSystemParentList

    fun getSystemTypes() {
        viewModelScope.launch(Dispatchers.Main) {
            emitArticleUIState(showLoading = true)
            val result = withContext(Dispatchers.IO) {
                systemRepository.getSystemTypes()
            }
            if (result is Result.Success)
                emitArticleUIState(showLoading = false, showSuccess = result.data)
            else if (result is Result.Error)
                emitArticleUIState(showLoading = false, showError = result.exception.message)
        }
    }

    fun collectArticle(articleId: Int, boolean: Boolean) {
        launch {
            withContext(Dispatchers.IO) {
                if (boolean) collectRepository.collectArticle(articleId)
                else collectRepository.unCollectArticle(articleId)
            }
        }
    }

    private fun emitArticleUIState(
        showLoading: Boolean = false,
        showError: String? = null,
        showSuccess: List<SystemParent>? = null
    ) {
        val uiModel = SystemUiModel(showLoading, showError, showSuccess)
        _MSystemParentList.value = uiModel
    }

    class SystemUiModel(
        showLoading: Boolean, showError: String?,
        showSuccess: List<SystemParent>?
    ) : BaseUiModel<List<SystemParent>>(showLoading, showError, showSuccess)

    open class BaseUiModel<T>(
        val showLoading: Boolean = false,
        val showError: String? = null,
        val showSuccess: T? = null,
        val showEnd: Boolean = false,//加载更多
        val isRefresh: Boolean = false//刷新
    )

}