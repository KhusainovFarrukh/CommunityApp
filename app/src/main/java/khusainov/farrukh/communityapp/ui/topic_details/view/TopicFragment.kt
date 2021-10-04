package khusainov.farrukh.communityapp.ui.topic_details.view

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.LoadState
import coil.load
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.models.Topic
import khusainov.farrukh.communityapp.databinding.FragmentTopicBinding
import khusainov.farrukh.communityapp.utils.adapters.ListLoadStateAdapter
import khusainov.farrukh.communityapp.utils.adapters.PostsOfAdapter
import khusainov.farrukh.communityapp.utils.Constants.SORT_BY_TIME_ASC
import khusainov.farrukh.communityapp.utils.Constants.SORT_BY_TIME_DESC
import khusainov.farrukh.communityapp.utils.Constants.SORT_BY_UPVOTES
import khusainov.farrukh.communityapp.utils.vm_factories.TopicVMFactory
import khusainov.farrukh.communityapp.ui.topic_details.viewmodel.TopicViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 *Created by FarrukhKhusainov on 3/22/21 11:31 PM
 **/
class TopicFragment : Fragment() {

	private var _binding: FragmentTopicBinding? = null
	private val binding get() = _binding!!
	private val navController by lazy { binding.root.findNavController() }

	private val postsOfTopicAdapter by lazy {
		PostsOfAdapter(
			{ topicId ->
				navController.navigate(TopicFragmentDirections.actionTopicFragmentSelf(
					topicId))
			},

			{ postId ->
				navController.navigate(TopicFragmentDirections.actionTopicFragmentToArticleFragment(
					postId))
			}
		)
	}

	private val topicId by lazy {
		TopicFragmentArgs.fromBundle(requireArguments()).topicId
	}

	private val topicViewModel by lazy {
		ViewModelProvider(this, TopicVMFactory(topicId, requireContext()))
			.get(TopicViewModel::class.java)
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentTopicBinding.inflate(layoutInflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initRecyclerView()
		setSpinnerListener()
		setObservers()
		setClickListeners()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	private fun initRecyclerView() = with(binding) {
		rvPosts.adapter = postsOfTopicAdapter.withLoadStateHeaderAndFooter(
			ListLoadStateAdapter { postsOfTopicAdapter.retry() },
			ListLoadStateAdapter { postsOfTopicAdapter.retry() }
		)
	}

	private fun setObservers() = with(topicViewModel) {
		//observe topic's loading state
		isLoading.observe(viewLifecycleOwner) {
			binding.pbLoadingTopic.isVisible = it
			if (topicLiveData.value == null) {
				binding.rlLoading.isVisible = true
				binding.txvErrorTopic.isVisible = !it
				binding.btnRetryTopic.isVisible = !it
			} else {
				binding.rlLoading.isVisible = it
				binding.txvErrorTopic.isVisible = false
				binding.btnRetryTopic.isVisible = false
			}
		}

		//observe topic's value
		topicLiveData.observe(viewLifecycleOwner) {
			setTopicDataToViews(it)
		}

		//observe error while initializing topic
		errorTopic.observe(viewLifecycleOwner) {
			binding.txvErrorTopic.text = it
		}

		//observe posts' loading state
		viewLifecycleOwner.lifecycleScope.launch {
			postsOfTopicAdapter.loadStateFlow.collectLatest { loadStates ->
				binding.rlLoadingPosts.isVisible =
					loadStates.refresh is LoadState.Loading || loadStates.refresh is LoadState.Error
				binding.pbLoadingPosts.isVisible = loadStates.refresh is LoadState.Loading
				binding.btnRetryArticles.isVisible = loadStates.refresh is LoadState.Error
				binding.txvErrorArticles.isVisible = loadStates.refresh is LoadState.Error

				loadStates.refresh.let {
					if (it is LoadState.Error) {
						binding.txvErrorArticles.text = it.error.message
					}
				}
			}
		}

		//observe posts' value
		topicPostsLiveData.observe(viewLifecycleOwner) {
			lifecycleScope.launch {
				postsOfTopicAdapter.submitData(it)
			}
		}
	}

	private fun setClickListeners() = with(binding) {
		btnRetryTopic.setOnClickListener {
			topicViewModel.initTopic()
		}
		btnRetryArticles.setOnClickListener {
			postsOfTopicAdapter.retry()
		}
		imvBack.setOnClickListener {
			navController.popBackStack()
		}
		//TODO set all click listeners in the fragment
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
					0 -> topicViewModel.sortPosts(SORT_BY_TIME_DESC)
					1 -> topicViewModel.sortPosts(SORT_BY_TIME_ASC)
					2 -> topicViewModel.sortPosts(SORT_BY_UPVOTES)
				}
			}

			override fun onNothingSelected(parent: AdapterView<*>?) {
			}
		}
	}

	private fun setTopicDataToViews(topic: Topic) = with(binding) {
		txvTopicTitle.text = topic.name.trim()
		txvFollowersCount.text = getString(R.string.count_follower, topic.stats.followers)
		txvPostsCount.text = getString(R.string.count_post, topic.stats.posts)
		imvTopicIcon.load(topic.picture) {
			crossfade(true)
			placeholder(R.drawable.ic_account_circle)
		}
	}
}