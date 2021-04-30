package khusainov.farrukh.communityapp.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.LoadState
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.snackbar.Snackbar
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.models.Post
import khusainov.farrukh.communityapp.databinding.FragmentArticleDetailsBinding
import khusainov.farrukh.communityapp.ui.adapters.recycler.*
import khusainov.farrukh.communityapp.utils.Constants.VALUE_DEFAULT
import khusainov.farrukh.communityapp.utils.listeners.CommentClickListener
import khusainov.farrukh.communityapp.utils.listeners.HomeActivityListener
import khusainov.farrukh.communityapp.vm.factories.ArticleVMFactory
import khusainov.farrukh.communityapp.vm.viewmodels.ArticleViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class ArticleFragment : Fragment(), CommentClickListener {

	private var _binding: FragmentArticleDetailsBinding? = null
	private val binding get() = _binding!!
	private var activityListener: HomeActivityListener? = null
	private val commentAdapter by lazy { CommentAdapter(this) }
	private val navController by lazy { binding.root.findNavController() }

	private val articleId by lazy {
		ArticleFragmentArgs.fromBundle(requireArguments()).articleId
	}

	private val articleViewModel: ArticleViewModel by lazy {
		ViewModelProvider(this, ArticleVMFactory(articleId, requireContext()))
			.get(ArticleViewModel::class.java)
	}

	private val topicOfArticleAdapter = TopicOfArticleAdapter { topicId ->
		navController.navigate(ArticleFragmentDirections.actionArticleFragmentToTopicFragment(
			topicId))
	}

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

		initRecyclerView()
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
			throw IllegalArgumentException(getString(R.string.context_is_not_listener,
				context.toString()))
		}
	}

	override fun onDetach() {
		super.onDetach()
		activityListener = null
	}

	private fun initRecyclerView() = with(binding) {
		rvHashtags.adapter = topicOfArticleAdapter
		rvComments.adapter = commentAdapter.withLoadStateHeaderAndFooter(
			ListLoadStateAdapter { commentAdapter.retry() },
			ListLoadStateAdapter { commentAdapter.retry() }
		)
	}

	private fun setClickListeners() = with(binding) {
		txvSendComment.setOnClickListener {
			if (etComment.text.isNotEmpty()) {
				lifecycleScope.launch {
					articleViewModel.addCommentTemp(etComment.text.toString()) { commentAdapter.refresh() }
				}
				etComment.text.clear()
			} else {
				Toast.makeText(
					requireContext(),
					getString(R.string.empty_text),
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

	private fun setObservers() = with(articleViewModel) {
		//observe article's loading state
		isLoading.observe(viewLifecycleOwner) {
			binding.pbLoadingArticle.isVisible = it
			if (articleLiveData.value == null) {
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

		//observe comments' loading state
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

		//observe article's value
		articleLiveData.observe(viewLifecycleOwner) {
			setDataToViews(it)
		}

		//observe comments' value
		comments.observe(viewLifecycleOwner) {
			lifecycleScope.launch {
				commentAdapter.submitData(it)
			}
		}

		//observe error while initializing article
		errorArticle.observe(viewLifecycleOwner) {
			binding.txvErrorArticle.text = it
		}

		//observe other errors while making some requests//		activityListener?.showUserFragment(userId)

		otherError.observe(viewLifecycleOwner) { otherError ->
			(Snackbar.make(binding.root, otherError.message, Snackbar.LENGTH_LONG)
				.setAction(getString(R.string.retry)) {
					otherError.retry.invoke()
				}).show()
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	private fun setDataToViews(article: Post) = with(binding) {
		setClickListenersAfterArticleInit(article)

		//set article's isLiked field to views
		if (article.isLiked) {
			txvLikeArticle.text = getString(R.string.liked)
			txvLikeArticle.setCompoundDrawablesWithIntrinsicBounds(
				R.drawable.ic_favorite,
				0,
				0,
				0
			)
		} else {
			txvLikeArticle.text = getString(R.string.like)
			txvLikeArticle.setCompoundDrawablesWithIntrinsicBounds(
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

		//set article's stats data to views
		txvComments.text = getString(R.string.count_comment, article.stats.comments)
		txvFollowers.text = getString(R.string.count_follower, article.stats.followers)
		txvLikes.text = getString(R.string.count_like, article.stats.likes)
		txvViews.text = getString(R.string.count_view, article.stats.viewsCount)
		txvTime.text = article.getDifference()

		//set author data to views
		txvUserDescription.text = article.user?.profile?.title?.trim()
		txvUserName.text =
			article.user?.profile?.name?.trim() ?: getString(R.string.unknown_author)
		imvProfilePhoto.load(article.user?.profile?.photo) {
			crossfade(true)
			placeholder(R.drawable.ic_account_circle)
			transformations(CircleCropTransformation())
		}

		//set article title to views
		txvTitle.text = article.title?.trim()

		//submit topics of article to adapter
		topicOfArticleAdapter.submitList(article.topics)
	}

	//fun to set some ClickListeners after article initialize
	private fun setClickListenersAfterArticleInit(article: Post) = with(binding) {
		txvLikeArticle.setOnClickListener {
			articleViewModel.likeArticle()
		}
		txvShare.setOnClickListener {
			activityListener?.shareIntent(article)
		}
		txvReportArticle.setOnClickListener {
			navController.navigate(ArticleFragmentDirections.actionArticleFragmentToReportDialogFragment(article.id))
		}
		txvSeeProfile.setOnClickListener {
			navController.navigate(ArticleFragmentDirections.actionArticleFragmentToUserFragment(
				article.user!!.id))
		}
	}

	override fun onLikeCommentClick(comment: Post) {
		articleViewModel.likeCommentTemp(comment) { commentAdapter.refresh() }
	}

	override fun onCommentAuthorClick(userId: String) {
		navController.navigate(ArticleFragmentDirections.actionArticleFragmentToUserFragment(userId))
	}

	override fun onReplyClick(body: String, replyTo: String) {
		articleViewModel.replyCommentTemp(body, replyTo) { commentAdapter.refresh() }
	}

	override fun onReportClick(commentId: String) {
		navController.navigate(ArticleFragmentDirections.actionArticleFragmentToReportDialogFragment(commentId))
	}

	override fun onDeleteCommentClick(commentId: String) {
		articleViewModel.deleteCommentTemp(commentId) { commentAdapter.refresh() }
	}

	override fun getUserId() = activityListener?.getUserId() ?: VALUE_DEFAULT
}