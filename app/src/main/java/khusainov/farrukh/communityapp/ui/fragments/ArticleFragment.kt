package khusainov.farrukh.communityapp.ui.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import coil.transform.CircleCropTransformation
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.model.Article
import khusainov.farrukh.communityapp.databinding.FragmentArticleBinding
import khusainov.farrukh.communityapp.ui.activities.HomeActivityListener
import khusainov.farrukh.communityapp.vm.viewmodels.MainViewModel
import org.jsoup.Jsoup

class ArticleFragment : Fragment() {

    private var activityListener: HomeActivityListener? = null
    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getString("articleId")
            ?: throw NullPointerException("There is no article ID")

        mainViewModel.getArticle(id)

        mainViewModel.responseArticle.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                setDataToViews(it.body()!!)
            } else {
                Toast.makeText(
                    requireActivity(),
                    "${it.code()}: ${it.errorBody()}",
                    Toast.LENGTH_LONG
                ).show()
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

    private fun setDataToViews(article: Article) {
        binding.apply {
            val content = article.content.replace("#", Uri.encode("#"))
            val doc = Jsoup.parse(content)

            doc.select("img").attr("width", "100%")

            wvArticle.settings.setSupportZoom(true)
            wvArticle.settings.builtInZoomControls = true
            wvArticle.settings.displayZoomControls = false

            wvArticle.settings.javaScriptEnabled = true
            wvArticle.loadData(
                doc.html(),
                "text/html",
                "UTF-8"
            )

            txvComments.text = "${article.stats.commentsCount} comments"
            txvFollowers.text = "${article.stats.followersCount} followers"
            txvLikes.text = "${article.stats.likesCount} likes"
            txvViews.text = "${article.stats.viewsCount} views"
            txvTime.text = "00 seconds ago"

            article.user?.profile?.description?.let {
                txvUserDescription.text = Html.fromHtml(it)
            }

            txvHashtags.text = ""
            article.topics.forEach {
                txvHashtags.append("#${it.name}")
                if (it != article.topics.last()) {
                    txvHashtags.append(" ")
                }
            }

            txvUserName.text = article.user?.profile?.name ?: "Unknown"

            txvTitle.text = article.title?.trim()

            imvProfilePhoto.load(article.user?.profile?.profilePhoto) {
                crossfade(true)
                placeholder(R.drawable.ic_account_circle)
                transformations(CircleCropTransformation())
            }
        }
    }
}