package test.sample.myweather.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import test.sample.myweather.DispatcherProvider

abstract class BaseViewModel<AppState : State, AppEvents : Event>(private val dispatcher: DispatcherProvider) :
    ViewModel() {

    private val initialState: AppState by lazy {
        createInitialState()
    }

    /**
     * Create the initial state of the app.
     */
    abstract fun createInitialState(): AppState

    private val _uiState: MutableStateFlow<AppState> = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<AppEvents> = MutableSharedFlow()
    private val event = _event.asSharedFlow()

    init {
        subscribeEvent()
    }

    private fun subscribeEvent() {
        viewModelScope.launch(dispatcher.main) {
            event.collect {
                handleEvent(it)
            }
        }
    }

    /**
     * Handle app events
     */
    abstract fun handleEvent(events: AppEvents)

    /**
     * Set the events [AppEvents] that the app must handle.
     */
    fun setEvent(newEvent: AppEvents) {
        viewModelScope.launch(dispatcher.main) {
            _event.emit(newEvent)
        }
    }

    /**
     * Set the state of the App
     */
    protected fun setAppState(newState: AppState) {
        _uiState.value = newState
    }
}