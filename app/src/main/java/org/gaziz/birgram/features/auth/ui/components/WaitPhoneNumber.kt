package org.gaziz.birgram.features.auth.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.gaziz.birgram.R
import org.gaziz.birgram.core.ui.icons.arrowBack

@Composable
fun WaitPhoneNumber(
    setNumber: (String) -> Unit,
    errorMessage: String?
) {
    var isLoading by rememberSaveable { mutableStateOf(false) }
    val cnt = stringArrayResource(R.array.login_cnt)
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    LaunchedEffect(Unit){
        focusRequester.requestFocus()
    }
    LaunchedEffect(errorMessage) {
        if(errorMessage != null) {
            isLoading = false
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = cnt[3],
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = cnt[5],
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
            color = MaterialTheme.colorScheme.onBackground.copy(0.5f)
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            enabled = !isLoading,
            label = { Text(cnt[4], style = MaterialTheme.typography.labelMedium) },
            singleLine = true,
            isError = errorMessage != null,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .focusRequester(focusRequester),
            textStyle = MaterialTheme.typography.labelMedium,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    isLoading = true
                    setNumber(phoneNumber)
                    phoneNumber = ""
                }
            ),
            trailingIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isVisible = phoneNumber.length > 6 || isLoading
                    AnimatedVisibility(isVisible) {
                        VerticalDivider(
                            thickness = 1.dp,
                            modifier = Modifier.height(56.dp),
                            color = when {
                                isLoading -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                                errorMessage != null -> MaterialTheme.colorScheme.error
                                else -> MaterialTheme.colorScheme.primary
                            }
                        )
                    }
                    AnimatedVisibility(isVisible) {
                        if(isLoading) {
                            IconButton(
                                onClick = {},
                                enabled = false
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                                    strokeWidth = 3.dp
                                )
                            }
                        } else {
                            IconButton(
                                onClick = {
                                    focusManager.clearFocus()
                                    isLoading = true
                                    setNumber(phoneNumber)
                                    phoneNumber = ""
                                },
                            ) {
                                Icon(
                                    imageVector = arrowBack,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(26.dp)
                                        .graphicsLayer { scaleX = -1f },
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                            }
                        }
                    }
                }
            },
            leadingIcon = { Text("+") }
        )
        Spacer(Modifier.height(16.dp))
        AnimatedVisibility(errorMessage != null) {
            Text(
                errorMessage.toString(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}