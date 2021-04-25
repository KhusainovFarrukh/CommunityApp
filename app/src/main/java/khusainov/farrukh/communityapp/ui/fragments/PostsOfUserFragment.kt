package khusainov.farrukh.communityapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import khusainov.farrukh.communityapp.data.api.RetrofitInstance
import khusainov.farrukh.communityapp.data.repository.Repository
import khusainov.farrukh.communityapp.databinding.FragmentListPostsOfUserBinding
import khusainov.farrukh.communityapp.ui.activities.HomeActivityListener
import khusainov.farrukh.communityapp.ui.adapters.recycler.ListLoadStateAdapter
import khusainov.farrukh.communityapp.ui.adapters.recycler.PostsOfUserAdapter
import khusainov.farrukh.communityapp.utils.clicklisteners.ItemClickListener
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
	private lateinit var postsViewModel: PostsOfUserViewModel
	private lateinit var postsOfUserAdapter: PostsOfUserAdapter
	private val binding get() = _binding!!

	fun newInstance(userId: String, postsType: String, sortBy: String): PostsOfUserFragment {
		val fragment = PostsOfUserFragment()
		val bundle = Bundle()
		bundle.putString("userId", userId)
		bundle.putString("type", postsType)
		bundle.putString("sortBy", sortBy)
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

		val userId = arguments?.getString("userId")!!
		val type = arguments?.getString("type")!!
		val sortBy = arguments?.getString("sortBy")!!

		postsViewModel =
			ViewModelProvider(
                this,
                PostsOfUserVMFactory(userId,
                    type,
                    sortBy,
                    Repository(RetrofitInstance(requireContext()).communityApiService))
            ).get(PostsOfUserViewModel::class.java)

		binding.rvPosts.adapter = postsOfUserAdapter.withLoadStateHeaderAndFooter(
            ListLoadStateAdapter { postsOfUserAdapter.retry() },
            ListLoadStateAdapter { postsOfUserAdapter.retry() }
        )

		setObservers()
		setClickListeners()
	}

	private fun setObservers() {
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
		postsViewModel.userPostsLiveData.observe(viewLifecycleOwner) {
			lifecycleScope.launch {
				postsOfUserAdapter.submitData(it)
			}
		}
	}

	private fun setClickListeners() {
		binding.apply {
			btnRetry.setOnClickListener {
				postsOfUserAdapter.retry()
			}
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
			postsOfUserAdapter = PostsOfUserAdapter(ItemClickListener(activityListener))
		} else {
			throw IllegalArgumentException("$context is not HomeActivityListener")
		}
	}

	override fun onDetach() {
		super.onDetach()
		activityListener = null
	}
}