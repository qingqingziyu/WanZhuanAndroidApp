package com.cf.wanzhuanandroidapp.adapter

import q.rorbin.verticaltablayout.adapter.TabAdapter
import q.rorbin.verticaltablayout.widget.ITabView
import javax.xml.transform.Templates

/**
 *@作者:陈飞
 *@说明:竖排导航条适配器
 *@时间:2019/12/2 16:09
 */
class VerticalTabAdapter(private val titles: List<String>) : TabAdapter {
    override fun getIcon(position: Int) = null

    override fun getBadge(position: Int) = null

    override fun getBackground(position: Int): Int {
        return -1
    }

    override fun getTitle(position: Int): ITabView.TabTitle {
        return ITabView.TabTitle.Builder()
            .setContent(titles[position])
            .setTextColor(-0xc94365, -0x8a8a8b)
            .build()
    }

    override fun getCount() = titles.size
}