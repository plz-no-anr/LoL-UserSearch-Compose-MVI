package com.plz.no.anr.lol_usersearch_compose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.plz.no.anr.lol_usersearch_compose.ui.navigation.destination.MainDestination
import com.plz.no.anr.lol_usersearch_compose.ui.navigation.destination.SearchDestination

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Navigation.Routes.MAIN,
    ) {
        composable(route = Navigation.Routes.MAIN) {
            MainDestination(navController = navController)
        }

        composable(route = Navigation.Routes.SEARCH) {
            SearchDestination(navController = navController)
        }
        composable(
            route = Navigation.Routes.SUMMONER,
            arguments = listOf(navArgument(name = Navigation.Args.SUMMONER_NAME) {
                type = NavType.StringType
            })
        ) {
            val summonerName = requireNotNull(it.arguments?.getString(Navigation.Args.SUMMONER_NAME)) { "Summoner name is required as an argument" }

        }
        composable(route = Navigation.Routes.SPECTATOR) {

        }

    }


}

object Navigation {
    object Args {
        const val SUMMONER_NAME = "summoner_name"

    }

    object Routes {
        const val MAIN = "main"
        const val SEARCH = "search"
        const val SUMMONER = "summoner/{${Args.SUMMONER_NAME}}"
        const val SPECTATOR = "spectator"
    }
}

fun NavController.navigateToMain() {
    navigate(route = Navigation.Routes.MAIN)
}

fun NavController.navigateToSearch() {
    navigate(route = Navigation.Routes.SEARCH)
}

fun NavController.navigateToSpectator() {
    navigate(route = Navigation.Routes.SPECTATOR)
}

fun NavController.navigateToSummoner(name: String) {
    navigate(route = "${Navigation.Routes.SUMMONER}/$name")
}