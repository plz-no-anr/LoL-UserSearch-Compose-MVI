package com.plz.no.anr.lol_usersearch_compose.ui.feature.summoner.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.plz.no.anr.lol_usersearch_compose.ui.base.SIDE_EFFECTS_KEY
import com.plz.no.anr.lol_usersearch_compose.ui.feature.common.AppProgressBar
import com.plz.no.anr.lol_usersearch_compose.ui.feature.common.error.ErrorScreen
import com.plz.no.anr.lol_usersearch_compose.ui.feature.common.TopAppBar
import com.plz.no.anr.lol_usersearch_compose.ui.feature.summoner.SummonerContract
import com.plznoanr.data.model.common.parseError
import com.plznoanr.domain.model.Summoner
import com.plznoanr.lol_usersearch_compose.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummonerScreen(
    state: SummonerContract.UiState,
    effectFlow: Flow<SummonerContract.Effect>?,
    onEvent: (SummonerContract.Event) -> Unit,
    onNavigationRequested: (SummonerContract.Effect.Navigation) -> Unit
) {
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        onEvent(SummonerContract.Event.OnLoad)
        effectFlow?.onEach { effect ->
            when (effect) {
                is SummonerContract.Effect.Toast -> snackbarHostState.showSnackbar(
                    message = effect.msg,
                    duration = SnackbarDuration.Short
                )
                is SummonerContract.Effect.Navigation.Back -> onNavigationRequested(effect)
                is SummonerContract.Effect.Navigation.ToSpectator -> onNavigationRequested(effect)
            }
        }?.collect()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.summoner_title),
                isBackPressVisible = true,
                onBackPressed = { onEvent(SummonerContract.Event.Navigation.Back) }
            )
        }) {
        when {
            state.isLoading -> AppProgressBar()
            state.error != null -> ErrorScreen(error = state.error.parseError()) { onEvent(SummonerContract.Event.Navigation.Back) }
            else -> {
                state.data?.also { data ->
                    SummonerView(
                        modifier = Modifier.padding(it),
                        data = data,
                        onEvent = onEvent
                    )
                }
            }
        }

    }

}

@Preview
@Composable
private fun SummonerScreenPreview() {
    SummonerScreen(
        state = SummonerContract.UiState(
            data = Summoner(
                name = "",
                level = "200",
                icon = "",
                tier = "CHALLENGER",
                leaguePoints = 300,
                rank = "I",
                wins = 100,
                losses = 0,
                isPlaying = false
            )
        ),
        effectFlow = null,
        onEvent = {},
        onNavigationRequested = {}
    )
}