package khusainov.farrukh.communityapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import coil.load
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.models.Topic
import khusainov.farrukh.communityapp.databinding.FragmentTopicBinding
import khusainov.farrukh.communityapp.ui.adapters.recycler.ListLoadStateAdapter
import khusainov.farrukh.communityapp.ui.adapters.recycler.PostsOfUserAdapter
import khusainov.farrukh.communityapp.utils.Constants.KEY_TOPIC_ID
import khusainov.farrukh.communityapp.utils.Constants.SORT_BY_TIME_ASC
import khusainov.farrukh.communityapp.utils.Constants.SORT_BY_TIME_DESC
import khusainov.farrukh.communityapp.utils.Constants.SORT_BY_UPVOTES
import khusainov.farrukh.communityapp.utils.clicklisteners.HomeActivityListener
import khusainov.farrukh.communityapp.vm.factories.TopicVMFactory
import khusainov.farrukh.communityapp.vm.viewmodels.TopicViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 *Created by FarrukhKhusainov on 3/22/21 11:31 PM
 **/
class TopicFragment : Fragment() {

	private var _binding: FragmentTopicBinding? = null
	private val binding get() = _binding!!
	private var activityListener: HomeActivityListener? = null
	private val postsOfUserAdapter by lazy {
		PostsOfUserAdapter({ topicId -> activityListener?.showTopicFragment(topicId) },
			{ postId -> activityListener?.showArticleDetailsFragment(postId) })
	}

	private val topicId by lazy {
		arguments?.getString(KEY_TOPIC_ID)
			?: throw NullPointerException(getString(R.string.no_topic_id))
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

	private fun initRecyclerView() = with(binding) {
		rvPosts.adapter = postsOfUserAdapter.withLoadStateHeaderAndFooter(
			ListLoadStateAdapter { postsOfUserAdapter.retry() },
			ListLoadStateAdapter { postsOfUserAdapter.retry() }
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
			postsOfUserAdapter.loadStateFlow.collectLatest { loadStates ->
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
				postsOfUserAdapter.submitData(it)
			}
		}
	}

	private fun setClickListeners() = with(binding) {
		btnRetryTopic.setOnClickListener {
			topicViewModel.initTopic()
		}
		btnRetryArticles.setOnClickListener {
			postsOfUserAdapter.retry()
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