package test.sample.myweather

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import dagger.hilt.android.testing.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import test.sample.myweather.base.BaseLocationPermissionUtil
import test.sample.myweather.base.BaseLocationUtil
import test.sample.myweather.di.DispatcherModule
import test.sample.myweather.di.LocationUtilModule
import test.sample.myweather.di.WeatherRepositoryModule
import test.sample.myweather.location.FakeLocationPermissionUtil
import test.sample.myweather.location.FakeLocationUtil
import test.sample.myweather.network.FakeWeatherRepository
import test.sample.myweather.network.WeatherRepositoryOneCallBase
import test.sample.myweather.ui.hourly.HourlyDataAdapter
import test.sample.myweather.ui.main.AppState

@UninstallModules(
    DispatcherModule::class,
    WeatherRepositoryModule::class,
    LocationUtilModule::class
)
@ExperimentalCoroutinesApi
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @BindValue
    @JvmField
    val locationUtil: BaseLocationUtil = FakeLocationUtil()

    @BindValue
    @JvmField
    val locationPermissionUtil: BaseLocationPermissionUtil = FakeLocationPermissionUtil()

    @BindValue
    @JvmField
    val hourlyDataAdapter: HourlyDataAdapter = mock()

    @BindValue
    @JvmField
    val repository: WeatherRepositoryOneCallBase = FakeWeatherRepository()

    @BindValue
    @JvmField
    val dispatcher: DispatcherProvider = TestDispatcher()


    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `app launch, initialState, state- CheckGPSAndLocationPermission `() {
        val activity = ActivityScenario.launch(MainActivity::class.java)
        activity.moveToState(Lifecycle.State.CREATED)

        activity.onActivity {
            assertEquals(AppState.CheckGPSAndLocationPermission, it.viewModel.uiState.value)
        }
    }

    @Test
    fun `app launch, location permission granted, state- LocationPermissionGranted`() {
        (locationPermissionUtil as FakeLocationPermissionUtil).enableLocation = true
        locationPermissionUtil.locationPermissionGranted = true

        val activity = ActivityScenario.launch(MainActivity::class.java)
        activity.moveToState(Lifecycle.State.CREATED)

        activity.onActivity {
            assertEquals(AppState.LocationPermissionGranted, it.viewModel.uiState.value)
        }
    }
}