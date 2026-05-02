package org.gaziz.birgram.presentation.auth.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.R
import org.gaziz.birgram.presentation.TGViewModel
import org.gaziz.birgram.ui.BackArrow
import org.gaziz.birgram.ui.navigation.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthTopBar(
    navController: NavController,
    tgViewModel: TGViewModel
){
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val loginState by tgViewModel.loginState.collectAsState()
    val authHistory by tgViewModel.authHistory.collectAsState()
    val isDark by tgViewModel.isDarkTheme.collectAsState()
    TopAppBar(
        title = {},
        navigationIcon = {
            if(currentRoute == Route.PasswordRecovery.route){
                BackArrow { navController.navigate(Route.Auth.route) }
            }
            if(authHistory.size > 1 && currentRoute != Route.PasswordRecovery.route){
                if (
                    loginState.javaClass != TdApi.AuthorizationStateWaitPhoneNumber::class.java &&
                    loginState.javaClass != TdApi.AuthorizationStateWaitRegistration::class.java &&
                    authHistory[authHistory.lastIndex-1] !is TdApi.AuthorizationStateWaitTdlibParameters
                ) {
                    BackArrow { tgViewModel.previousAuthHistory() }
                }
            }
        },
        actions = {
            if (loginState is TdApi.AuthorizationStateWaitPhoneNumber && !tgViewModel.isNumber) {
                IconButton(
                    onClick = {tgViewModel.switchIsDark()}
                ) {
                    Icon(
                        imageVector = if(isDark) ImageVector.vectorResource(R.drawable.light_mode) else ImageVector.vectorResource(R.drawable.dark_mode),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}