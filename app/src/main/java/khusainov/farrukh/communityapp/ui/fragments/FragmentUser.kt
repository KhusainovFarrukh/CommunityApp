package khusainov.farrukh.communityapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.Html
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
import khusainov.farrukh.communityapp.data.model.User
import khusainov.farrukh.communityapp.data.repository.Repository
import khusainov.farrukh.communityapp.databinding.FragmentUserBinding
import khusainov.farrukh.communityapp.ui.activities.HomeActivityListener
import khusainov.farrukh.communityapp.vm.factories.UserVMFactory
import khusainov.farrukh.communityapp.vm.viewmodels.UserViewModel

/**
 *Created by FarrukhKhusainov on 3/5/21 2:55 PM
 **/
class FragmentUser : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private var activityListener: HomeActivityListener? = null
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = arguments?.getString("userId")
            ?: throw NullPointerException("There is no user ID")

        userViewModel =
            ViewModelProvider(
                this,
                UserVMFactory(userId, Repository(RetrofitInstance.communityApi))
            ).get(
                UserViewModel::class.java
            )

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

    private fun setObservers() {
        userViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.rlLoading.isVisible = it
        }
        userViewModel.userResponse.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                setUserDataToViews(it.body()!!)
            } else {
                Toast.makeText(
                    requireActivity(),
                    "${it.code()}: ${it.errorBody()}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setUserDataToViews(user: User) {
        binding.apply {
            txvName.text = user.profileInUser.name
            txvTitle.text = user.profileInUser.title
            txvDescription.text = Html.fromHtml(user.profileInUser.description)
            txvLikes.text = "${user.profileInUser.statsInUser.likes} likes"
            txvFollowers.text = "${user.profileInUser.statsInUser.followers} followers"
            txvComments.text = "${user.profileInUser.statsInUser.comments} comments"
            txvReputation.text = "${user.profileInUser.score} reputation"

            imvProfile.load(user.profileInUser.picture) {
                crossfade(true)
                placeholder(R.drawable.ic_account_circle)
                transformations(CircleCropTransformation())
            }
        }
    }

    private fun setClickListeners() {
        //TODO set all click listeners in the fragment
    }
}