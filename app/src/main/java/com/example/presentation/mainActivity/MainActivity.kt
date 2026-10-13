package com.example.presentation.mainActivity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.constants.AppConstants
import com.example.data.model.NotificationItemDto
import com.example.presentation.R
import com.example.presentation.databinding.ActivityMainBinding
import com.example.presentation.detailsActivity.DetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NotificationAdapter

    private val viewModel: MainViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        val messageRes = if (isGranted) {
            R.string.toast_notifications_enabled
        } else {
            R.string.toast_notifications_denied
        }

        Toast.makeText(this, messageRes, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        setupRecyclerView()
        observeViewModel()
        checkAndRequestNotificationPermission()
    }

    override fun onResume() {
        super.onResume()
        viewModel.processIntent(MainContract.Intent.LoadData)
    }

    private fun setupRecyclerView() {
        adapter = NotificationAdapter(emptyList()) { selectedItem ->
            viewModel.processIntent(
                MainContract.Intent.SelectNotification(selectedItem)
            )
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
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

    private fun renderState(state: MainContract.State) {
        binding.progressBar.isVisible = state.isLoading
        adapter.updateData(state.notifications)
    }

    private fun handleEffect(effect: MainContract.Effect) {
        when (effect) {
            is MainContract.Effect.NavigateToDetail -> {
                navigateToDetail(effect.item)
            }

            is MainContract.Effect.ShowToast -> {
                Toast.makeText(
                    this,
                    effect.messageResId,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun navigateToDetail(item: NotificationItemDto) {
        val intent = Intent(
            this,
            DetailActivity::class.java
        ).apply {
            putExtra(AppConstants.EXTRA_NOTIFICATION, item)
        }

        startActivity(intent)
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(
            0,
            AppConstants.MENU_CANCEL_ALL_ID,
            0,
            getString(R.string.menu_cancel_all)
        )?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == AppConstants.MENU_CANCEL_ALL_ID) {
            viewModel.processIntent(
                MainContract.Intent.CancelAllNotifications
            )
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}