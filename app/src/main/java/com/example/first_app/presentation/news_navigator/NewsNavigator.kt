package com.example.first_app.presentation.news_navigator

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.first_app.ChatBot.ChatViewModel.ChatViewModel
import com.example.first_app.ChatBot.ScreenCB.ChatBotScreen
import com.example.first_app.Login_auth.LoginViewModel
import com.example.first_app.Login_auth.ProfileViewModel
import com.example.first_app.R
import com.example.first_app.Screen.SearchScreen
import com.example.first_app.Screen.SettingsScreen
import com.example.first_app.domain.model.Article
import com.example.first_app.googleSignIn.GoogleSignInViewModel
import com.example.first_app.presentation.bookmark.BookmarkScreen
import com.example.first_app.presentation.bookmark.BookmarkViewModel
import com.example.first_app.presentation.details.DetailsScreen
import com.example.first_app.presentation.details.DetailsViewModel
import com.example.first_app.presentation.home.HomeScreen_NS
import com.example.first_app.presentation.home.HomeViewModel
import com.example.first_app.presentation.news_navigator.components.BottomNavigationItem
import com.example.first_app.presentation.news_navigator.components.NewsBottomNavigation
import com.example.first_app.presentation.nvGraph.RouteNS
import com.example.first_app.presentation.search.SearchViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsNavigator() {

    val bottomNavigationItems = remember {
        listOf(
            BottomNavigationItem(icon = R.drawable.ic_home, text = "Home"),
            BottomNavigationItem(icon = R.drawable.ic_search, text = "Search"),
            BottomNavigationItem(icon = R.drawable.ic_bookmark, text = "Bookmark"),
            BottomNavigationItem(icon = R.drawable.ic_setting, text = "Settings")
//            BottomNavigationItem(icon = R.drawable.chatbot_icon, text = "ChatBot")
        )
    }

    val navController = rememberNavController()
    val backStackState = navController.currentBackStackEntryAsState().value
    var selectedItem by rememberSaveable {
        mutableStateOf(0)
    }
    selectedItem = when (backStackState?.destination?.route) {
        RouteNS.HomeScreen.route -> 0
        RouteNS.SearchScreen.route -> 1
        RouteNS.BookmarkScreen.route -> 2
        RouteNS.SettingsScreen.route ->3
//        RouteNS.ChatBotScreen.route ->4
        else -> 0
    }

    //Hide the bottom navigation when the user is in the details screen
    val isBottomBarVisible = remember(key1 = backStackState) {
        backStackState?.destination?.route == RouteNS.HomeScreen.route ||
                backStackState?.destination?.route == RouteNS.SearchScreen.route ||
                backStackState?.destination?.route == RouteNS.BookmarkScreen.route ||
                backStackState?.destination?.route == RouteNS.SettingsScreen.route
    }


    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        if (isBottomBarVisible) {
            NewsBottomNavigation(
                items = bottomNavigationItems,
                selectedItem = selectedItem,
                onItemClick = { index ->
                    when (index) {
                        0 -> navigateToTab(
                            navController = navController,
                            route = RouteNS.HomeScreen.route
                        )

                        1 -> navigateToTab(
                            navController = navController,
                            route = RouteNS.SearchScreen.route
                        )

                        2 -> navigateToTab(
                            navController = navController,
                            route = RouteNS.BookmarkScreen.route
                        )
                        3 -> navigateToTab(
                            navController = navController,
                            route = RouteNS.SettingsScreen.route
                        )
//                        4-> navigateToTab(
//                            navController = navController,
//                            route = RouteNS.ChatBotScreen.route
//                        )
                    }
                }
            )
        }
    }) {
        val bottomPadding = it.calculateBottomPadding()
        NavHost(
            navController = navController,
            startDestination = RouteNS.HomeScreen.route,
            modifier = Modifier.padding(bottom = bottomPadding)
        ) {
            composable(route = RouteNS.HomeScreen.route) { backStackEntry ->
                val viewModel: HomeViewModel = hiltViewModel()
                val articles = viewModel.news.collectAsLazyPagingItems()
                HomeScreen_NS(
                    articles = articles,
                    navigateToSearch = {
                        navigateToTab(
                            navController = navController,
                            route = RouteNS.SearchScreen.route
                        )
                    },
                    navigateToDetails = { article ->
                        navigateToDetails(
                            navController = navController,
                            article = article
                        )
                    },navController
                )
            }
            composable(route = RouteNS.SearchScreen.route) {
                val viewModel: SearchViewModel = hiltViewModel()
                val state = viewModel.state.value
                OnBackClickStateSaver(navController = navController)
                SearchScreen(
                    state = state,
                    event = viewModel::onEvent,
                    navigateToDetails = { article ->
                        navigateToDetails(
                            navController = navController,
                            article = article
                        )
                    }
                )
            }
            composable(route = RouteNS.DetailsScreen.route) {
                val viewModel: DetailsViewModel = hiltViewModel()
                navController.previousBackStackEntry?.savedStateHandle?.get<Article?>("article")
                    ?.let { article ->
                        DetailsScreen(
                            article = article,
                            event = viewModel::onEvent,
                            navigateUp = { navController.navigateUp() },
                            sideEffect = viewModel.sideEffect
                        )
                    }

            }
            composable(route = RouteNS.BookmarkScreen.route) {
                val viewModel: BookmarkViewModel = hiltViewModel()
                val state = viewModel.state.value
                OnBackClickStateSaver(navController = navController)
                BookmarkScreen(
                    state = state,
                    navigateToDetails = { article ->
                        navigateToDetails(
                            navController = navController,
                            article = article
                        )
                    }
                )
            }
            composable(route = RouteNS.SettingsScreen.route){
                val googleSignInViewModel:GoogleSignInViewModel = hiltViewModel()
                val viewModel: LoginViewModel = hiltViewModel()
                val profile_VM:ProfileViewModel= hiltViewModel()
                OnBackClickStateSaver(navController = navController)
                   SettingsScreen(navController,viewModel,profile_VM, googleSignInViewModel = googleSignInViewModel)
            }
            composable(route = RouteNS.ChatBotScreen.route){
                val chatViewModel: ChatViewModel = hiltViewModel()
                OnBackClickStateSaver(navController = navController)
                ChatBotScreen(navController, chatViewModel)
            }
        }
    }
}

@Composable
fun OnBackClickStateSaver(navController: NavController) {
    BackHandler(true) {
        navigateToTab(
            navController = navController,
            route = RouteNS.HomeScreen.route
        )
    }
}

private fun navigateToTab(navController: NavController, route: String) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { screen_route ->
            popUpTo(screen_route) {
                saveState = true
            }
        }
        launchSingleTop = true
        restoreState = true
    }
}

private fun navigateToDetails(navController: NavController, article: Article) {
    navController.currentBackStackEntry?.savedStateHandle?.set("article", article)
    navController.navigate(
        route = RouteNS.DetailsScreen.route
    )
}
// In NewsNavigator.kt
//
//private fun navigateToChatBot(navController: NavController) {
//    navController.navigate(RouteNS.ChatBotScreen.route) {
//        launchSingleTop = true  // Avoid duplicates in the back stack
//    }
//}
