package com.plznoanr.lol.feature.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.plznoanr.lol.core.common.model.parseError
import com.plznoanr.lol.core.designsystem.component.AppProgressBar
import com.plznoanr.lol.core.designsystem.component.error.ErrorScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SearchRoute(
    onShowSnackbar: suspend (String) -> Boolean,
    navigateToSummoner: (String, String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val eventChannel = remember { Channel<Event>(Channel.UNLIMITED) }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Main.immediate) {
            eventChannel
                .consumeAsFlow()
                .onEach(viewModel::onEvent)
                .collect()
        }
    }
    val onEvent = remember {
        { event: Event ->
            eventChannel.trySend(event).getOrThrow()
        }
    }

    SearchScreen(
        state = uiState,
        onEvent = onEvent,
        sideEffectFlow = viewModel.sideEffectFlow,
        navigateToSummoner = navigateToSummoner,
        onShowSnackbar = onShowSnackbar
    )

}

@Composable
internal fun SearchScreen(
    state: UiState,
    onEvent: (Event) -> Unit,
    sideEffectFlow: Flow<SideEffect>,
    navigateToSummoner: (String, String) -> Unit,
    onShowSnackbar: suspend (String) -> Boolean,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        sideEffectFlow.onEach { sideEffect ->
            when (sideEffect) {
                is NavigateToSummoner -> navigateToSummoner(sideEffect.name, sideEffect.tag)
                is ShowSnackbar -> coroutineScope.launch {
                    onShowSnackbar(sideEffect.message)
                }
            }
        }.collect()
    }
    when {
        state.isLoading -> AppProgressBar()
        state.error != null -> ErrorScreen(
            error = state.error.parseError()
        ) {}

        else -> {
            SearchContent(
                data = state.data,
                name = state.query,
                isActive = state.isActive,
                onNameChange = { onEvent(Event.OnQueryChange(it)) },
                onSearch = {
                    onEvent(Event.OnSearch(it))
                    keyboardController?.hide()
                },
                onActiveChange = { onEvent(Event.OnActiveChange(it)) },
                onDelete = { onEvent(Event.OnDelete(it)) },
                onDeleteAll = { onEvent(Event.OnDeleteAll) }
            )
        }
    }

}

@Preview
@Composable
private fun SearchScreenPreview() {
    SearchScreen(
        state = UiState(),
        sideEffectFlow = flow { },
        onEvent = {},
        navigateToSummoner = { n,t -> },
        onShowSnackbar = { true }
    )
}