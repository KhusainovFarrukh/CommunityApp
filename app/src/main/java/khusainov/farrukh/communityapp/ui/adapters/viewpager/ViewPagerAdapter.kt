package khusainov.farrukh.communityapp.ui.adapters.viewpager

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import khusainov.farrukh.communityapp.ui.fragments.PostsOfUserFragment
import khusainov.farrukh.communityapp.utils.Constants.KEY_ARTICLE
import khusainov.farrukh.communityapp.utils.Constants.KEY_RESPONSE
import khusainov.farrukh.communityapp.utils.Constants.SORT_BY_TIME_DESC
import khusainov.farrukh.communityapp.utils.Constants.TITLE_ARTICLES
import khusainov.farrukh.communityapp.utils.Constants.TITLE_COMMENTS

/**
 *Created by FarrukhKhusainov on 3/17/21 11:10 PM
 **/
class ViewPagerAdapter(
    private val userId: String,
    fragment: FragmentManager,
) : FragmentStatePagerAdapter(fragment, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var sortBy = SORT_BY_TIME_DESC

    override fun getCount() = 2

    override fun getItem(position: Int) = when (position) {
        0 -> PostsOfUserFragment().newInstance(userId, KEY_ARTICLE, sortBy)
        1 -> PostsOfUserFragment().newInstance(userId, KEY_RESPONSE, sortBy)
        else -> throw IllegalArgumentException("There is only 2 fragments")
    }

    override fun getPageTitle(position: Int) = when (position) {
        0 -> TITLE_ARTICLES
        1 -> TITLE_COMMENTS
        else -> throw IllegalArgumentException("There are only 2 tabs")
    }

    override fun getItemPosition(`object`: Any) = POSITION_NONE
}