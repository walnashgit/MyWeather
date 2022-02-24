package test.sample.myweather.base

import kotlinx.coroutines.ExperimentalCoroutinesApi
import test.sample.myweather.TestDispatcher

@ExperimentalCoroutinesApi
abstract class BaseViewModelTest {

    protected val testDispatcher by lazy {
        TestDispatcher()
    }
}