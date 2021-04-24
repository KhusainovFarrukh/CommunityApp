package khusainov.farrukh.communityapp.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import khusainov.farrukh.communityapp.ui.activities.HomeActivityListener
import khusainov.farrukh.communityapp.ui.recycler.adapter.ListLoadStateAdapter
import khusainov.farrukh.communityapp.ui.recycler.adapter.PostsOfUserAdapter
import khusainov.farrukh.communityapp.utils.clicklisteners.ItemClickListener
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
    private lateinit var topicViewModel: TopicViewModel
    private lateinit var postsOfUserAdapter: PostsOfUserAdapter

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

        val topicId = arguments?.getString("topicId")
            ?: throw NullPointerException("There is no topic ID")

        topicViewModel =
            ViewModelProvider(
                this,
                TopicVMFactory(topicId, requireContext())
            ).get(TopicViewModel::class.java)

        setSpinnerListener()
        binding.rvPosts.adapter = postsOfUserAdapter.withLoadStateHeaderAndFooter(
            ListLoadStateAdapter { postsOfUserAdapter.retry() },
            ListLoadStateAdapter { postsOfUserAdapter.retry() }
        )
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
            postsOfUserAdapter = PostsOfUserAdapter(ItemClickListener(activityListener))
        } else {
            throw IllegalArgumentException("$context is not HomeActivityListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        activityListener = null
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            postsOfUserAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.pbLoadingPosts.isVisible = loadStates.refresh is LoadState.Loading
            }
        }
        topicViewModel.responseTopic.observe(viewLifecycleOwner) {
            setTopicDataToViews(it)
        }
        topicViewModel.responseTopicPosts.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                postsOfUserAdapter.submitData(it)
            }
        }
        topicViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.pbLoadingTopic.isVisible = it
            if (topicViewModel.responseTopic.value == null) {
                binding.rlLoading.isVisible = true
                binding.txvErrorTopic.isVisible = !it
                binding.btnRetryTopic.isVisible = !it
            } else {
                binding.rlLoading.isVisible = it
                binding.txvErrorTopic.isVisible = false
                binding.btnRetryTopic.isVisible = false
            }
        }
        topicViewModel.errorTopic.observe(viewLifecycleOwner) {
            binding.txvErrorTopic.text = it
        }
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
    }

    @SuppressLint("SetTextI18n")
    private fun setTopicDataToViews(topic: Topic) {
        binding.apply {
            txvTopicTitle.text = topic.name
            txvFollowersCount.text = "${topic.stats.followers} followers"
            txvPostsCount.text = "${topic.stats.posts} posts"
            imvTopicIcon.load(topic.picture) {
                crossfade(true)
                placeholder(R.drawable.ic_account_circle)
            }
        }
    }

    private fun setClickListeners() {
        binding.apply {
            btnRetryTopic.setOnClickListener {
                topicViewModel.initializeTopic()
            }
            btnRetryArticles.setOnClickListener {
                postsOfUserAdapter.retry()
            }
        }
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
                lifecycleScope.launch {
                    when (position) {
                        0 -> topicViewModel.sortPosts("createdAt.desc")
                        1 -> topicViewModel.sortPosts("createdAt.asc")
                        2 -> topicViewModel.sortPosts("upvotes")
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
}