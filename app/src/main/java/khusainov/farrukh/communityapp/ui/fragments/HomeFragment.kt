package khusainov.farrukh.communityapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.paging.LoadState
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.snackbar.Snackbar
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.models.User
import khusainov.farrukh.communityapp.databinding.FragmentHomeBinding
import khusainov.farrukh.communityapp.ui.adapters.recycler.*
import khusainov.farrukh.communityapp.utils.comingSoon
import khusainov.farrukh.communityapp.utils.listeners.HomeActivityListener
import khusainov.farrukh.communityapp.vm.factories.LoginVMFactory
import khusainov.farrukh.communityapp.vm.factories.MainVMFactory
import khusainov.farrukh.communityapp.vm.viewmodels.LoginViewModel
import khusainov.farrukh.communityapp.vm.viewmodels.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

	private var _binding: FragmentHomeBinding? = null
	private val binding get() = _binding!!
	private var activityListener: HomeActivityListener? = null
	private val navController by lazy { Navigation.findNavController(binding.root) }

	private val articleAdapter by lazy {
		ArticleAdapter { articleId ->
			navController.navigate(HomeFragmentDirections.actionHomeFragmentToArticleFragment(
				articleId))
		}
	}

	private val topicAdapter by lazy {
		TopicAdapter { topicId ->
			navController.navigate(HomeFragmentDirections.actionHomeFragmentToTopicFragment(topicId))
		}
	}

	private val mainViewModel by lazy {
		ViewModelProvider(this, MainVMFactory(requireContext()))
			.get(MainViewModel::class.java)
	}

	private val loginViewModel: LoginViewModel by activityViewModels {
		LoginVMFactory(requireContext())
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentHomeBinding.inflate(inflater)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initRecyclerView()
		setClickListeners()
		setObservers()
		signInAutomatically()
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

	private fun setObservers() {
		with(loginViewModel) {
			//observe signing in state
			isLoading.observe(viewLifecycleOwner) {
				binding.pbLoadingLogin.isVisible = it
				binding.btnLogin.isEnabled = !it
			}

			//observe signed user
			userLiveData.observe(viewLifecycleOwner) {
				activityListener?.saveUserId(it.id)
				setUserToViews(it)
			}

			//observe error while signing in
			signInError.observe(viewLifecycleOwner) { otherError ->
				(Snackbar.make(binding.root, otherError.message, Snackbar.LENGTH_LONG)
					.setAction(getString(R.string.retry)) {
						otherError.retry.invoke()
					}).show()
			}
		}

		with(mainViewModel) {
			//observe topics' loading state
			isLoadingTopics.observe(viewLifecycleOwner) {
				binding.pbLoadingTopics.isVisible = it
				if (topicAdapter.currentList.isNullOrEmpty()) {
					binding.rlLoadingTopics.isVisible = true
					binding.txvErrorTopics.isVisible = !it
					binding.btnRetryTopics.isVisible = !it
				} else {
					binding.rlLoadingTopics.isVisible = it
					binding.txvErrorTopics.isVisible = false
					binding.btnRetryTopics.isVisible = false
				}
			}

			//observe topics' value
			topicsLiveData.observe(viewLifecycleOwner) {
				topicAdapter.submitList(it)
			}

			//observe error while initializing topics
			errorTopics.observe(viewLifecycleOwner) {
				binding.txvErrorTopics.text = it
			}

			//observe articles' value
			articlesLiveData.observe(viewLifecycleOwner) { responseList ->
				viewLifecycleOwner.lifecycleScope.launch {
					articleAdapter.submitData(responseList)
				}
			}

			//observe articles' loading state
			viewLifecycleOwner.lifecycleScope.launch {
				articleAdapter.loadStateFlow.collectLatest { loadStates ->
					binding.rlLoadingPosts.isVisible =
						loadStates.refresh is LoadState.Loading || loadStates.refresh is LoadState.Error
					binding.pbLoadingArticles.isVisible = loadStates.refresh is LoadState.Loading
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
	}

	private fun setClickListeners() = with(binding) {
		btnLogin.setOnClickListener {
			navController.navigate(HomeFragmentDirections.actionHomeFragmentToLoginDialogFragment())
		}
		imvNotif.setOnClickListener {
			navController.navigate(HomeFragmentDirections.actionHomeFragmentToNotificationsFragment())
		}
		btnRetryArticles.setOnClickListener {
			articleAdapter.retry()
		}
		btnRetryTopics.setOnClickListener {
			mainViewModel.initTopics()
		}
		imvProfile.setOnClickListener {
			activityListener?.getUserId()?.let { userId ->
				navController.navigate(HomeFragmentDirections.actionHomeFragmentToUserFragment(
					userId))
			}
		}
		imvSearch.comingSoon()
		imvCreatePost.comingSoon()
	}

	private fun setUserToViews(user: User) = with(binding) {
		btnLogin.visibility = Button.INVISIBLE
		imvProfile.visibility = ImageView.VISIBLE
		imvCreatePost.visibility = ImageView.VISIBLE

		imvProfile.load(user.profile.photo) {
			crossfade(true)
			placeholder(R.drawable.ic_account_circle)
			transformations(CircleCropTransformation())
		}
	}

	private fun initRecyclerView() = with(binding) {
		rvTopics.adapter = topicAdapter
		rvPosts.setHasFixedSize(true)
		rvPosts.adapter = articleAdapter.withLoadStateHeaderAndFooter(
			ListLoadStateAdapter { articleAdapter.retry() },
			ListLoadStateAdapter { articleAdapter.retry() }
		)
	}

	//fun to sign in user automatically if it is possible
	private fun signInAutomatically() = with(loginViewModel) {
		if (userLiveData.value == null) {
			activityListener?.getSignInData()?.let {
				signInWithEmail(it)
			}
		}
	}
}