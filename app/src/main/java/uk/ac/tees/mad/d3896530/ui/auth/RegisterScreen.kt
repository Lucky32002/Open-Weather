package uk.ac.tees.mad.d3896530.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import uk.ac.tees.mad.d3896530.NavDestinations

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onLogin: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {


    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val registerState by viewModel.registerState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(modifier) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            Column(Modifier.padding(24.dp)) {
                Text(text = "Create an account", fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "Name", fontWeight = FontWeight.Medium)
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Email address", fontWeight = FontWeight.Medium)
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Password", fontWeight = FontWeight.Medium)
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    )
                )
                Spacer(modifier = Modifier.height(36.dp))
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.register(name, email, password)
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors().copy(containerColor = Color.Blue)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White
                        )
                    } else {
                        Text("Register")
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = onLogin) {
                Text(text = "Login to your account", color = Color.Blue)
            }
            registerState?.let {
                when {
                    it.isSuccess -> {
                        Text("Registration Successful")
                        onSuccess.invoke()
                    }

                    it.isFailure -> Text("Registration Failed: ${it.exceptionOrNull()?.message}")
                }
            }
        }
    }

}

object RegisterDes : NavDestinations {
    override val navRoute = "register"
}