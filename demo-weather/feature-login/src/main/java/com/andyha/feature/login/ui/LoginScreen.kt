package com.andyha.feature.login.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andyha.coreextension.updateLanguageResource
import com.andyha.coreresource.R as CoreR
import com.andyha.coreui.base.theme.Language
import com.andyha.feature.login.R


@Composable
fun LoginScreen(
    onSignin: () -> Unit,
){
    val loginViewModel: LoginViewModel = hiltViewModel()
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isUserAuthorized by loginViewModel.isUserAuthorized.collectAsStateWithLifecycle()
    val language by loginViewModel.language.collectAsStateWithLifecycle()
    val isVietnamese = language.ifEmpty { Language.getDeviceLocale() }.startsWith("vi")

    LaunchedEffect(isUserAuthorized) {
        if (isUserAuthorized) {
            onSignin()
        }
    }

    key(language) {
        Scaffold { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(R.string.welcome),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 24.dp),
                )
                Username(
                    username = username,
                    onValueChange = { username = it },
                )
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = {
                        Text(
                            text = stringResource(id = R.string.password_title),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (username.isNotBlank() && password.isNotBlank()) {
                                loginViewModel.login(username.trim())
                            }
                        }
                    ),
                    singleLine = true
                )
                Button(
                    onClick = { if (username.isNotBlank() && password.isNotBlank()) loginViewModel.login(username.trim()) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                ) {
                    Text(stringResource(R.string.just_login))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    IconButton(
                        onClick = {
                            val next = loginViewModel.nextLanguage()
                            context.updateLanguageResource(next)
                            loginViewModel.setLanguage(next)
                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                if (isVietnamese) CoreR.drawable.ic_flag_vn else CoreR.drawable.ic_flag_us
                            ),
                            contentDescription = stringResource(CoreR.string.change_language),
                            tint = Color.Unspecified,
                        )
                    }
                    IconButton(onClick = { loginViewModel.toggleTheme() }) {
                        Icon(
                            painter = painterResource(CoreR.drawable.ic_theme_light),
                            contentDescription = stringResource(CoreR.string.change_theme),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Username(
    username: String,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = username,
        onValueChange = onValueChange,
        label = {
            Text(
                text = stringResource(id = R.string.username_title),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->

            },
        textStyle = MaterialTheme.typography.bodyMedium,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email
        ),
        keyboardActions = KeyboardActions(
            onDone = {

            }
        ),
        singleLine = true
    )
}


@Preview(name = "Login light theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Login dark theme", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun LoginScreenPreview() {
       LoginScreen(
           onSignin = {},
       )
}
