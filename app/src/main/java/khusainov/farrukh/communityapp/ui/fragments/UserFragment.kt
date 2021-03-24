package khusainov.farrukh.communityapp.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.CircleCropTransformation
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.models.User
import khusainov.farrukh.communityapp.databinding.FragmentUserBinding
import khusainov.farrukh.communityapp.ui.activities.HomeActivityListener
import khusainov.farrukh.communityapp.ui.viewpager.adapter.ViewPagerAdapter
import khusainov.farrukh.communityapp.utils.Constants.KEY_USER_ID
import khusainov.farrukh.communityapp.vm.factories.UserVMFactory
import khusainov.farrukh.communityapp.vm.viewmodels.UserViewModel

/**
 *Created by FarrukhKhusainov on 3/5/21 2:55 PM
 **/
class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private var activityListener: HomeActivityListener? = null
    private lateinit var userViewModel: UserViewModel
    private lateinit var pagerAdapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUserBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = arguments?.getString(KEY_USER_ID)
            ?: throw NullPointerException("There is no user ID")

        userViewModel =
            ViewModelProvider(
                this,
                UserVMFactory(userId, requireContext())
            ).get(UserViewModel::class.java)

        pagerAdapter = ViewPagerAdapter(userId, childFragmentManager)
        binding.vpPosts.adapter = pagerAdapter
        binding.tlPosts.setupWithViewPager(binding.vpPosts)

        setSpinnerListener()
        setObservers()
        setClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomeActivityListener) {
            activityListener = context
        } else {
            throw IllegalArgumentException("$context is not HomeActivityListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        activityListener = null
    }

    private fun setObservers() {
        userViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.rlLoading.isVisible = it
        }
        userViewModel.responseUser.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                setUserDataToViews(it.body()!!)
            } else {
                Toast.makeText(
                    requireActivity(),
                    "${it.code()}: ${it.errorBody()}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        //TODO edit this to return User data class
        userViewModel.isFollowed.observe(viewLifecycleOwner) {
            if (it) {
                binding.txvFollow.text = "Unfollow"
            } else {
                binding.txvFollow.text = "Follow"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUserDataToViews(user: User) {
        binding.apply {
            txvName.text = user.profile.name
            txvTitle.text = user.profile.title
            txvDescription.text = Html.fromHtml(user.profile.description)
            txvLikes.text = "${user.profile.stats.likes} received likes"
            txvFollowers.text = "${user.profile.stats.followers} followers"
            txvPostsCount.text = "${user.profile.stats.posts} posts"
            txvReputation.text = "${user.profile.score} reputation"

            txvFollow.setOnClickListener {
                //TODO edit this to return User data class
                userViewModel.followUser()
            }
            imvProfile.load(user.profile.photo) {
                crossfade(true)
                placeholder(R.drawable.ic_account_circle)
                transformations(CircleCropTransformation())
            }

            imvBanner.load(user.profile.banner) {
                crossfade(true)
            }
        }
    }

    private fun setClickListeners() {
        //TODO set all click listeners in the fragment
    }

    private fun setSpinnerListener() {
        binding.spSortBy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                when (position) {
                    0 -> pagerAdapter.sortBy = "createdAt.desc"
                    1 -> pagerAdapter.sortBy = "createdAt.asc"
                    2 -> pagerAdapter.sortBy = "upvotes"
                }
                pagerAdapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
}