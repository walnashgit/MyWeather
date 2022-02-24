package test.sample.myweather

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import test.sample.myweather.base.BaseLocationPermissionUtil
import test.sample.myweather.base.BaseLocationUtil
import test.sample.myweather.databinding.MainActivityBinding
import test.sample.myweather.ui.hourly.HourlyDataAdapter
import test.sample.myweather.ui.main.AppEvents
import test.sample.myweather.ui.main.AppState
import test.sample.myweather.ui.main.MainViewModel
import javax.inject.Inject

private const val TAG = "MainActivity"
const val LOCATION_PERMISSIONS_REQUEST_CODE = 15

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val viewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var locationUtil: BaseLocationUtil

    @Inject
    lateinit var locationPermissionUtil: BaseLocationPermissionUtil

    @Inject
    lateinit var hourlyDataAdapter: HourlyDataAdapter

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
        binding.hourlyRecyclerView.adapter = hourlyDataAdapter
        binding.viewModel = viewModel
        observeAppState()
    }

    /**
     * Start observing ui states.
     */
    private fun observeAppState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        AppState.CheckGPSAndLocationPermission -> {
                            Log.d(TAG, "Checking GPS and location Permission.")
                            // Check for GPS.
                            if (!locationPermissionUtil.isLocationEnabled()) {
                                locationPermissionUtil.showGPSNotEnabledDialog()
                            } else {
                                binding.loading.visibility = View.VISIBLE
                                // Check for location permission.
                                if (!locationPermissionUtil.checkLocationPermission()) {
                                    locationPermissionUtil.requestLocationPermission()
                                } else {
                                    viewModel.setEvent(AppEvents.PermissionGranted)
                                }
                            }
                        }
                        AppState.LocationPermissionGranted -> {
                            Log.d(TAG, "location Permission granted, get current location.")
                            locationUtil.getCurrentLocation { locationResultEvent ->
                                viewModel.setEvent(locationResultEvent)
                            }
                        }
                        AppState.LocationPermissionDenied -> {
                            binding.loading.visibility = View.GONE
                            if (locationPermissionUtil.showRationale()) {
                                Log.d(TAG, "Request location permission again.")
                                viewModel.setEvent(AppEvents.RefreshWeatherInfo)
                            } else {
                                Log.d(TAG, "Show snack bar requesting location permission.")
                                showSnackBarToRequestLocationPermission()
                            }
                        }
                        AppState.LocationNotObtained -> {
                            Log.e(TAG, "Location could not be determined.")
                        }
                        is AppState.WeatherFetchResult<*> -> {
                            binding.loading.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun showSnackBarToRequestLocationPermission() {
        val sb = Snackbar.make(
            findViewById(R.id.main),
            R.string.permission_rationale,
            Snackbar.LENGTH_INDEFINITE
        )

        sb.setAction(R.string.settings) {
            // Build intent that displays the App settings screen.
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts(
                "package",
                BuildConfig.APPLICATION_ID,
                null
            )
            intent.data = uri
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        sb.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d(TAG, "In onRequestPermissionResult")
        when (requestCode) {
            LOCATION_PERMISSIONS_REQUEST_CODE -> {
                when {
                    grantResults.isEmpty() -> {
                        Log.d(TAG, "Permission interaction was cancelled")
                        viewModel.setEvent(AppEvents.PermissionDenied)
                        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                    }
                    grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                        viewModel.setEvent(AppEvents.PermissionGranted)
                    }
                    else -> {
                        // Permission denied
                        Log.d(TAG, "Permission denied")
                        viewModel.setEvent(AppEvents.PermissionDenied)
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (viewModel.uiState.value == AppState.LocationPermissionDenied) {
            viewModel.setEvent(AppEvents.RefreshWeatherInfo)
        }
    }

}