package com.cf.wanzhuanandroidapp.adapter

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.cf.wanzhuanandroidapp.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

open class BaseBindAdapter<T>(layoutResId: Int, br: Int) :
    BaseQuickAdapter<T, BaseBindAdapter.BindViewHolder>(layoutResId) {

    private val _br:Int = br

    override fun convert(helper: BindViewHolder?, item: T) {
        helper!!.binding.run {
            setVariable(_br,item)
            executePendingBindings()
        }
    }

    override fun getItemView(layoutResId: Int, parent: ViewGroup?): View {

        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(mLayoutInflater, layoutResId, parent, false)
                ?: return super.getItemView(layoutResId, parent)
        return binding.root.apply {
            //这个Id是用来做标记的
            setTag(R.id.BaseQuickAdapter_databinding_support,binding)
        }
    }

    class BindViewHolder(view: View) : BaseViewHolder(view) {
        val binding: ViewDataBinding
            get() = itemView.getTag(R.id.BaseQuickAdapter_databinding_support) as ViewDataBinding
    }
}