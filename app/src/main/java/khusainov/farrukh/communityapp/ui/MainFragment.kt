package khusainov.farrukh.communityapp.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import coil.transform.CircleCropTransformation
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.clicklistener.ArticleClickListener
import khusainov.farrukh.communityapp.databinding.FragmentMainBinding
import khusainov.farrukh.communityapp.model.Article
import khusainov.farrukh.communityapp.model.User
import khusainov.farrukh.communityapp.recycleradapter.ArticleAdapter
import khusainov.farrukh.communityapp.recycleradapter.TopicAdapter
import khusainov.farrukh.communityapp.utils.Constants.Companion.COOKIES_KEY
import khusainov.farrukh.communityapp.viewmodel.MainViewModel
import okhttp3.Cookie

class MainFragment : Fragment(), ArticleClickListener {

    private val articleAdapter = ArticleAdapter(this)
    private val topicAdapter = TopicAdapter()
    private var activityListener: HomeActivityListener? = null
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        setClickListeners()
        setObservers()
        if (savedInstanceState == null) {
            mainViewModel.getTopics()
            mainViewModel.getAllPosts(20, "article")
            activityListener?.getSignInData().let {
                if (it != null) {
                    mainViewModel.signIn(it)
                }
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
        }
    }

    override fun onDetach() {
        super.onDetach()
        activityListener = null
    }

    private fun setObservers() {
        mainViewModel.responseUser.observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {

                if (response.raw().headers(COOKIES_KEY).isNotEmpty()) {

                    val cookies = HashMap<String, String>()

                    response.raw().headers(COOKIES_KEY).forEach {
                        val cookie = Cookie.Companion.parse(
                            response.raw().request.url,
                            it
                        )!!
                        cookies[cookie.name] = cookie.value
                    }

                    activityListener?.saveCookies(cookies)

                } else {
                    Toast.makeText(
                        context,
                        "There is no cookies",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                setStatsToViews(response.body()!!)

            } else {
                Toast.makeText(
                    context,
                    "Error code:" + response.code(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        mainViewModel.responseAllPosts.observe(viewLifecycleOwner, { responseList ->
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

        })

        mainViewModel.responseTopics.observe(viewLifecycleOwner, { responseTopics ->
            if (responseTopics.isSuccessful) {
                if (responseTopics.body()?.isNotEmpty() == true) {
                    topicAdapter.submitList(responseTopics.body())
                    Log.wtf("submitList:", responseTopics.body()?.toString())
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

        })

        mainViewModel.isLoadingArticles.observe(viewLifecycleOwner, { isLoading ->
            binding.pbLoadingArticles.isVisible = isLoading
        })

        mainViewModel.isLoadingLogin.observe(viewLifecycleOwner, { isLoading ->
            binding.pbLoadingLogin.isVisible = isLoading
            binding.btnLogin.isEnabled = !isLoading
        })
    }

    private fun setClickListeners() {
        binding.btnLogin.setOnClickListener {
            activityListener?.showLoginDialog()
        }

        binding.imvNotif.setOnClickListener {
            activityListener?.showNotificationsFragment()
        }
    }

    private fun setStatsToViews(user: User) {

        Log.wtf("Name", user.profileInUser.name)
        Log.wtf("Email", user.email)
        Log.wtf("Score", user.profileInUser.score.toString())
        Log.wtf("Posts", user.profileInUser.statsInUser.posts.toString())
        Log.wtf("Likes", user.profileInUser.statsInUser.likes.toString())
        Log.wtf("Followers", user.profileInUser.statsInUser.followers.toString())

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
        binding.rvTopics.setHasFixedSize(true)
        binding.rvTopics.adapter = topicAdapter
        binding.rvPosts.setHasFixedSize(true)
        binding.rvPosts.adapter = articleAdapter
    }

    override fun onArticleClick(article: Article) {
        activityListener?.showArticleFragment(article)
//        activityListener?.goToBrowser(BASE_URL + article.url)
    }
}