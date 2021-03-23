package khusainov.farrukh.communityapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import khusainov.farrukh.communityapp.data.api.RetrofitInstance
import khusainov.farrukh.communityapp.data.repository.Repository
import khusainov.farrukh.communityapp.databinding.FragmentListPostsOfUserBinding
import khusainov.farrukh.communityapp.ui.activities.HomeActivityListener
import khusainov.farrukh.communityapp.ui.recycler.adapter.ArticleInUserAdapter
import khusainov.farrukh.communityapp.utils.clicklisteners.ItemClickListener
import khusainov.farrukh.communityapp.vm.factories.PostsOfUserVMFactory
import khusainov.farrukh.communityapp.vm.viewmodels.PostsOfUserViewModel

/**
 *Created by FarrukhKhusainov on 3/17/21 11:14 PM
 **/
class PostsOfUserListFragment : Fragment() {

    private var activityListener: HomeActivityListener? = null
    private var _binding: FragmentListPostsOfUserBinding? = null
    private lateinit var postsViewModel: PostsOfUserViewModel
    private lateinit var articleInUserAdapter: ArticleInUserAdapter
    private val binding get() = _binding!!

    fun newInstance(userId: String, postsType: String, sortBy: String): PostsOfUserListFragment {
        val fragment = PostsOfUserListFragment()
        val bundle = Bundle()
        bundle.putString("userId", userId)
        bundle.putString("type", postsType)
        bundle.putString("sortBy", sortBy)
        fragment.arguments = bundle
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentListPostsOfUserBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = arguments?.getString("userId")!!
        val type = arguments?.getString("type")!!
        val sortBy = arguments?.getString("sortBy")!!

        postsViewModel =
            ViewModelProvider(
                this,
                PostsOfUserVMFactory(userId,
                    type,
                    sortBy,
                    Repository(RetrofitInstance(requireContext()).communityApi))
            ).get(PostsOfUserViewModel::class.java)

        binding.rvPosts.adapter = articleInUserAdapter

        setObservers()
    }

    private fun setObservers() {
        postsViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.pbLoading.isVisible = it
        }

        postsViewModel.usersPosts.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                articleInUserAdapter.submitList(it.body()!!)
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
            articleInUserAdapter = ArticleInUserAdapter(ItemClickListener(activityListener))
        } else {
            throw IllegalArgumentException("$context is not HomeActivityListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        activityListener = null
    }
}