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
import khusainov.farrukh.communityapp.data.models.User
import khusainov.farrukh.communityapp.databinding.FragmentArticlesListBinding
import khusainov.farrukh.communityapp.ui.activities.HomeActivityListener
import khusainov.farrukh.communityapp.ui.recycler.adapter.ArticleAdapter
import khusainov.farrukh.communityapp.ui.recycler.adapter.TopicAdapter
import khusainov.farrukh.communityapp.utils.clicklisteners.ItemClickListener
import khusainov.farrukh.communityapp.vm.factories.MainVMFactory
import khusainov.farrukh.communityapp.vm.factories.LoginVMFactory
import khusainov.farrukh.communityapp.vm.viewmodels.MainViewModel
import khusainov.farrukh.communityapp.vm.viewmodels.LoginViewModel

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

        mainViewModel.responseAllPosts.observe(viewLifecycleOwner) { responseList ->
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

        mainViewModel.responseTopics.observe(viewLifecycleOwner) { responseTopics ->
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

        mainViewModel.isLoadingArticles.observe(viewLifecycleOwner) {
            binding.pbLoadingArticles.isVisible = it
        }

        mainViewModel.isLoadingTopics.observe(viewLifecycleOwner) {
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
            rvPosts.adapter = articleAdapter
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