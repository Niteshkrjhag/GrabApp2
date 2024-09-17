package com.example.first_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.first_app.ChatBot.ChatViewModel.ChatViewModel
import com.example.first_app.ChatBot.ScreenCB.ChatBotScreen
import com.example.first_app.Screen.Login
import com.example.first_app.Screen.signUpScreen
import com.example.first_app.Login_auth.LoginViewModel
import com.example.first_app.presentation.nvGraph.NavGraph

@Preview(showBackground = true, showSystemUi = true)
@Composable

fun Navigation(startDestination:String,authViewModel:LoginViewModel,chatViewModel: ChatViewModel){

    val navController = rememberNavController()
    NavHost(navController = navController
        , startDestination =Routes.Login, builder = {
            composable(Routes.Login){
                Login(navController,authViewModel)
            }
            composable(Routes.SignUp){
                signUpScreen(navController,authViewModel)
            }
            composable(Routes.BoardingScreen){
//                val viewModel:OnBoardingViewModel = hiltViewModel()
//                OnBoardingScreen(
//                    OnEvent= viewModel::onEvent
//                )
                NavGraph(StartDestination=startDestination)
            }

        }
    )

}