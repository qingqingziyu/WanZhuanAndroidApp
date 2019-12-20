package com.cf.wanzhuanandroidapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cf.ktx_base.base.BaseViewModel
import com.cf.wanzhuanandroidapp.core.Result
import com.cf.wanzhuanandroidapp.model.bean.User
import com.cf.wanzhuanandroidapp.model.respository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(val loginRepository: LoginRepository) : BaseViewModel() {
    private val _uiState = MutableLiveData<LoginUiModel>()
    val uiState: LiveData<LoginUiModel>
        get() = _uiState

    val mRegisterUser: MutableLiveData<User> = MutableLiveData()

    /**
     *@作者:陈飞
     *@说明:判断用户名和密码是否为空
     *@时间:2019/12/5 13:20
     */
    private fun isInputValid(userName: String, passWord: String) =
        userName.isNotBlank() && passWord.isNotBlank()

    fun loginDataChanged(userName: String, passWord: String) {
        emitUiState(enableLoginButton = isInputValid(userName, passWord))
    }

    /**
     *@作者:陈飞
     *@说明:登录
     *@时间:2019/12/5 13:34
     */
    fun login(userName: String, passWord: String) {
        viewModelScope.launch(Dispatchers.Default) {
            if (userName.isBlank() || passWord.isBlank()) return@launch
            withContext(Dispatchers.Main) { showLoading() }

            val result = loginRepository.register(userName, passWord)
            withContext(Dispatchers.Main) {
                if (result is Result.Success) {
                    emitUiState(showSuccess = result.data, enableLoginButton = true)
                } else if (result is Result.Error) {
                    emitUiState(showError = result.exception.message, enableLoginButton = true)
                }
            }
        }
    }

    /**
     *@作者:陈飞
     *@说明:注册
     *@时间:2019/12/5 13:37
     */
    fun register(userName: String, passWord: String) {
        viewModelScope.launch(Dispatchers.Main) {
            if (userName.isBlank() || passWord.isBlank()) return@launch

            withContext(Dispatchers.Main) { showLoading() }

            val result = loginRepository.register(userName, passWord)
            withContext(Dispatchers.Main) {
                if (result is Result.Success) {
                    emitUiState(showSuccess = result.data, enableLoginButton = true)
                } else if (result is Result.Error) {
                    emitUiState(showError = result.exception.message, enableLoginButton = true)
                }
            }
        }
    }

    private fun showLoading() {
        emitUiState(true)
    }

    private fun emitUiState(
        showProgress: Boolean = false,
        showError: String? = null,
        showSuccess: User? = null,
        enableLoginButton: Boolean = false,
        needLogin: Boolean = false
    ) {
        val uiModel =
            LoginUiModel(showProgress, showError, showSuccess, enableLoginButton, needLogin)
        _uiState.value = uiModel
    }

    data class LoginUiModel(
        val showProgress: Boolean = false,
        val showError: String? = null,
        val showSuccess: User?,
        val enableLoginButton: Boolean,
        val needLogin: Boolean
    )
}