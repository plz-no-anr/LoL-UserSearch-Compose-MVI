package com.plz.no.anr.lol_usersearch_compose.ui.base

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plz.no.anr.lol_usersearch_compose.utils.NetworkManager
import com.plz.no.anr.lol_usersearch_compose.utils.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


abstract class BaseViewModel<UiState : BaseContract.ViewState, Event : BaseContract.ViewEvent, Effect : BaseContract.ViewSideEffect>
    : ViewModel() {

    abstract fun setInitialState(): UiState

    abstract fun handleEvents(event: Event)

    private val initialState: UiState by lazy { setInitialState() }

    private val _uiState: MutableState<UiState> = mutableStateOf(initialState)
    val uiState: State<UiState> = _uiState

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        subscribeToEvents()
    }

    private fun subscribeToEvents() {
        viewModelScope.launch {
            _event.collect {
                handleEvents(it)
            }
        }
    }

    fun setEvent(event: Event) {
        viewModelScope.launch { _event.emit(event) }
    }

    protected fun setState(reducer: UiState.() -> UiState) {
        val newState = uiState.value.reducer()
        _uiState.value = newState
    }

    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

}