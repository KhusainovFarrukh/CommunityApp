package khusainov.farrukh.communityapp.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.CircleCropTransformation
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.api.RetrofitInstance
import khusainov.farrukh.communityapp.data.model.ArticleDetails
import khusainov.farrukh.communityapp.data.model.ArticleDetailsWithResponses
import khusainov.farrukh.communityapp.data.repository.Repository
import khusainov.farrukh.communityapp.databinding.FragmentArticleDetailsBinding
import khusainov.farrukh.communityapp.ui.activities.HomeActivityListener
import khusainov.farrukh.communityapp.ui.recycler.adapter.CommentAdapter
import khusainov.farrukh.communityapp.utils.Constants.Companion.KEY_ARTICLE_ID
import khusainov.farrukh.communityapp.utils.clicklisteners.CommentClickInterface
import khusainov.farrukh.communityapp.vm.factories.ArticleDetailsVMFactory
import khusainov.farrukh.communityapp.vm.viewmodels.ArticleDetailsViewModel
import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.format.ISODateTimeFormat
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*

class ArticleDetailsFragment : Fragment(), CommentClickInterface {

    private var _binding: FragmentArticleDetailsBinding? = null
    private val binding get() = _binding!!
    private var activityListener: HomeActivityListener? = null
    private lateinit var articleViewModel: ArticleDetailsViewModel
    private val commentAdapter = CommentAdapter(this)

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
            ArticleDetailsVMFactory(
                id,
                Repository(RetrofitInstance(requireContext()).communityApi)
            )
        ).get(ArticleDetailsViewModel::class.java)

        binding.rvComments.adapter = commentAdapter
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

    private fun setClickListeners() {
        binding.apply {
            txvSendComment.setOnClickListener {
                if (etComment.text.isNotEmpty()) {
                    articleViewModel.addCommentToArticle(etComment.text.toString())
                    etComment.text.clear()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Empty body",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setObservers() {
        articleViewModel.responseArticle.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                articleViewModel.isLiked(
                    activityListener?.getUserId() ?: "",
                    response.body()?.likes!!
                )
                setDataToViews(response.body()!!)
            } else {
                Toast.makeText(
                    requireActivity(),
                    "${response.code()}: ${response.errorBody()}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        articleViewModel.responseComments.observe(viewLifecycleOwner) {
            if (it.isSuccessful && it.body()!!.isNotEmpty()) {
                commentAdapter.submitList(it.body()!!.toMutableList())
                binding.txvNoComments.isVisible = false
            } else {
                binding.txvNoComments.isVisible = true
            }
        }
        articleViewModel.isLoadingArticle.observe(viewLifecycleOwner) {
            binding.rlLoading.isVisible = it
            binding.mcvSendComment.isVisible = !it
        }
        articleViewModel.isLoadingComments.observe(viewLifecycleOwner, {
            binding.rlLoadingComments.isVisible = it
        })
        articleViewModel.isLiked.observe(viewLifecycleOwner) {
            if (it) {
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
        }
    }

    //TODO remove this annotation in the future
    @SuppressLint("SetJavaScriptEnabled", "SetTextI18n")
    private fun setDataToViews(article: ArticleDetails) {
        binding.apply {
            txvLikeArticle.setOnClickListener {
                articleViewModel.likeArticleById(article.articleId)
            }
            txvShare.setOnClickListener {
                activityListener?.shareIntent(article)
            }
            txvReportArticle.setOnClickListener {
                activityListener?.showReportDialog(article.articleId)
            }
            txvSeeProfile.setOnClickListener {
                activityListener?.showUserFragment(article.user!!.userId)
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

            txvComments.text = "${article.stats.commentsCount} comments"
            txvFollowers.text = "${article.stats.followersCount} followers"
            txvLikes.text = "${article.stats.likesCount} likes"
            txvViews.text = "${article.stats.viewsCount} views"
            txvTime.text = article.getDifference()

            txvUserDescription.text = article.user?.profile?.title

            txvHashtags.text = article.getHashtags()

            txvUserName.text = article.user?.profile?.name ?: "Unknown"

            txvTitle.text = article.title?.trim()

            imvProfilePhoto.load(article.user?.profile?.profilePhoto) {
                crossfade(true)
                placeholder(R.drawable.ic_account_circle)
                transformations(CircleCropTransformation())
            }
        }
    }

    override fun onLikeCommentClick(commentId: String, isLiked: Boolean) {
        articleViewModel.likeCommentById(commentId, isLiked)
    }

    override fun onLikeSubCommentClick(commentId: String, isLiked: Boolean) {
        articleViewModel.likeSubCommentById(commentId, isLiked)
    }

    override fun onCommentAuthorClick(userId: String) {
        activityListener?.showUserFragment(userId)
    }

    override fun onWriteSubCommentClick(body: String, parentComment: ArticleDetailsWithResponses) {
        articleViewModel.addCommentToComment(body, parentComment)
    }

    override fun getUserId() = activityListener?.getUserId() ?: ""

    override fun showReportDialog(commentId: String) {
        activityListener?.showReportDialog(commentId)
    }

    override fun onDeleteCommentClick(commentId: String) {
        articleViewModel.deleteCommentById(commentId)
    }

    override fun onDeleteSubCommentClick(commentId: String) {
        articleViewModel.deleteSubCommentById(commentId)
    }
}