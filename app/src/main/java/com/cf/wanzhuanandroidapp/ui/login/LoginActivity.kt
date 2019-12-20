package com.cf.wanzhuanandroidapp.ui.login

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.cf.ktx_base.base.BaseVMActivity
import com.cf.wanzhuanandroidapp.MainActivity
import com.cf.wanzhuanandroidapp.R
import com.cf.wanzhuanandroidapp.databinding.ActivityLoginBinding
import kotlinx.android.synthetic.main.activity_browser_normal.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.title_layout.*
import luyao.util.ktx.core.util.contentView
import luyao.util.ktx.ext.listener.textWatcher
import luyao.util.ktx.ext.startKtxActivity
import luyao.util.ktx.ext.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseVMActivity<LoginViewModel>() {

    private val binding by contentView<LoginActivity, ActivityLoginBinding>(R.layout.activity_login)

    private val mViewModel: LoginViewModel by viewModel()

    override fun getLayoutResId() = R.layout.activity_login

    override fun initView() {
        mToolbar.setTitle("登录")
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        binding.lifecycleOwner = this
        binding.viewModel = mViewModel
        startObserve()
    }

    override fun initData() {
        mToolbar.setNavigationOnClickListener { onBackPressed() }
        login.setOnClickListener {
            mViewModel.login(userNameEt.text.toString(), passwordEt.text.toString())
        }
        register.setOnClickListener {
            mViewModel.register(userNameEt.text.toString(), passwordEt.text.toString())
        }
        passwordEt.textWatcher {
            afterTextChanged {
                mViewModel.loginDataChanged(
                    userNameEt.text.toString(),
                    passwordEt.text.toString()
                )
            }
        }
        userNameEt.textWatcher {
            afterTextChanged {
                mViewModel.loginDataChanged(
                    userNameEt.text.toString(),
                    passwordEt.text.toString()
                )
            }
        }
    }

    override fun startObserve() {
        mViewModel.apply {
            mRegisterUser.observe(this@LoginActivity, Observer {
                it?.run {
                    mViewModel.login(username, password)
                }
            })
            uiState.observe(this@LoginActivity, Observer {
                if (it.showProgress) showProgressDialog()

                it.showSuccess?.let {
                    dismissProgressDialog()
                    startKtxActivity<MainActivity>()
                    finish()
                }
                it.showError?.let { err ->
                    dismissProgressDialog()
                    toast(err)
                }
            })
        }
    }

    private var progressDialog: ProgressDialog? = null

    private fun showProgressDialog() {
        if (progressBar == null)
            progressDialog = ProgressDialog(this)
        progressDialog?.show()
    }

    private fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }
}
