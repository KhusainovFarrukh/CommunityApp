package khusainov.farrukh.communityapp.ui.viewpager.adapter

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import khusainov.farrukh.communityapp.ui.fragments.PostsOfUserListFragment

/**
 *Created by FarrukhKhusainov on 3/17/21 11:10 PM
 **/
class FragmentPagerAdapter(
    private val userId: String,
    fragment: FragmentManager,
) : FragmentStatePagerAdapter(fragment, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var sortBy = "createdAt.desc"

    override fun getCount() = 2

    override fun getItem(position: Int) = when (position) {
        0 -> PostsOfUserListFragment().newInstance(userId, "article", sortBy)
        1 -> PostsOfUserListFragment().newInstance(userId, "response", sortBy)
        else -> throw IllegalArgumentException("There is only 2 fragments")
    }

    override fun getPageTitle(position: Int) = when (position) {
        0 -> "Articles"
        1 -> "Comments"
        else -> throw IllegalArgumentException("There are only 2 tabs")
    }

    override fun getItemPosition(`object`: Any) = POSITION_NONE
}