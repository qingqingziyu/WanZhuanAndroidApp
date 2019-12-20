package com.cf.wanzhuanandroidapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.cf.wanzhuanandroidapp.R
import com.cf.wanzhuanandroidapp.model.bean.Article
import com.cf.wanzhuanandroidapp.model.bean.Navigation
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout

class NavigationAdapter(layoutResId: Int = R.layout.item_navigation) :
    BaseQuickAdapter<Navigation, BaseViewHolder>(layoutResId) {

    override fun convert(helper: BaseViewHolder, item: Navigation?) {
        helper?.setText(R.id.navigationName, item?.name)
        helper?.getView<TagFlowLayout>(R.id.navigationTagLayout).run {
            adapter = object : TagAdapter<Article>(item?.articles) {
                override fun getCount(): Int {
                    return item?.articles!!.size
                }

                override fun getView(parent: FlowLayout?, position: Int, t: Article?): View {
                    val tv = LayoutInflater.from(parent?.context).inflate(
                        R.layout.tag,
                        parent, false
                    ) as TextView
                    tv.text = t?.title
                    return tv
                }
            }

            setOnTagClickListener { view, position, parent ->
                true
            }
        }
    }

}