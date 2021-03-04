package khusainov.farrukh.communityapp.utils.clicklisteners

import khusainov.farrukh.communityapp.data.model.Article

interface ArticleClickListener {
    fun onArticleClick(article: Article)
}