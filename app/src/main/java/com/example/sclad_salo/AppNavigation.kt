package com.example.sclad_salo

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sclad_salo.sign_in.LoginPage
import com.example.sclad_salo.sign_up.RegistrationPage
import com.example.sclad_salo.ui.Dashboard.DashboardPage_unit_create
import com.example.sclad_salo.ui.List_operators.ListOperatorsPage
import com.example.sclad_salo.ui.home.HomePage_units_list
import com.example.sclad_salo.ui.notification_ai.NotificationPage_AI
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

object AppRoutes{

    const val HOME = "home"

    const val DASHBOARD = "dashboard"

    const val NOTIFICATION_AI = "notification_ai"

    const val REGISTRATION = "registration"

    const val LOGIN = "login"

    const val LIST_OF_OPERATORS = "list_of_operators"


}





@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    //Определяет направление пользователя, начиная с экрана логина

    val startDestination = if (Firebase.auth.currentUser != null) {
        AppRoutes.HOME // Если юзер залогинен
    }else {
        AppRoutes.LOGIN // Если юзер не-залогинен
    }



    NavHost(
        navController = navController,
        startDestination = startDestination


    ){
        //Login Screen

        composable(AppRoutes.LOGIN) {
            LoginPage(
                onLoginSuccess = {
                    //В случае успеха возвращаемся домой и очищаем всю историю навигации
                    navController.navigate(AppRoutes.HOME){

                        popUpTo(navController.graph.id) {inclusive = true}

                    }

                },
                onNavigateToRegistration = {
                    navController.navigate(AppRoutes.REGISTRATION)
                }


            )

        }
//Registration Screen

        composable(AppRoutes.REGISTRATION) {
            RegistrationPage (
                onRegistrationSuccess = {
                    //В случае успеха переходим к входу в систему и очищаем экран регистрации из стека
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.REGISTRATION) {inclusive= true}
                    }
                }

            )

        }

        //Home Screen
        composable(AppRoutes.HOME) {
            HomePage_units_list (
                navController = navController,
                onNavigateToDashboardPage = {
                    navController.navigate(AppRoutes.DASHBOARD)


                },
                onNavigateToLoginPage = {
                    navController.navigate(AppRoutes.LOGIN)
                },


                onNavigateToNofitications = {
                    navController.navigate(AppRoutes.LOGIN)
                }


            )


        }


        //Dashboard Screen

        composable(AppRoutes.DASHBOARD) {
            DashboardPage_unit_create(
                navController = navController,
                onNavigateBack = {
                    // popBackStack - возврат на предыдущую страницу, для более естественной обратной
                    navController.popBackStack()
                }

            )
        }

        //Notification AI Screen

        composable(AppRoutes.NOTIFICATION_AI) {
            NotificationPage_AI (
                navController = navController,
                onNavigateBack = {
                    navController.popBackStack()

                }

            )

        }
        //List Operations Screen

        composable(AppRoutes.LIST_OF_OPERATORS) {
            ListOperatorsPage (
                navController = navController,
                onNavigateBack = {
                    navController.popBackStack()

                }

            )

        }

    }


}