package com.plznoanr.lol_usersearch_compose.ui.navigation


sealed class Destination(open val route: String) {

    object Main : Destination("main")

    object Search : Destination("search")

    object Summoner : Destination("summoner") {
        object Args {
            const val KEY_SUMMONER_NAME = "summoner_name"
        }

        override val route: String
        get() = "${super.route}?{${Args.KEY_SUMMONER_NAME}}"
        fun routeWithArgs(summonerName: String) = "${super.route}?$summonerName"
    }

    object Spectator : Destination("spectator") {
        object Args {
            const val KEY_SUMMONER_NAME = "summoner_name"
        }

        override val route: String
        get() = "${super.route}?{${Args.KEY_SUMMONER_NAME}}"
        fun pathWithArgs(summonerName: String) = "${super.route}?$summonerName"
    }

}

private fun generateArgsRoute(route: String, args: List<String>): String {
    var routeWithArgs = route
    args.forEach {
        routeWithArgs += "?{$it}"
    }
    return routeWithArgs
}
