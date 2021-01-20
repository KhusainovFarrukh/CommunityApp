package khusainov.farrukh.communityapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import khusainov.farrukh.communityapp.clicklistener.NotificationClickListener
import khusainov.farrukh.communityapp.databinding.FragmentNotificationsBinding
import khusainov.farrukh.communityapp.model.Notification
import khusainov.farrukh.communityapp.recycleradapter.NotificationAdapter
import khusainov.farrukh.communityapp.utils.Constants
import khusainov.farrukh.communityapp.viewmodel.MainViewModel

class NotificationsFragment : Fragment(), NotificationClickListener {

    private val notificationAdapter = NotificationAdapter(this)
    private var activityListener: HomeActivityListener? = null
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cookies = activityListener?.getCookies()!!

        initRecyclerView()
        setObservers()
        if (savedInstanceState == null) {
            mainViewModel.getNotifications(
                Constants.REMEMBER_ME_KEY + "=" + cookies[Constants.REMEMBER_ME_KEY],
                Constants.SESSION_ID_KEY + "=" + cookies[Constants.SESSION_ID_KEY]
            )
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

    override fun onNotificationClick(notification: Notification) {
        Toast.makeText(
            context,
            "NotificationID: ${notification.notificationId}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun initRecyclerView() {
        binding.rvNotifications.setHasFixedSize(true)
        binding.rvNotifications.adapter = notificationAdapter
    }

    private fun setObservers() {
        mainViewModel.responseNotification.observe(viewLifecycleOwner, { responseList ->
            if (responseList.isSuccessful) {
                if (responseList.body()?.isNotEmpty() == true) {
                    notificationAdapter.submitList(responseList.body())
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

        mainViewModel.isLoadingNotifications.observe(viewLifecycleOwner, {
            binding.pbLoadingNotification.isVisible = it
        })
    }
}