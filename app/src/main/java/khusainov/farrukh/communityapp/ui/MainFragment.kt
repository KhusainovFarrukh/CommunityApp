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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import coil.transform.CircleCropTransformation
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.databinding.FragmentMainBinding
import khusainov.farrukh.communityapp.model.Notif
import khusainov.farrukh.communityapp.model.User
import khusainov.farrukh.communityapp.utils.Constants
import khusainov.farrukh.communityapp.utils.Constants.Companion.COOKIES_KEY
import khusainov.farrukh.communityapp.viewmodel.ArticleViewModel
import okhttp3.Cookie

class MainFragment : Fragment() {

    private var activityListener: HomeActivityListener? = null
    private val cookies = HashMap<String, String>()
    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val articleViewModel: ArticleViewModel by activityViewModels()

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

        setClickListeners()
        setObservers()
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
        articleViewModel.responseUser.observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {

                if (response.raw().headers(COOKIES_KEY).isNotEmpty()) {

                    response.raw().headers(COOKIES_KEY).forEach {
                        val cookie = Cookie.Companion.parse(
                            response.raw().request.url,
                            it
                        )!!
                        cookies[cookie.name] = cookie.value
                    }

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

        articleViewModel.responseNotif.observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {
                Toast.makeText(
                    context,
                    response.body()?.get(0)?.verb ?: "Something is null",
                    Toast.LENGTH_SHORT
                ).show()

                setNotifToViews(response.body()!!)

            } else {
                Toast.makeText(
                    context,
                    "Error code:" + response.code(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun setClickListeners() {
        binding.btnLogin.setOnClickListener {
            activityListener?.showLoginDialog()
        }

        binding.imvNotif.setOnClickListener {
            articleViewModel.getNotifications(
                Constants.REMEMBER_ME_KEY + "=" + cookies[Constants.REMEMBER_ME_KEY],
                Constants.SESSION_ID_KEY + "=" + cookies[Constants.SESSION_ID_KEY]
            )
        }
    }

    private fun setStatsToViews(user: User) {
//        binding.txvName.text = user.profile.name
//        binding.txvEmail.text = user.email
//        binding.txvScoreValue.text = user.profile.score.toString()
//        binding.txvArticlesValue.text = user.profile.userStats.articles.toString()
//        binding.txvLikesValue.text = user.profile.userStats.likes.toString()
//        binding.txvFollowersValue.text = user.profile.userStats.followers.toString()
        Log.wtf("Name", user.profile.name)
        Log.wtf("Email", user.email)
        Log.wtf("Score", user.profile.score.toString())
        Log.wtf("Posts", user.profile.userStats.posts.toString())
        Log.wtf("Likes", user.profile.userStats.likes.toString())
        Log.wtf("Followers", user.profile.userStats.followers.toString())

        binding.btnLogin.visibility = Button.INVISIBLE
        binding.imvProfile.visibility = ImageView.VISIBLE
        binding.imvCreatePost.visibility = ImageView.VISIBLE

        binding.imvProfile.load(user.profile.picture) {
            crossfade(true)
            placeholder(R.drawable.ic_account_circle)
            transformations(CircleCropTransformation())
        }
    }

    private fun setNotifToViews(notifList: List<Notif>) {
        notifList.forEach {
            if (!it.read) {
//                binding.txvNotif.append("${it.verb} turidagi xabar\n")
                Log.wtf("Notif", "${it.verb} turidagi xabar")
            }
        }
    }
}