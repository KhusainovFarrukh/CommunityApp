package khusainov.farrukh.communityapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.snackbar.Snackbar
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.models.User
import khusainov.farrukh.communityapp.databinding.FragmentArticlesListBinding
import khusainov.farrukh.communityapp.ui.activities.HomeActivityListener
import khusainov.farrukh.communityapp.ui.recycler.adapter.ArticleAdapter
import khusainov.farrukh.communityapp.ui.recycler.adapter.ListLoadStateAdapter
import khusainov.farrukh.communityapp.ui.recycler.adapter.TopicAdapter
import khusainov.farrukh.communityapp.utils.clicklisteners.ItemClickListener
import khusainov.farrukh.communityapp.vm.factories.LoginVMFactory
import khusainov.farrukh.communityapp.vm.factories.MainVMFactory
import khusainov.farrukh.communityapp.vm.viewmodels.LoginViewModel
import khusainov.farrukh.communityapp.vm.viewmodels.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private var _binding: FragmentArticlesListBinding? = null
    private val binding get() = _binding!!
    private var activityListener: HomeActivityListener? = null
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var topicAdapter: TopicAdapter
    private lateinit var mainViewModel: MainViewModel
    private val loginViewModel: LoginViewModel by activityViewModels {
        LoginVMFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentArticlesListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(
            this,
            MainVMFactory(requireContext())
        ).get(MainViewModel::class.java)

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
            topicAdapter = TopicAdapter(ItemClickListener(activityListener))
            articleAdapter = ArticleAdapter(ItemClickListener(activityListener))
        } else {
            throw IllegalArgumentException("$context is not HomeActivityListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        activityListener = null
    }

    private fun setObservers() {
        loginViewModel.otherError.observe(viewLifecycleOwner) { otherError ->
            (Snackbar.make(binding.root, otherError.message, Snackbar.LENGTH_LONG)
                .setAction("Retry") {
                    otherError.retry.invoke()
                }).show()
        }
        loginViewModel.responseUser.observe(viewLifecycleOwner) {
            activityListener?.saveUserId(it.id)
            setUserToViews(it)
        }
        mainViewModel.responseAllPosts.observe(viewLifecycleOwner) { responseList ->
            viewLifecycleOwner.lifecycleScope.launch {
                articleAdapter.submitData(responseList)
            }
        }
        mainViewModel.errorTopics.observe(viewLifecycleOwner) {
            binding.txvErrorTopics.text = it
        }
        viewLifecycleOwner.lifecycleScope.launch {
            articleAdapter.loadStateFlow.collectLatest { loadStates ->
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
        mainViewModel.responseTopics.observe(viewLifecycleOwner) {
                topicAdapter.submitList(it)
        }
        mainViewModel.isLoadingTopics.observe(viewLifecycleOwner) {
            binding.pbLoadingTopics.isVisible = it
            if (topicAdapter.currentList.isNullOrEmpty()) {
                binding.txvErrorTopics.isVisible = !it
                binding.btnRetryTopics.isVisible = !it
            } else {
                binding.txvErrorTopics.isVisible = false
                binding.btnRetryTopics.isVisible = false
            }
        }
        loginViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.pbLoadingLogin.isVisible = it
            binding.btnLogin.isEnabled = !it
        }
    }

    private fun setClickListeners() {
        binding.btnLogin.setOnClickListener {
            activityListener?.showLoginDialog()
        }
        binding.imvNotif.setOnClickListener {
            activityListener?.showNotificationsFragment()
        }
        binding.btnRetryArticles.setOnClickListener {
            articleAdapter.retry()
        }
        binding.btnRetryTopics.setOnClickListener {
            mainViewModel.initializeTopics()
        }
    }

    private fun setUserToViews(user: User) {
        binding.apply {
            btnLogin.visibility = Button.INVISIBLE
            imvProfile.visibility = ImageView.VISIBLE
            imvCreatePost.visibility = ImageView.VISIBLE

            imvProfile.load(user.profile.photo) {
                crossfade(true)
                placeholder(R.drawable.ic_account_circle)
                transformations(CircleCropTransformation())
            }
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            rvTopics.adapter = topicAdapter
            rvPosts.setHasFixedSize(true)
            rvPosts.adapter = articleAdapter.withLoadStateHeaderAndFooter(
                ListLoadStateAdapter { articleAdapter.retry() },
                ListLoadStateAdapter { articleAdapter.retry() }
            )
        }
    }

    private fun signInAutomatically() {
        if (loginViewModel.responseUser.value == null) {
            activityListener?.getSignInData()?.let {
                loginViewModel.signInWithEmail(it)
            }
        }
    }
}