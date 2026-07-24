package org.gaziz.birgram.features.searchChats.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import org.gaziz.birgram.R
import org.gaziz.birgram.core.ui.icon.arrowBack
import org.gaziz.birgram.core.ui.icon.close

@Composable
fun SearchTopBar(
    onBack: () -> Unit,
    onSearch: (String) -> Unit
){
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val searchPlaceHolder = stringResource(R.string.search_placeholder)
    val focusManager = LocalFocusManager.current
    TextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        modifier = Modifier.fillMaxWidth().statusBarsPadding(),
        textStyle = MaterialTheme.typography.labelSmall,
        singleLine = true,
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent
        ),
        leadingIcon = {
            IconButton(
                onClick = { onBack() }
            ) {
                Icon(
                    imageVector = arrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        },
        trailingIcon = {
            if(searchQuery.isNotEmpty()) {
                IconButton(
                    onClick = { searchQuery = "" }
                ) {
                    Icon(
                        imageVector = close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
        },
        placeholder = {
            Text(
                text = searchPlaceHolder,
                style = MaterialTheme.typography.labelSmall,
            )
        },
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                onSearch(searchQuery)
            }
        )
    )
}