package com.cf.wanzhuanandroidapp.ui.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cf.ktx_base.base.BaseViewModel
import com.cf.wanzhuanandroidapp.core.Result
import com.cf.wanzhuanandroidapp.model.bean.Navigation
import com.cf.wanzhuanandroidapp.model.respository.NavigationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NavigationViewModel(private val navigationRepository: NavigationRepository) :
    BaseViewModel() {
    private val _navigationList: MutableLiveData<List<Navigation>> = MutableLiveData()

    val navigationListState: LiveData<List<Navigation>>
        get() = _navigationList

    fun getNavigation() {
        viewModelScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                navigationRepository.getNavigation()
            }
            if (result is Result.Success) {
                _navigationList.value = result.data
            }
        }
    }
}