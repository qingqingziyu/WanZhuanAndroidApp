package com.cf.wanzhuanandroidapp.adapter

import android.text.Html
import android.widget.TextView
import com.cf.ktx_base.ext.fromO
import com.cf.wanzhuanandroidapp.BR
import com.cf.wanzhuanandroidapp.R
import com.cf.wanzhuanandroidapp.model.bean.Article

class HomeArticleAdapter(layouResId: Int = R.layout.item_article) :
    BaseBindAdapter<Article>(layouResId, BR.article) {
    private var showStar = true

    fun showStar(showStar: Boolean) {
        this.showStar = showStar
    }

    override fun convert(helper: BindViewHolder?, item: Article) {
        super.convert(helper, item)
        helper!!.addOnClickListener(R.id.articleStar)
        helper!!.addOnClickListener(R.id.main_item_controll)

        if (showStar) helper.setImageResource(
            R.id.articleStar,
            if (item.collect) R.mipmap.timeline_like_pressed else R.mipmap.timeline_like_normal
        ) else {
            helper.setVisible(R.id.articleStar, false)
        }
        helper.setText(
            R.id.articleAuthor,
            if (item.author.isBlank()) "分享者：${item.shareUser}" else item.author
        )
    }
}