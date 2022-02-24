package test.sample.myweather.ui.main

import android.location.Location
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import test.sample.myweather.base.BaseViewModelTest
import test.sample.myweather.network.ApiResult
import test.sample.myweather.network.ERROR_MSG
import test.sample.myweather.network.FakeWeatherRepository

@ExperimentalCoroutinesApi
class MainViewModelTest: BaseViewModelTest() {

    lateinit var viewModel: MainViewModel
    lateinit var fakeRepo: FakeWeatherRepository

    @Before
    fun setUp() {
        fakeRepo = FakeWeatherRepository()
        viewModel = MainViewModel(fakeRepo, testDispatcher)
    }

    @Test
    fun `test handleEvent - initial state`()  {
        assertEquals(viewModel.createInitialState(), viewModel.uiState.value)
    }

    @Test
    fun `test handleEvent - PermissionGranted`() = runBlocking {
        viewModel.setEvent(AppEvents.PermissionGranted)
        assertEquals(AppState.LocationPermissionGranted, viewModel.uiState.value)
    }

    @Test
    fun `test handleEvent - PermissionDenied`() = runBlocking {
        viewModel.setEvent(AppEvents.PermissionDenied)
        assertEquals(AppState.LocationPermissionDenied, viewModel.uiState.value)
    }

    @Test
    fun `test handleEvent - LocationFetchFailed`() = runBlocking {
        assertEquals("", viewModel.errorMessage.value)
        viewModel.setEvent(AppEvents.LocationFetchFailed)
        assertEquals(AppState.LocationNotObtained, viewModel.uiState.value)
        assertEquals("Location could not be determined.", viewModel.errorMessage.value)
    }

    @Test
    fun `test handleEvent - LocationFetched, API result - success`() = runBlocking {
        val loc: Location = mock()
        loc.latitude = 1.1
        loc.longitude = 2.2
        assertEquals("", viewModel.locationName.value)
        viewModel.setEvent(AppEvents.LocationFetched(LocationModel(loc, "Berlin")))
        assertEquals("Berlin", viewModel.locationName.value)
        assertEquals(AppState.WeatherFetchResult(ApiResult.Success(null)), viewModel.uiState.value)
    }

    @Test
    fun `test handleEvent - LocationFetched, API result - Error`() = runBlocking {
        val loc: Location = mock()
        loc.latitude = 1.1
        loc.longitude = 2.2
        fakeRepo.returnError = true
        viewModel.setEvent(AppEvents.LocationFetched(LocationModel(loc, "Munich")))
        assertEquals("Munich", viewModel.locationName.value)
        assertEquals(AppState.WeatherFetchResult(ApiResult.Error(null, ERROR_MSG)), viewModel.uiState.value)
    }

    @Test
    fun `test handleEvent - RefreshWeatherInfo`() = runBlocking {
        `test handleEvent - LocationFetched, API result - success`()

        viewModel.setEvent(AppEvents.RefreshWeatherInfo)
        assertEquals(viewModel.createInitialState(), viewModel.uiState.value)
    }
}