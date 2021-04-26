package khusainov.farrukh.communityapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.api.RetrofitInstance
import khusainov.farrukh.communityapp.data.repository.Repository
import khusainov.farrukh.communityapp.databinding.FragmentListPostsOfUserBinding
import khusainov.farrukh.communityapp.ui.adapters.recycler.ListLoadStateAdapter
import khusainov.farrukh.communityapp.ui.adapters.recycler.PostsOfAdapter
import khusainov.farrukh.communityapp.utils.Constants.KEY_SORT_BY
import khusainov.farrukh.communityapp.utils.Constants.KEY_TYPE
import khusainov.farrukh.communityapp.utils.Constants.KEY_USER_ID
import khusainov.farrukh.communityapp.utils.listeners.HomeActivityListener
import khusainov.farrukh.communityapp.vm.factories.PostsOfUserVMFactory
import khusainov.farrukh.communityapp.vm.viewmodels.PostsOfUserViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 *Created by FarrukhKhusainov on 3/17/21 11:14 PM
 **/
class PostsOfUserFragment : Fragment() {

	private var activityListener: HomeActivityListener? = null
	private var _binding: FragmentListPostsOfUserBinding? = null
	private val binding get() = _binding!!
	private val postsOfUserAdapter by lazy {
		PostsOfAdapter({ topicId -> activityListener?.showTopicFragment(topicId) },
			{ postId -> activityListener?.showArticleDetailsFragment(postId) })
	}

	private val userId by lazy {
		arguments?.getString(KEY_USER_ID) ?: throw NullPointerException(getString(
			R.string.no_user_id))
	}
	private val type by lazy {
		arguments?.getString(KEY_TYPE) ?: throw NullPointerException(getString(
			R.string.no_type))
	}
	private val sortBy by lazy {
		arguments?.getString(KEY_SORT_BY) ?: throw NullPointerException(getString(
			R.string.no_sort_by))
	}

	private val postsViewModel by lazy {
		ViewModelProvider(this, PostsOfUserVMFactory(
			userId,
			type,
			sortBy,
			Repository(RetrofitInstance(requireContext()).communityApiService)))
			.get(PostsOfUserViewModel::class.java)
	}

	fun newInstance(userId: String, postsType: String, sortBy: String): PostsOfUserFragment {
		val fragment = PostsOfUserFragment()
		val bundle = Bundle()
		bundle.putString(KEY_USER_ID, userId)
		bundle.putString(KEY_TYPE, postsType)
		bundle.putString(KEY_SORT_BY, sortBy)
		fragment.arguments = bundle
		return fragment
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentListPostsOfUserBinding.inflate(layoutInflater)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initRecyclerView()
		setObservers()
		setClickListeners()
	}

	private fun initRecyclerView() = with(binding) {
		rvPosts.adapter = postsOfUserAdapter.withLoadStateHeaderAndFooter(
			ListLoadStateAdapter { postsOfUserAdapter.retry() },
			ListLoadStateAdapter { postsOfUserAdapter.retry() }
		)
	}

	private fun setObservers() = with(postsViewModel) {
		//observe posts' loading state
		viewLifecycleOwner.lifecycleScope.launch {
			postsOfUserAdapter.loadStateFlow.collectLatest { loadStates ->
				binding.pbLoading.isVisible = loadStates.refresh is LoadState.Loading
				binding.btnRetry.isVisible = loadStates.refresh is LoadState.Error
				binding.txvError.isVisible = loadStates.refresh is LoadState.Error

				loadStates.refresh.let {
					if (it is LoadState.Error) {
						binding.txvError.text = it.error.message
					}
				}
			}
		}

		//observe posts' value
		userPostsLiveData.observe(viewLifecycleOwner) {
			lifecycleScope.launch {
				postsOfUserAdapter.submitData(it)
			}
		}
	}

	private fun setClickListeners() = with(binding) {
		btnRetry.setOnClickListener {
			postsOfUserAdapter.retry()
		}
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
			throw IllegalArgumentException(getString(R.string.context_is_not_listener,
				context.toString()))
		}
	}

	override fun onDetach() {
		super.onDetach()
		activityListener = null
	}
}