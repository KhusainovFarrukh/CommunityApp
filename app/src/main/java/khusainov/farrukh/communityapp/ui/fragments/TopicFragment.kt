package khusainov.farrukh.communityapp.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import khusainov.farrukh.communityapp.data.api.RetrofitInstance
import khusainov.farrukh.communityapp.data.model.Topic
import khusainov.farrukh.communityapp.data.repository.Repository
import khusainov.farrukh.communityapp.databinding.FragmentTopicBinding
import khusainov.farrukh.communityapp.ui.activities.HomeActivityListener
import khusainov.farrukh.communityapp.ui.recycler.adapter.ArticleInUserAdapter
import khusainov.farrukh.communityapp.utils.clicklisteners.ArticleClickListener
import khusainov.farrukh.communityapp.utils.clicklisteners.TopicClickListener
import khusainov.farrukh.communityapp.vm.factories.TopicVMFactory
import khusainov.farrukh.communityapp.vm.viewmodels.TopicViewModel

/**
 *Created by FarrukhKhusainov on 3/22/21 11:31 PM
 **/
class TopicFragment : Fragment(), ArticleClickListener, TopicClickListener {

    private var _binding: FragmentTopicBinding? = null
    private val binding get() = _binding!!
    private var activityListener: HomeActivityListener? = null
    private lateinit var topicViewModel: TopicViewModel
    val adapter = ArticleInUserAdapter(this, this)

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
                TopicVMFactory(topicId, Repository(RetrofitInstance(requireContext()).communityApi))
            ).get(TopicViewModel::class.java)

        binding.spSortBy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                when (position) {
                    0 -> topicViewModel.getTopicPosts("createdAt.desc")
                    1 -> topicViewModel.getTopicPosts("createdAt.asc")
                    2 -> topicViewModel.getTopicPosts("upvotes")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.rvPosts.adapter = adapter

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
        topicViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.rlLoading.isVisible = it
        }
        topicViewModel.isLoadingPosts.observe(viewLifecycleOwner) {
            binding.pbLoadingPosts.isVisible = it
        }
        topicViewModel.responseTopic.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                setTopicDataToViews(it.body()!!)
            } else {
                Toast.makeText(
                    requireActivity(),
                    "${it.code()}: ${it.errorBody()}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        topicViewModel.responseTopicPosts.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                adapter.submitList(it.body()!!)
            } else {
                Toast.makeText(
                    requireActivity(),
                    "${it.code()}: ${it.errorBody()}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTopicDataToViews(topic: Topic) {
        binding.apply {
            txvTopicTitle.text = topic.name
            txvFollowersCount.text = topic.stats.followers.toString()
            txvPostsCount.text = topic.stats.posts.toString()
        }
    }

    private fun setClickListeners() {
        //TODO set all click listeners in the fragment
    }

    override fun onArticleClick(articleId: String) {
        activityListener?.showArticleDetailsFragment(articleId)
    }

    override fun onTopicClick(topicId: String) {
        activityListener?.showTopicFragment(topicId)
    }
}