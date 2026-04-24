package com.yolo.myhabitshub.navigation

import androidx.navigation.NavHostController
import com.yolo.core.presentation.navigation.Navigator
import com.yolo.core.presentation.navigation.NavigationAction

class ComposeNavigator(private val navController: NavHostController) : Navigator {
    override fun execute(action: NavigationAction) {
        when (action) {
            is NavigationAction.Navigate -> navController.navigate(action.route)
            is NavigationAction.NavigatePopUpTo -> navController.navigate(action.route) {
                popUpTo(action.popUpTo) { inclusive = action.inclusive }
            }
            is NavigationAction.PopBackStack -> navController.popBackStack()
        }
    }
}
