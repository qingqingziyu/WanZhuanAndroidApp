package com.cf.wanzhuanandroidapp.ui

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.cf.ktx_base.base.BaseActivity
import com.cf.wanzhuanandroidapp.R
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import kotlinx.android.synthetic.main.activity_browser_normal.*
import kotlinx.android.synthetic.main.title_layout.*

/**
 *@作者:陈飞
 *@说明:web浏览器
 *@时间:2019/12/3 13:46
 */
class BrowserNormalActivity : BaseActivity() {

    companion object {
        const val URL = "url"
    }

    override fun initData() {
        mToolbar.title = "正在加载..."
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        initWebView()
    }

    private fun initWebView() {
        progressBar.progressDrawable = this.resources.getDrawable(R.drawable.color_progressbar)

        webView.run {
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    progressBar.visibility = View.VISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    progressBar.visibility = View.GONE
                }
            }
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(p0: WebView?, p1: Int) {
                    super.onProgressChanged(p0, p1)
                    progressBar.progress = p1
                    Log.e("browser", p1.toString())
                }

                override fun onReceivedTitle(p0: WebView?, p1: String?) {
                    super.onReceivedTitle(p0, p1)
                    p1?.let { mToolbar.title = p1 }
                }
            }
        }
    }

    override fun initView() {
        mToolbar.setNavigationOnClickListener { onBackPressed() }
        intent?.extras?.getString(URL).let {
            webView.loadUrl(it)
        }
    }

    override fun getLayoutResId() = R.layout.activity_browser_normal

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack() else super.onBackPressed()

    }
}
