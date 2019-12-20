package com.cf.wanzhuanandroidapp.ui.about

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cf.ktx_base.base.BaseActivity
import com.cf.wanzhuanandroidapp.R
import com.cf.wanzhuanandroidapp.util.GITHUB_PAGE
import de.psdev.licensesdialog.LicensesDialog
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20
import de.psdev.licensesdialog.model.Notice
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.title_layout.*

class AboutActivity : BaseActivity() {
    override fun initData() {
        mToolbar.setNavigationOnClickListener { onBackPressed() }
        license.setOnClickListener { shoOwnLIcense() }
    }

    private fun shoOwnLIcense() {
        val license = ApacheSoftwareLicense20()
        val notice = Notice("Coffey", GITHUB_PAGE, "", license)
        LicensesDialog.Builder(this)
            .setNotices(notice)
            .build()
            .show()
    }

    override fun initView() {
        mToolbar.title = "关于"
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_about
    }


}
