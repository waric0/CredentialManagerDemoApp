package com.waric0.credentialmanagerdemoapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.*
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.GetCredentialException
import com.waric0.credentialmanagerdemoapp.ui.theme.CredentialManagerDemoAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val credentialManager = CredentialManager.create(this)
        val getPasswordOption = GetPasswordOption()
        val getCredRequest = GetCredentialRequest(
            listOf(getPasswordOption)
        )

        enableEdgeToEdge()
        setContent {
            CredentialManagerDemoAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = innerPadding.calculateTopPadding()),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val context = LocalContext.current
                        val coroutineScope = rememberCoroutineScope()
                        var username by remember { mutableStateOf("") }
                        var password by remember { mutableStateOf("") }
                        AutofillUsername(
                            onFocused = {
                                coroutineScope.launch {
                                    try {
                                        val result = credentialManager.getCredential(
                                            // Use an activity-based context to avoid undefined system UI
                                            // launching behavior.
                                            context = context,
                                            request = getCredRequest
                                        )
                                        when (val credential = result.credential) {
                                            is PasswordCredential -> {
                                                val username = credential.id
                                                val password = credential.password
                                                Log.d(
                                                    "",
                                                    "username : $username / password : $password"
                                                )
                                            }
                                        }
                                    } catch (e: GetCredentialException) {
                                        Log.e(
                                            "",
                                            "",
                                            e
                                        )
                                    }
                                }
                            },
                            onValueChange = {
                                username = it
                            }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        AutofillPassword(
                            onValueChange = {
                                password = it
                            }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = {
                                val createPasswordRequest =
                                    CreatePasswordRequest(id = username, password = password)

                                coroutineScope.launch {
                                    try {
                                        val result =
                                            credentialManager.createCredential(
                                                context,
                                                createPasswordRequest
                                            )
                                        Log.d(
                                            "",
                                            "username : $username / password : $password"
                                        )
                                    } catch (e: CreateCredentialException) {
                                        Log.e(
                                            "",
                                            "",
                                            e
                                        )
                                    }
                                }
                            }
                        ) {
                            Text("Login")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AutofillUsername(
    onFocused: () -> Unit,
    onValueChange: (String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    val autoFillNode =
        AutofillNode(autofillTypes = listOf(AutofillType.Username), onFill = { username = it })
    val autofill = LocalAutofill.current
    TextField(
        modifier = Modifier
            .onGloballyPositioned {
                autoFillNode.boundingBox = it.boundsInWindow()
            }
            .onFocusChanged {
                if (it.isFocused) {
                    autofill?.requestAutofillForNode(autoFillNode)
                    onFocused()
                } else {
                    autofill?.cancelAutofillForNode(autoFillNode)
                }
            },
        value = username,
        onValueChange = {
            username = it
            onValueChange(it)
        },
        label = {
            Text(text = "username")
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AutofillUsernamePreview() {
    CredentialManagerDemoAppTheme {
        AutofillUsername(onFocused = {}, onValueChange = {})
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AutofillPassword(
    onValueChange: (String) -> Unit
) {
    var password by remember { mutableStateOf("") }
    val autoFillNode =
        AutofillNode(autofillTypes = listOf(AutofillType.Password), onFill = { password = it })
    val autofill = LocalAutofill.current
    TextField(
        modifier = Modifier
            .onGloballyPositioned {
                autoFillNode.boundingBox = it.boundsInWindow()
            }
            .onFocusChanged {
                if (it.isFocused) {
                    autofill?.requestAutofillForNode(autoFillNode)
                } else {
                    autofill?.cancelAutofillForNode(autoFillNode)
                }
            },
        value = password,
        onValueChange = {
            password = it
            onValueChange(it)
        },
        label = {
            Text(text = "password")
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AutofillPasswordPreview() {
    CredentialManagerDemoAppTheme {
        AutofillPassword(onValueChange = {})
    }
}