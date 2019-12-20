package com.cf.wanzhuanandroidapp.ui.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cf.ktx_base.base.BaseVMActivity
import com.cf.wanzhuanandroidapp.R
import com.cf.wanzhuanandroidapp.model.bean.SystemParent
import com.cf.wanzhuanandroidapp.ui.system.SystemTypeFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_project.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProjectActivity : BaseVMActivity<ProjectViewModel>() {

    companion object {
        const val BLOG_TAG = "isBlog"
    }

    private val mViewModel: ProjectViewModel by viewModel()

    private val mProjectTypeList = mutableListOf<SystemParent>()

    private val isBlog by lazy {
        intent.getBooleanExtra(BLOG_TAG, false)
    }

    override fun getLayoutResId() = R.layout.activity_project

    override fun initView() {
        projectToolbar.title = if (isBlog) "公众号" else "项目分类"
        projectToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)

        projectViewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return mProjectTypeList.size
            }

            override fun createFragment(position: Int): Fragment {
                return if (isBlog) SystemTypeFragment.newInstance(
                    mProjectTypeList[position].id,
                    true
                )
                else ProjectTypeFragment.newInstance(mProjectTypeList[position].id, false)

            }
        }
        TabLayoutMediator(tabLayout, projectViewPager) { tab, position ->
            tab.text = mProjectTypeList[position].name
        }.attach()
    }

    override fun initData() {
        projectToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        if (isBlog) mViewModel.getBlogType()
        else mViewModel.getProjectTypeList()
    }

    override fun startObserve() {
        mViewModel.systemData.observe(this, Observer {
            it?.run {
                getProjectTypeList(it)
            }
        })
    }

    private fun getProjectTypeList(projectTypeList: List<SystemParent>) {
        mProjectTypeList.clear()
        mProjectTypeList.addAll(projectTypeList)
        projectViewPager.adapter?.notifyDataSetChanged()
    }

}


