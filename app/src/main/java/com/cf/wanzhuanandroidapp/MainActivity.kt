package com.cf.wanzhuanandroidapp

import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.afollestad.materialdialogs.MaterialDialog
import com.cf.ktx_base.base.BaseActivity
import com.cf.wanzhuanandroidapp.adapter.FragmentAdapter
import com.cf.wanzhuanandroidapp.core.WanRetrofitClient
import com.cf.wanzhuanandroidapp.ui.BrowserNormalActivity
import com.cf.wanzhuanandroidapp.ui.about.AboutActivity
import com.cf.wanzhuanandroidapp.ui.collect.MyCollectActivity
import com.cf.wanzhuanandroidapp.ui.home.HomeFragment
import com.cf.wanzhuanandroidapp.ui.login.LoginActivity
import com.cf.wanzhuanandroidapp.ui.navigation.NavigationFragment
import com.cf.wanzhuanandroidapp.ui.project.ProjectActivity
import com.cf.wanzhuanandroidapp.ui.project.ProjectTypeFragment
import com.cf.wanzhuanandroidapp.ui.search.SearchActivity
import com.cf.wanzhuanandroidapp.ui.square.SquareFragment
import com.cf.wanzhuanandroidapp.ui.system.SystemFragment
import com.cf.wanzhuanandroidapp.util.Preference
import com.cf.wanzhuanandroidapp.util.TOOL_URL
import com.cf.wanzhuanandroidapp.view.SharedPreferencesUtils
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import luyao.util.ktx.ext.startKtxActivity

class MainActivity : BaseActivity() {

    private val titleList = arrayListOf("首页", "广场", "最新项目", "体系", "导航")

    private var isLogin = SharedPreferencesUtils.getParam(
        CFApp.CONTEXT,
        Preference.IS_LOGIN,
        false
    ) as Boolean
    private var userJson = SharedPreferencesUtils.getParam(CFApp.CONTEXT, Preference.USER_GSON, "")

    private val fragments = arrayListOf<Fragment>()
    private val homeFragment by lazy { HomeFragment() }
    private val squareFragment by lazy { SquareFragment() }
    private val projectTypeFragment by lazy { ProjectTypeFragment.newInstance(0, true) }
    private val systemFragment by lazy { SystemFragment() }
    private val navigationFragment by lazy { NavigationFragment() }

    /**
     *@作者:陈飞
     *@说明:初始化
     *@时间:2019/11/27 16:21
     */
    init {
        fragments.add(homeFragment)
        fragments.add(squareFragment)
        fragments.add(projectTypeFragment)
        fragments.add(systemFragment)
        fragments.add(navigationFragment)
    }


    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        //搜索框
        mainSearch.setOnClickListener {
            startKtxActivity<SearchActivity>()
        }
    }

    override fun initView() {
        initViewPager()
        //打开侧滑栏
        mainToolBar.setNavigationOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
        //点击侧滑栏的item
        navigationView.setNavigationItemSelectedListener(object :
            NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.nav_exit -> {
                        exit()
                    }
                    R.id.nav_blog -> startKtxActivity<ProjectActivity>(value = ProjectActivity.BLOG_TAG to true)
                    R.id.nav_project_type -> startKtxActivity<ProjectActivity>(value = ProjectActivity.BLOG_TAG to false)
                    R.id.nav_tool -> switchToTool()
                    R.id.nav_collect -> switchCollect()
                    R.id.nav_about -> startKtxActivity<AboutActivity>()
                    else -> {
                        exit()
                    }
                }
                return true
            }

        })

        navigationView.menu.findItem(R.id.nav_exit).isVisible = isLogin

        addFad.setOnClickListener {
            if (!isLogin) startKtxActivity<LoginActivity>()
        }
    }

    private fun switchCollect() {
        if (isLogin) {
            startKtxActivity<MyCollectActivity>()
        } else {
            startKtxActivity<LoginActivity>()
        }
    }

    private fun switchToTool() {
        startKtxActivity<BrowserNormalActivity>(value = BrowserNormalActivity.URL to TOOL_URL)
    }

    private fun initViewPager() {
        viewPager.offscreenPageLimit = 1
        viewPager.adapter = FragmentAdapter(this, fragments)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 1) addFad.show() else addFad.hide()
            }
        })
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = titleList[position]
        }.attach()
    }

    private fun exit(): Boolean {
        MaterialDialog(this).show {
            title(text = "退出")
            message(text = "是否确认退出登录?")
            positiveButton(text = "确认") {
                launch(Dispatchers.Default) {
                    //退出登录接口
                    WanRetrofitClient.serivices.logOut()
                }
                isLogin = false
                userJson = ""
                refreshView()
            }
            negativeButton(text = "取消")
        }
        return true
    }

    private fun refreshView() {
        navigationView.menu.findItem(R.id.nav_exit).isVisible = isLogin
        homeFragment.refresh()
        projectTypeFragment.refresh()
    }
}
