package khusainov.farrukh.communityapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.CircleCropTransformation
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.api.RetrofitInstance
import khusainov.farrukh.communityapp.data.model.User
import khusainov.farrukh.communityapp.data.repository.Repository
import khusainov.farrukh.communityapp.databinding.FragmentArticlesListBinding
import khusainov.farrukh.communityapp.ui.activities.HomeActivityListener
import khusainov.farrukh.communityapp.ui.recycler.adapter.ArticleAdapter
import khusainov.farrukh.communityapp.ui.recycler.adapter.TopicAdapter
import khusainov.farrukh.communityapp.utils.clicklisteners.ArticleClickListener
import khusainov.farrukh.communityapp.vm.factories.ArticlesListVMFactory
import khusainov.farrukh.communityapp.vm.factories.LoginVMFactory
import khusainov.farrukh.communityapp.vm.viewmodels.ArticlesListViewModel
import khusainov.farrukh.communityapp.vm.viewmodels.LoginViewModel

class ArticlesListFragment : Fragment(), ArticleClickListener {

    private var _binding: FragmentArticlesListBinding? = null
    private val binding get() = _binding!!
    private var activityListener: HomeActivityListener? = null
    private val articleAdapter = ArticleAdapter(this)
    private val topicAdapter = TopicAdapter()
    private lateinit var articlesListViewModel: ArticlesListViewModel
    private val loginViewModel: LoginViewModel by activityViewModels {
        LoginVMFactory(Repository(RetrofitInstance(requireContext()).communityApi))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticlesListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articlesListViewModel = ViewModelProvider(
            this,
            ArticlesListVMFactory(Repository(RetrofitInstance(requireContext()).communityApi))
        ).get(ArticlesListViewModel::class.java)

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
            throw IllegalArgumentException("$context is not HomeActivityListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        activityListener = null
    }

    private fun setObservers() {
        loginViewModel.responseUser.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                activityListener?.saveUserId(response.body()!!.id)
                setUserToViews(response.body()!!)
            } else {
                Toast.makeText(
                    context,
                    "Error code:" + response.code(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        articlesListViewModel.responseAllPosts.observe(viewLifecycleOwner) { responseList ->
            if (responseList.isSuccessful) {
                if (responseList.body()?.isNotEmpty() == true) {
                    articleAdapter.submitList(responseList.body())
                } else {
                    Toast.makeText(
                        context,
                        "Not valid list",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    context,
                    "Error code: " + responseList.code(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        articlesListViewModel.responseTopics.observe(viewLifecycleOwner) { responseTopics ->
            if (responseTopics.isSuccessful) {
                if (responseTopics.body()?.isNotEmpty() == true) {
                    topicAdapter.submitList(responseTopics.body())
                } else {
                    Toast.makeText(
                        context,
                        "Not valid list",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    context,
                    "Error code: " + responseTopics.code(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        articlesListViewModel.isLoadingArticles.observe(viewLifecycleOwner) {
            binding.pbLoadingArticles.isVisible = it
        }

        articlesListViewModel.isLoadingTopics.observe(viewLifecycleOwner) {
            binding.pbLoadingTopics.isVisible = it
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
    }

    private fun setUserToViews(user: User) {

        binding.btnLogin.visibility = Button.INVISIBLE
        binding.imvProfile.visibility = ImageView.VISIBLE
        binding.imvCreatePost.visibility = ImageView.VISIBLE

        binding.imvProfile.load(user.profileInUser.picture) {
            crossfade(true)
            placeholder(R.drawable.ic_account_circle)
            transformations(CircleCropTransformation())
        }
    }

    private fun initRecyclerView() {
        binding.rvTopics.adapter = topicAdapter
        binding.rvPosts.setHasFixedSize(true)
        binding.rvPosts.adapter = articleAdapter
    }

    private fun signInAutomatically() {
        if (loginViewModel.responseUser.value == null) {
            activityListener?.getSignInData()?.let {
                loginViewModel.signInWithEmail(it)
            }
        }
    }

    override fun onArticleClick(articleId: String) {
        activityListener?.showArticleDetailsFragment(articleId)
    }
}