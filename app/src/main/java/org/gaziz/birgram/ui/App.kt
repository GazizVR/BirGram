package org.gaziz.birgram.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.R
import org.gaziz.birgram.domain.model.auth.AuthData
import org.gaziz.birgram.presentation.TGViewModel
import org.gaziz.birgram.presentation.auth.components.AuthTopBar
import org.gaziz.birgram.presentation.chatList.components.ChatListTopBar
import org.gaziz.birgram.ui.navigation.Navigation
import org.gaziz.birgram.ui.navigation.Route

@Composable
fun BackArrow(
    callBack: () -> Unit
){
    IconButton(
        onClick = { callBack() }
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.arrow_back),
            contentDescription = "Back",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
fun Dialog(
    isActive: Boolean,
    title: String,
    description: String,
    descriptionColor: Color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
    descriptionStyle: TextStyle = MaterialTheme.typography.labelMedium,
    cancel: String,
    next: String? = null,
    onCancel: () -> Unit,
    onNext: (() -> Unit)? = null
){
    if (isActive) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background.copy(0.8f))
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {}
        ) {}
    }
    if(isActive) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = description,
                        textAlign = TextAlign.Center,
                        style = descriptionStyle,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
                        color = descriptionColor
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { onCancel() }
                        ) {
                            Text(
                                text = cancel,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                        if(onNext != null && next != null){
                            Spacer(Modifier.width(6.dp))
                            TextButton(
                                onClick = { onNext() }
                            ) {
                                Text(
                                    text = next,
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun App(tgViewModel: TGViewModel) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val loginState by tgViewModel.loginState.collectAsState()
    val cnt = stringArrayResource(R.array.login_cnt)
    Box(Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                when (currentRoute) {
                    Route.Auth.route, Route.PasswordRecovery.route -> {
                        AuthTopBar(navController, tgViewModel)
                    }
                    Route.Chats.route -> {
                        ChatListTopBar(tgViewModel)
                    }
                    else -> {}
                }
            },
            bottomBar = {
                when(currentRoute){
                    Route.Auth.route -> {
                        if(loginState.javaClass == TdApi.AuthorizationStateWaitRegistration::class.java){
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(
                                    text = buildAnnotatedString {
                                        append(cnt[46])
                                        withLink(
                                            LinkAnnotation.Clickable(
                                                tag = "FUCKER",
                                                styles = TextLinkStyles(SpanStyle(MaterialTheme.colorScheme.primary)),
                                                linkInteractionListener = {tgViewModel.isTerms = true}
                                            )
                                        ){
                                            append(cnt[47])
                                        }
                                    },
                                )
                                Button(
                                    onClick = { tgViewModel.nextLoginStep(
                                        TdApi.AuthorizationStateWaitRegistration(),
                                        AuthData.Registration(tgViewModel.firstName,tgViewModel.lastName,false)
                                    ) },
                                    contentPadding = PaddingValues(0.dp),
                                    shape = CircleShape
                                ){
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.arrow_back),
                                        contentDescription = null,
                                        modifier = Modifier.scale(scaleX = -1f,scaleY=1f),
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                    }
                    else -> {}
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Navigation(navController, tgViewModel, paddingValues)
        }
        if(loginState.javaClass == TdApi.AuthorizationStateWaitEmailCode::class.java){
            Dialog(
                isActive = tgViewModel.isAvailableRecovery != null,
                title = cnt[39],
                description = cnt[40],
                descriptionColor = MaterialTheme.colorScheme.onBackground,
                cancel = "OK",
                onCancel = { tgViewModel.isAvailableRecovery = null },
            )
            Dialog(
                isActive = tgViewModel.isPendingRecovery != null,
                title = cnt[33],
                description = "${cnt[34]} ${(loginState as TdApi.AuthorizationStateWaitEmailCode).codeInfo?.emailAddressPattern}${cnt[35]}",
                cancel = cnt[20],
                next = cnt[36],
                onCancel = { tgViewModel.isPendingRecovery = null },
                onNext = {
                    tgViewModel.resetEmail()
                    tgViewModel.isPendingRecovery = null
                }
            )
        }
        if(tgViewModel.isAccountDelete){
            var isSecond by rememberSaveable{mutableStateOf(false)}
            Dialog(
                isActive = !isSecond,
                title = cnt[17],
                description = cnt[18],
                cancel = cnt[20],
                next = cnt[19],
                onCancel = { tgViewModel.isAccountDelete = false },
                onNext = { isSecond = true }
            )
            Dialog(
                isActive = isSecond,
                title = cnt[21],
                description = cnt[22],
                cancel = cnt[20],
                next = cnt[19],
                onCancel = { tgViewModel.isAccountDelete = false },
                onNext = {
                    tgViewModel.isAccountDelete = false
                    tgViewModel.previousAuthHistory()
                    tgViewModel.deleteAccount()
                }
            )
        }
        if(loginState.javaClass == TdApi.AuthorizationStateWaitRegistration::class.java){
            Dialog(
                isActive = tgViewModel.isTerms,
                title = cnt[48],
                description = (loginState as TdApi.AuthorizationStateWaitRegistration).termsOfService?.text?.text ?: "",
                descriptionColor = MaterialTheme.colorScheme.onBackground,
                cancel = "OK",
                onCancel = {tgViewModel.isTerms = false},
            )
        }
        Dialog(
            isActive = tgViewModel.isPhoneNumber.first,
            title = cnt[6],
            description = "+${tgViewModel.isPhoneNumber.second}",
            descriptionStyle = MaterialTheme.typography.bodyLarge,
            cancel = cnt[7],
            next = cnt[8],
            onCancel = {tgViewModel.isPhoneNumber = Pair(false,"")},
            onNext = {
                tgViewModel.nextLoginStep(TdApi.AuthorizationStateWaitPhoneNumber(),AuthData.PhoneNumber(tgViewModel.isPhoneNumber.second))
                tgViewModel.isPhoneNumber = Pair(false,"")
            }
        )
    }
}
