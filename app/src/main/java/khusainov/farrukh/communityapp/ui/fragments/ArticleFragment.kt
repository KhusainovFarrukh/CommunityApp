package khusainov.farrukh.communityapp.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.snackbar.Snackbar
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.models.Post
import khusainov.farrukh.communityapp.databinding.FragmentArticleDetailsBinding
import khusainov.farrukh.communityapp.ui.activities.HomeActivityListener
import khusainov.farrukh.communityapp.ui.adapters.recycler.CommentAdapter
import khusainov.farrukh.communityapp.ui.adapters.recycler.HashTagAdapter
import khusainov.farrukh.communityapp.ui.adapters.recycler.ListLoadStateAdapter
import khusainov.farrukh.communityapp.utils.Constants.KEY_ARTICLE_ID
import khusainov.farrukh.communityapp.utils.clicklisteners.CommentClickListener
import khusainov.farrukh.communityapp.utils.clicklisteners.ItemClickListener
import khusainov.farrukh.communityapp.vm.factories.ArticleVMFactory
import khusainov.farrukh.communityapp.vm.viewmodels.ArticleViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class ArticleFragment : Fragment(), CommentClickListener {

    private var _binding: FragmentArticleDetailsBinding? = null
    private val binding get() = _binding!!
    private var activityListener: HomeActivityListener? = null
    private lateinit var articleViewModel: ArticleViewModel
    private val commentAdapter = CommentAdapter(this)
    private lateinit var hashTagAdapter: HashTagAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentArticleDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getString(KEY_ARTICLE_ID)
            ?: throw NullPointerException("There is no article ID")

        articleViewModel = ViewModelProvider(
            this,
            ArticleVMFactory(id, requireContext())
        ).get(ArticleViewModel::class.java)

        setRecyclerAdapters()
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
            hashTagAdapter = HashTagAdapter(ItemClickListener(activityListener))
        } else {
            throw IllegalArgumentException("$context is not HomeActivityListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        activityListener = null
    }

    private fun setClickListeners() {
        binding.apply {
            txvSendComment.setOnClickListener {
                if (etComment.text.isNotEmpty()) {
                    lifecycleScope.launch {
                        articleViewModel.addCommentTemp(etComment.text.toString()) { commentAdapter.refresh() }
                    }
                    etComment.text.clear()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Empty body",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            btnRetryComments.setOnClickListener {
                commentAdapter.retry()
            }
            btnRetryArticle.setOnClickListener {
                articleViewModel.initArticle()
            }
        }
    }

    private fun setObservers() {
        articleViewModel.otherError.observe(viewLifecycleOwner) { otherError ->
            (Snackbar.make(binding.root, otherError.message, Snackbar.LENGTH_LONG)
                .setAction("Retry") {
                    otherError.retry.invoke()
                }).show()
        }
        articleViewModel.articleLiveData.observe(viewLifecycleOwner) {
            setDataToViews(it)
        }
        articleViewModel.comments.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                commentAdapter.submitData(it)
            }
        }
        articleViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.pbLoadingArticle.isVisible = it
            if (hashTagAdapter.currentList.isNullOrEmpty()) {
                binding.rlLoading.isVisible = true
                binding.mcvSendComment.isVisible = false
                binding.txvErrorArticle.isVisible = !it
                binding.btnRetryArticle.isVisible = !it
            } else {
                binding.rlLoading.isVisible = it
                binding.mcvSendComment.isVisible = !it
                binding.txvErrorArticle.isVisible = false
                binding.btnRetryArticle.isVisible = false
            }
        }
        articleViewModel.errorArticle.observe(viewLifecycleOwner) {
            binding.txvErrorArticle.text = it
        }
        viewLifecycleOwner.lifecycleScope.launch {
            commentAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.rlLoadingComments.isVisible =
                    loadStates.refresh is LoadState.Loading || loadStates.refresh is LoadState.Error
                binding.pbLoadingComments.isVisible = loadStates.refresh is LoadState.Loading
                binding.btnRetryComments.isVisible = loadStates.refresh is LoadState.Error
                binding.txvErrorComments.isVisible = loadStates.refresh is LoadState.Error

                loadStates.refresh.let {
                    if (it is LoadState.Error) {
                        binding.txvErrorComments.text = it.error.message
                    }
                }
            }
        }
    }

    private fun setRecyclerAdapters() {
        binding.rvHashtags.adapter = hashTagAdapter
        binding.rvComments.adapter = commentAdapter.withLoadStateHeaderAndFooter(
            ListLoadStateAdapter { commentAdapter.retry() },
            ListLoadStateAdapter { commentAdapter.retry() }
        )
    }

    //TODO remove this annotation in the future
    @SuppressLint("SetJavaScriptEnabled", "SetTextI18n")
    private fun setDataToViews(article: Post) {
        binding.apply {
            txvLikeArticle.setOnClickListener {
                articleViewModel.likeArticle()
            }
            txvShare.setOnClickListener {
                activityListener?.shareIntent(article)
            }
            txvReportArticle.setOnClickListener {
                activityListener?.showReportDialog(article.id)
            }
            txvSeeProfile.setOnClickListener {
                activityListener?.showUserFragment(article.user!!.id)
            }
            if (article.isLiked) {
                binding.txvLikeArticle.text = "Liked already"
                binding.txvLikeArticle.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_favorite,
                    0,
                    0,
                    0
                )
            } else {
                binding.txvLikeArticle.text = "Like"
                binding.txvLikeArticle.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_favorite_border,
                    0,
                    0,
                    0
                )
            }

            //Replacing '#' from encoded version. Because there is a bug with WebView
            val content = article.content
                .replace("#", Uri.encode("#"))

            val html = Jsoup.parse(content)

            //Changing some html and css attributes of image to make it match display width
            html.select("img")
                .attr("width", "100%")
            html.body()
                .appendElement("style")
                .appendText("* {margin-top:4px; margin-bottom:4px; margin-left:0; margin-right:0; padding:0; } ")

            wvArticle.settings.setSupportZoom(true)
            wvArticle.settings.builtInZoomControls = true
            wvArticle.settings.displayZoomControls = false

            wvArticle.settings.javaScriptEnabled = true
            wvArticle.loadData(
                html.html(),
                "text/html",
                "UTF-8"
            )

            txvComments.text = "${article.stats.comments} comments"
            txvFollowers.text = "${article.stats.followers} followers"
            txvLikes.text = "${article.stats.likes} likes"
            txvViews.text = "${article.stats.viewsCount} views"
            txvTime.text = article.getDifference()

            txvUserDescription.text = article.user?.profile?.title

            hashTagAdapter.submitList(article.topics)

            txvUserName.text = article.user?.profile?.name ?: "Unknown"

            txvTitle.text = article.title?.trim()

            imvProfilePhoto.load(article.user?.profile?.photo) {
                crossfade(true)
                placeholder(R.drawable.ic_account_circle)
                transformations(CircleCropTransformation())
            }
        }
    }

    override fun onLikeCommentClick(comment: Post) {
        articleViewModel.likeCommentTemp(comment) { commentAdapter.refresh() }
    }

    override fun onLikeSubCommentClick(comment: Post) {
        articleViewModel.likeCommentTemp(comment) { commentAdapter.refresh() }
    }

    override fun onCommentAuthorClick(userId: String) {
        activityListener?.showUserFragment(userId)
    }

    override fun onWriteSubCommentClick(body: String, replyTo: String) {
        articleViewModel.replyCommentTemp(body, replyTo) { commentAdapter.refresh() }
    }

    override fun getUserId() = activityListener?.getUserId() ?: ""

    override fun showReportDialog(commentId: String) {
        activityListener?.showReportDialog(commentId)
    }

    override fun onDeleteCommentClick(commentId: String) {
        articleViewModel.deleteCommentTemp(commentId) { commentAdapter.refresh() }
    }

    override fun onDeleteSubCommentClick(commentId: String) {
        articleViewModel.deleteCommentTemp(commentId) { commentAdapter.refresh() }
    }
}