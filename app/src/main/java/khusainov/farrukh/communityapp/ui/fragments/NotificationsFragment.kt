package khusainov.farrukh.communityapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.google.gson.Gson
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.models.Notification
import khusainov.farrukh.communityapp.data.models.Post
import khusainov.farrukh.communityapp.databinding.FragmentNotificationsBinding
import khusainov.farrukh.communityapp.ui.adapters.recycler.ListLoadStateAdapter
import khusainov.farrukh.communityapp.ui.adapters.recycler.NotificationAdapter
import khusainov.farrukh.communityapp.utils.Constants.KEY_NOTIFICATION_FOLLOW_USER
import khusainov.farrukh.communityapp.utils.Constants.KEY_NOTIFICATION_POST
import khusainov.farrukh.communityapp.utils.Constants.KEY_NOTIFICATION_POST_UPVOTE
import khusainov.farrukh.communityapp.utils.Constants.KEY_NOTIFICATION_REPLY
import khusainov.farrukh.communityapp.utils.clicklisteners.HomeActivityListener
import khusainov.farrukh.communityapp.vm.factories.NotificationsVMFactory
import khusainov.farrukh.communityapp.vm.viewmodels.NotificationsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotificationsFragment : Fragment() {

	private var _binding: FragmentNotificationsBinding? = null
	private val binding get() = _binding!!
	private var activityListener: HomeActivityListener? = null

	private val notificationAdapter by lazy {
		NotificationAdapter { notification -> onNotificationClick(notification) }
	}

	private val notificationsViewModel by lazy {
		ViewModelProvider(this, NotificationsVMFactory(requireContext()))
			.get(NotificationsViewModel::class.java)
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentNotificationsBinding.inflate(inflater)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initRecyclerView()
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
		rvNotifications.setHasFixedSize(true)
		rvNotifications.adapter = notificationAdapter.withLoadStateHeaderAndFooter(
			ListLoadStateAdapter { notificationAdapter.retry() },
			ListLoadStateAdapter { notificationAdapter.retry() }
		)
	}

	private fun setObservers() = with(notificationsViewModel) {
		//observe notifications' loading state
		viewLifecycleOwner.lifecycleScope.launch {
			notificationAdapter.loadStateFlow.collectLatest { loadStates ->
				binding.pbLoadingNotification.isVisible = loadStates.refresh is LoadState.Loading
				binding.btnRetryNotification.isVisible = loadStates.refresh is LoadState.Error
				binding.txvErrorNotification.isVisible = loadStates.refresh is LoadState.Error

				loadStates.refresh.let {
					if (it is LoadState.Error) {
						binding.txvErrorNotification.text = it.error.message
					}
				}
			}
		}

		//observe notifications' value
		notificationsLiveData.observe(viewLifecycleOwner) {
			lifecycleScope.launch {
				notificationAdapter.submitData(it)
			}
		}
	}

	private fun setClickListeners() = with(binding) {
		btnRetryNotification.setOnClickListener {
			notificationAdapter.retry()
		}
	}

	private fun onNotificationClick(notification: Notification) {
		when (notification.verb) {

			KEY_NOTIFICATION_POST -> {
				activityListener?.showArticleDetailsFragment(notification.objects[0].id)
			}

			KEY_NOTIFICATION_POST_UPVOTE -> {
				activityListener?.showUserFragment(notification.from[0].id)
			}

			KEY_NOTIFICATION_REPLY -> {
				if (notification.objects[0].onlyParentId()) {
					activityListener?.showArticleDetailsFragment(notification.objects[0].id)
				} else {
					activityListener?.showArticleDetailsFragment(Gson().fromJson(notification.objects[0].parent,
						Post::class.java).id)
				}
			}

			KEY_NOTIFICATION_FOLLOW_USER -> {
				activityListener?.showUserFragment(notification.from[0].id)
			}

			else -> {
				Toast.makeText(
					context,
					getString(R.string.notification_other, notification.verb),
					Toast.LENGTH_SHORT
				).show()
			}
		}
	}
}