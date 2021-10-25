package test.sample.myweather

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import test.sample.myweather.data.current.CurrentWeather
import test.sample.myweather.data.onecall.OneCall
import test.sample.myweather.databinding.MainActivityBinding
import test.sample.myweather.location.LocationUtil
import test.sample.myweather.network.ApiResult
import test.sample.myweather.ui.main.AppEvents
import test.sample.myweather.ui.main.AppState
import test.sample.myweather.ui.main.MainViewModel
import javax.inject.Inject

private const val TAG = "MainActivity"
const val LOCATION_PERMISSIONS_REQUEST_CODE = 15

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var locationUtil: LocationUtil

    lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
        binding.refresh.setOnClickListener {
            viewModel.setEvent(AppEvents.RefreshWeatherInfo)
        }
        observeAppState()
    }

    /**
     * Start observing ui states.
     */
    private fun observeAppState() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                when (it) {
                    AppState.CheckingLocationPermission -> {
                        Log.d(TAG, "Checking location Permission.")
                        binding.loading.visibility = View.VISIBLE
                        locationUtil.getCurrentLocation { locationResultEvent ->
                            viewModel.setEvent(locationResultEvent)
                        }
                    }
                    AppState.LocationPermissionGranted -> {
                        Log.d(TAG, "location Permission granted, get current location.")
                        locationUtil.getCurrentLocation { locationResultEvent ->
                            viewModel.setEvent(locationResultEvent)
                        }
                    }
                    AppState.LocationPermissionDenied -> {
                        Log.d(TAG, "Show snack bar requesting location permission.")
                        binding.loading.visibility = View.GONE
                        showSnackBarToRequestLocationPermission()
                    }
                    AppState.FetchingWeatherData -> {
                        binding.loading.visibility = View.VISIBLE
                        Log.d(TAG, "Fetching weather data.")
                    }
                    AppState.LocationNotObtained -> {
                        Log.e(TAG, "Location could not be determined.")
                    }
                    is AppState.WeatherFetchResult<*> -> {
                        binding.loading.visibility = View.GONE
                        handleWeatherData(it.apiResult)
                    }
                }
            }
        }
    }

    /**
     * Handle result of weather api.
     */
    private fun <T> handleWeatherData(result: ApiResult<T>) {
        when (result) {
            is ApiResult.Error -> {
                Log.e(TAG, "Error while getting weather data: ${result.message}")
                Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                result.message
            }
            is ApiResult.Loading -> {
                Log.d(TAG, "Loading weather data")
            }
            is ApiResult.Success -> {
                when (result.data) {
                    is CurrentWeather -> {
                        Log.d(TAG, "Weather data fetched: ${result.data.weather[0].getDescription()}")
                        binding.currentWeather = result.data
                        binding.showData = true
                    }
                    is OneCall -> {
                        Log.d(TAG, "Weather forecast data fetched: ${result.data.timezone}")
                    }
                }
            }
        }
    }

    private fun showSnackBarToRequestLocationPermission() {
        Snackbar.make(
            findViewById(R.id.main),
            R.string.permission_rationale,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(R.string.settings) {
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
            .show()
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
}