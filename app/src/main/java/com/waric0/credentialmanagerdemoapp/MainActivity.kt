package com.waric0.credentialmanagerdemoapp

import android.os.Bundle
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.waric0.credentialmanagerdemoapp.ui.theme.CredentialManagerDemoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                        AutofillUsername()
                        Spacer(modifier = Modifier.height(20.dp))
                        AutofillPassword()
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(onClick = { }) {
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
fun AutofillUsername() {
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
                } else {
                    autofill?.cancelAutofillForNode(autoFillNode)
                }
            },
        value = username,
        onValueChange = { username = it },
        label = {
            Text(text = "username")
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AutofillUsernamePreview() {
    CredentialManagerDemoAppTheme {
        AutofillUsername()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AutofillPassword() {
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
        onValueChange = { password = it },
        label = {
            Text(text = "password")
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AutofillPasswordPreview() {
    CredentialManagerDemoAppTheme {
        AutofillPassword()
    }
}