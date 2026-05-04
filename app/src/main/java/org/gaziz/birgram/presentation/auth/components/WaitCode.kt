package org.gaziz.birgram.presentation.auth.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.gaziz.birgram.R
import org.gaziz.birgram.domain.model.auth.AuthCodeInfo

@Composable
fun WaitCode(
    setCode: (String) -> Unit,
    errorMessage: String?,
    onBack: () -> Unit,
    codeInfo: AuthCodeInfo,
    resendCode: () -> Unit
){
    val cnt = stringArrayResource(R.array.login_cnt)
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var code by rememberSaveable { mutableStateOf("") }
    val codeType = stringArrayResource(R.array.code_type)[codeInfo.type.type.ordinal]
    var counter by rememberSaveable { mutableIntStateOf(codeInfo.timeout) }
    LaunchedEffect(Unit){
        focusRequester.requestFocus()
        while(counter > 0) {
            counter--
            delay(1000)
        }
    }
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(WindowInsets.statusBars.asPaddingValues()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = { onBack() }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.arrow_back),
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = cnt[11],
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = codeType,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
                color = MaterialTheme.colorScheme.onBackground.copy(0.5f)
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = code,
                isError = errorMessage != null,
                onValueChange = { code = it },
                label = { Text(cnt[12], style = MaterialTheme.typography.labelMedium) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .focusRequester(focusRequester),
                textStyle = MaterialTheme.typography.labelMedium,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Decimal,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        setCode(code)
                        code = ""
                    }
                ),
                trailingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val isVisible = code.length >= codeInfo.type.length
                        AnimatedVisibility(isVisible) {
                            VerticalDivider(
                                thickness = 1.dp,
                                modifier = Modifier.height(56.dp),
                                color = if(errorMessage != null)MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                            )
                        }
                        AnimatedVisibility(isVisible) {
                            IconButton(
                                onClick = {
                                    focusManager.clearFocus()
                                    setCode(code)
                                    code = ""
                                },
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.arrow_back),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(26.dp)
                                        .graphicsLayer { scaleX = -1f },
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                            }
                        }
                    }
                },
            )
            if(codeInfo.nextType != null) {
                val nextType = stringArrayResource(R.array.next_type)[codeInfo.nextType.type.ordinal]
                val nextTypeSuffix = stringResource(R.string.suffix_waiting)
                Spacer(Modifier.height(8.dp))
                TextButton(
                    onClick = {
                        resendCode()
                    },
                    enabled = counter < 1
                ) {
                    Text(
                        text = if(counter < 1) nextType else "$nextType $nextTypeSuffix $counter",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
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
}