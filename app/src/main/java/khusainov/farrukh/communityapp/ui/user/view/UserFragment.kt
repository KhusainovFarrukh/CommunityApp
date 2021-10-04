package khusainov.farrukh.communityapp.ui.user.view

import android.os.Bundle
import android.text.Html
import android.view.*
import android.widget.AdapterView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.snackbar.Snackbar
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.models.User
import khusainov.farrukh.communityapp.databinding.FragmentUserBinding
import khusainov.farrukh.communityapp.ui.user.utils.ViewPagerAdapter
import khusainov.farrukh.communityapp.utils.Constants.SORT_BY_TIME_ASC
import khusainov.farrukh.communityapp.utils.Constants.SORT_BY_TIME_DESC
import khusainov.farrukh.communityapp.utils.Constants.SORT_BY_UPVOTES
import khusainov.farrukh.communityapp.utils.vm_factories.UserVMFactory
import khusainov.farrukh.communityapp.ui.user.viewmodel.UserViewModel

/**
 *Created by FarrukhKhusainov on 3/5/21 2:55 PM
 **/
class UserFragment : Fragment() {

	private var _binding: FragmentUserBinding? = null
	private val binding get() = _binding!!
	private val pagerAdapter by lazy { ViewPagerAdapter(userId, childFragmentManager) }

	private val userId by lazy {
		UserFragmentArgs.fromBundle(requireArguments()).userId
	}

	private val userViewModel by lazy {
		ViewModelProvider(this, UserVMFactory(userId, requireContext()))
			.get(UserViewModel::class.java)
	}

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

		initViewPagerAdapter()
		setSpinnerListener()
		setObservers()
		setClickListeners()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	private fun initViewPagerAdapter() = with(binding) {
		vpPosts.adapter = pagerAdapter
		tlPosts.setupWithViewPager(binding.vpPosts)
	}

	private fun setObservers() = with(userViewModel) {
		//observe user's loading state
		isLoading.observe(viewLifecycleOwner) {
			binding.pbLoadingUser.isVisible = it
			if (userLiveData.value == null) {
				binding.rlLoading.isVisible = true
				binding.txvErrorUser.isVisible = !it
				binding.btnRetryUser.isVisible = !it
			} else {
				binding.rlLoading.isVisible = it
				binding.btnRetryUser.isVisible = false
			}
		}

		//observe user's value
		userLiveData.observe(viewLifecycleOwner) {
			setUserDataToViews(it)
		}

		//observe error while initializing user
		errorUser.observe(viewLifecycleOwner) {
			binding.txvErrorUser.text = it
		}

		//observe other error while making some requests (follow user and etc.)
		otherError.observe(viewLifecycleOwner) { otherError ->
			(Snackbar.make(binding.root, otherError.message, Snackbar.LENGTH_LONG)
				.setAction(getString(R.string.retry)) {
					otherError.retry.invoke()
				}).show()
		}
	}

	private fun setUserDataToViews(user: User) = with(binding) {
		txvName.text = user.profile.name.trim()
		txvTitle.text = user.profile.title.trim()
		txvDescription.text = Html.fromHtml(user.profile.description.trim())
		txvLikes.text = getString(R.string.count_like, user.profile.stats.likesOfUser)
		txvFollowers.text = getString(R.string.count_follower, user.profile.stats.followers)
		txvPostsCount.text = getString(R.string.count_post, user.profile.stats.posts)
		txvReputation.text = getString(R.string.count_reputation, user.profile.score)

		if (user.isFollowed) {
			txvFollow.text = getString(R.string.unfollow)
		} else {
			txvFollow.text = getString(R.string.follow)
		}

		imvProfile.load(user.profile.photo) {
			crossfade(true)
			placeholder(R.drawable.ic_account_circle)
			transformations(CircleCropTransformation())
		}
		imvBanner.load(user.profile.banner) {
			crossfade(true)
		}

		txvFollow.setOnClickListener {
			userViewModel.followUser()
		}
	}

	private fun setClickListeners() = with(binding) {
		//TODO set all click listeners in the fragment
		btnRetryUser.setOnClickListener {
			userViewModel.initUser()
		}
		imvBack.setOnClickListener {
			binding.root.findNavController().popBackStack()
		}
	}

	private fun setSpinnerListener() = with(binding) {
		spSortBy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
			override fun onItemSelected(
				parent: AdapterView<*>?,
				view: View?,
				position: Int,
				id: Long,
			) {
				when (position) {
					0 -> pagerAdapter.sortBy = SORT_BY_TIME_DESC
					1 -> pagerAdapter.sortBy = SORT_BY_TIME_ASC
					2 -> pagerAdapter.sortBy = SORT_BY_UPVOTES
				}
				pagerAdapter.notifyDataSetChanged()
			}

			override fun onNothingSelected(parent: AdapterView<*>?) {
			}
		}
	}
}