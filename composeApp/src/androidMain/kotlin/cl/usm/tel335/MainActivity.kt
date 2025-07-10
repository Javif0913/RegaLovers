package cl.usm.tel335

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import cl.usm.tel335.model.UsuariosDto
import cl.usm.tel335.ui.LoginScreen
import cl.usm.tel335.ui.RegisterScreen
import cl.usm.tel335.ui.UserScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val currentScreen = remember { mutableStateOf<Screen>(Screen.Login) }
                val currentUser = remember { mutableStateOf<UsuariosDto?>(null) }  // Estado para el usuario

                when (currentScreen.value) {
                    Screen.Login -> LoginScreen(
                        onLoginSuccess = { user ->
                            currentUser.value = user  // Almacena el usuario
                            currentScreen.value = Screen.User  // Navega a UserScreen
                        },
                        onRegisterClick = {
                            currentScreen.value = Screen.Register
                        }
                    )
                    Screen.Register -> RegisterScreen(
                        onRegisterSuccess = {
                            currentScreen.value = Screen.Login
                        },
                        onBackToLoginClick = {
                            currentScreen.value = Screen.Login
                        }
                    )
                    Screen.User -> currentUser.value?.let { user ->  // Solo si hay usuario
                        UserScreen(
                            usuario = user,
                            onLogout = {
                                currentUser.value = null
                                currentScreen.value = Screen.Login
                            }
                        )
                    }
                }
            }
        }
    }
}
sealed class Screen {
    object Login : Screen()
    object Register : Screen()
    object User : Screen()
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen(
            onLoginSuccess = {},
            onRegisterClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    MaterialTheme {
        RegisterScreen(
            onRegisterSuccess = {},
            onBackToLoginClick = {}
        )
    }
}