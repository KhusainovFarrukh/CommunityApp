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
import khusainov.farrukh.communityapp.data.model.Notification
import khusainov.farrukh.communityapp.data.repository.Repository
import khusainov.farrukh.communityapp.databinding.FragmentNotificationsBinding
import khusainov.farrukh.communityapp.ui.activities.HomeActivityListener
import khusainov.farrukh.communityapp.ui.recycler.adapter.NotificationAdapter
import khusainov.farrukh.communityapp.utils.clicklisteners.NotificationClickListener
import khusainov.farrukh.communityapp.vm.factories.NotificationsVMFactory
import khusainov.farrukh.communityapp.vm.viewmodels.NotificationsViewModel

class NotificationsFragment : Fragment(), NotificationClickListener {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private var activityListener: HomeActivityListener? = null
    private val notificationAdapter = NotificationAdapter(this)
    private lateinit var notificationsViewModel: NotificationsViewModel

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

        notificationsViewModel = ViewModelProvider(
            this,
            NotificationsVMFactory(Repository(RetrofitInstance(requireContext()).communityApi))
        ).get(NotificationsViewModel::class.java)

        initRecyclerView()
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
        notificationsViewModel.responseNotification.observe(viewLifecycleOwner) { responseList ->
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
        }

        notificationsViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.pbLoadingNotification.isVisible = it
        }
    }
}