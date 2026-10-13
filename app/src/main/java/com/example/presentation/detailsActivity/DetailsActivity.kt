package com.example.presentation.detailsActivity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.core.constants.AppConstants
import com.example.core.utils.getParcelableExt
import com.example.data.model.NotificationItemDto
import com.example.presentation.R
import com.example.presentation.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupListeners()
        observeViewModel()

        if (savedInstanceState == null) {
            val item: NotificationItemDto? = intent.extras
                ?.getParcelableExt(AppConstants.EXTRA_NOTIFICATION)

            viewModel.processIntent(
                DetailContract.Intent.SetNotificationItem(item)
            )
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupListeners() {
        binding.btnSchedule.setOnClickListener {
            viewModel.processIntent(
                DetailContract.Intent.ScheduleNotification
            )
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        renderState(state)
                    }
                }

                launch {
                    viewModel.effect.collect { effect ->
                        handleEffect(effect)
                    }
                }
            }
        }
    }

    private fun renderState(state: DetailContract.State) {
        state.notificationItemDto?.let { item ->
            binding.tvDetailTitle.text = item.title
        }
    }

    private fun handleEffect(effect: DetailContract.Effect) {
        when (effect) {
            is DetailContract.Effect.ShowScheduleDialog -> {
                showScheduleDialog(effect.delayInSeconds)
            }

            is DetailContract.Effect.ShowToast -> {
                Toast.makeText(
                    this,
                    effect.messageResId,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is DetailContract.Effect.FinishActivity -> {
                finish()
            }
        }
    }

    private fun showScheduleDialog(delayInSeconds: Long) {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_notification_scheduled_title)
            .setMessage(
                getString(
                    R.string.dialog_notification_scheduled_msg,
                    delayInSeconds
                )
            )
            .setPositiveButton(R.string.dialog_ok, null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(
            0,
            AppConstants.MENU_CANCEL_SINGLE_ID,
            0,
            getString(R.string.menu_cancel)
        )?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                viewModel.processIntent(
                    DetailContract.Intent.NavigateBack
                )
                true
            }

            AppConstants.MENU_CANCEL_SINGLE_ID -> {
                viewModel.processIntent(
                    DetailContract.Intent.CancelNotification
                )
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}