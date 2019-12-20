package com.cf.wanzhuanandroidapp.ui.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cf.ktx_base.base.BaseViewModel
import com.cf.wanzhuanandroidapp.core.Result
import com.cf.wanzhuanandroidapp.model.bean.SystemParent
import com.cf.wanzhuanandroidapp.model.respository.ProjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectViewModel(private val projectRepository: ProjectRepository) : BaseViewModel() {

    private val _mSystemParentList: MutableLiveData<List<SystemParent>> = MutableLiveData()

    val systemData: LiveData<List<SystemParent>>
        get() = _mSystemParentList

    fun getProjectTypeList() {
        viewModelScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                projectRepository.getProjectTypeList()
            }
            if (result is Result.Success)
                _mSystemParentList.value = result.data
        }
    }

    fun getBlogType() {
        viewModelScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                projectRepository.getBlog()
            }
            if (result is Result.Success)
                _mSystemParentList.value = result.data
        }
    }

}