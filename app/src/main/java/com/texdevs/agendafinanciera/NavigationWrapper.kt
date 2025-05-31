package com.texdevs.agendafinanciera

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.texdevs.agendafinanciera.presentation.initial.InitialScreen
import com.texdevs.agendafinanciera.presentation.login.LoginScreen
import com.texdevs.agendafinanciera.presentation.signup.SignUpScreen

@Composable
fun NavigationWrapper(navHostController: NavHostController, auth: FirebaseAuth) {

    NavHost(navController = navHostController, startDestination = "initial"){
        composable("initial"){
            InitialScreen(
                navigateToLogin = {navHostController.navigate("logIn")},
                navigateToSignUp = {navHostController.navigate("signUp")}
            )
        }
        composable("logIn"){
            LoginScreen(auth)
        }
        composable("signUp"){
            SignUpScreen(auth)
        }
    }
}