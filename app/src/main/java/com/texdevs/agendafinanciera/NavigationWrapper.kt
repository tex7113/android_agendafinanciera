package com.texdevs.agendafinanciera

import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambda
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.texdevs.agendafinanciera.presentation.home.HomeScreen
import com.texdevs.agendafinanciera.presentation.initial.InitialScreen
import com.texdevs.agendafinanciera.presentation.login.LoginScreen
import com.texdevs.agendafinanciera.presentation.signup.SignUpScreen

@Composable
fun NavigationWrapper(
    navHostController: NavHostController,
    auth: FirebaseAuth,
) {

    NavHost(navController = navHostController, startDestination = "home"){
        composable("initial"){
            InitialScreen(
                navigateToLogin = {navHostController.navigate("logIn")},
                navigateToSignUp = {navHostController.navigate("signUp")}
            )
        }
        composable("logIn"){
            LoginScreen(auth){navHostController.navigate("home")}
        }
        composable("signUp"){
            SignUpScreen(auth)
        }
        composable("home"){
            HomeScreen()
        }
    }
}