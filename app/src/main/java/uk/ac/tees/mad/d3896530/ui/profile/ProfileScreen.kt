package uk.ac.tees.mad.d3896530.ui.profile

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import uk.ac.tees.mad.d3896530.BottomNavigationBar
import uk.ac.tees.mad.d3896530.NavDestinations
import uk.ac.tees.mad.d3896530.models.UserProfile
import uk.ac.tees.mad.d3896530.ui.auth.LoginDes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController, viewModel: ProfileViewModel = hiltViewModel()) {
    val profile by viewModel.userProfile.collectAsState()
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var showCamera by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf(profile.name) }
    var email by remember { mutableStateOf(profile.email) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.fetchUserProfile()
    }
    if (showCamera) {
        RequestCameraPermission {
            CameraCapture(onImageCaptured = { uri ->
                profileImageUri = uri
                viewModel.uploadProfileImage(uri)
                showCamera = false
            })
        }
    } else {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController = navController, modifier = Modifier)
            },
            topBar = {
                TopAppBar(title = { Text(text = "Profile") })
            }
        ) { pv ->
            Box(Modifier.padding(pv)) {


                Column(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .border(2.dp, Color.Blue, CircleShape)
                            .padding(6.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {

                        if (profileImageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(profileImageUri),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .clickable {

                                        showCamera = true
                                    }
                            )
                        } else {
                            if (profile.profileImageUrl.isEmpty()) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            showCamera = true
                                        },
                                    tint = Color.Blue
                                )
                            } else {
                                Image(
                                    painter = rememberAsyncImagePainter(profile.profileImageUrl),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            showCamera = true
                                        }
                                )
                            }
                        }
                    }
                    Column {
                        if (isEditing) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Edit profile",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 22.sp
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                TextButton(onClick = {
                                    isEditing = false
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Cancel,
                                        contentDescription = null,
                                        tint = Color.Red,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Text(text = "Name")
                            TextField(
                                value = name,
                                onValueChange = { name = it },
                                placeholder = { Text("Name") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        focusManager.moveFocus(FocusDirection.Down)
                                    }
                                )
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Email")
                            TextField(
                                value = email,
                                onValueChange = { email = it },
                                placeholder = { Text("Email") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        focusManager.clearFocus()
                                    }
                                )
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    val updatedProfile =
                                        UserProfile(name, email, profile.profileImageUrl)
                                    viewModel.updateUserProfile(updatedProfile)
                                    isEditing = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = null)
                            }
                        } else {
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Profile details",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 22.sp
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                TextButton(onClick = {
                                    isEditing = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = null,
                                        tint = Color.Blue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Edit", style = MaterialTheme.typography.titleSmall)
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Name: ", style = MaterialTheme.typography.titleMedium)

                                Text(
                                    text = profile.name,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 20.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Email: ", style = MaterialTheme.typography.titleMedium)
                                Text(
                                    text = profile.email,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 20.sp
                                )
                            }
                        }

                    }

                    Spacer(modifier = Modifier.weight(1f))
                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        onClick = {
                            Firebase.auth.signOut()
                            navController.navigate(LoginDes.navRoute)
                        }
                    ) {
                        Text(text = "Sign out", color = MaterialTheme.colorScheme.onErrorContainer)
                    }
                }
            }
        }
    }
}

object ProfileDes : NavDestinations {
    override val navRoute = "profile"
}

