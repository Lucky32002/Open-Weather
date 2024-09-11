package uk.ac.tees.mad.d3896530.ui.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import uk.ac.tees.mad.d3896530.data.repository.AuthRepository
import uk.ac.tees.mad.d3896530.models.UserProfile
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: AuthRepository,
    auth: FirebaseAuth
) : ViewModel() {

    private val _userProfile = MutableStateFlow(UserProfile("", "", ""))
    val userProfile = _userProfile.asStateFlow()

    private val userId = auth.currentUser?.uid

    fun fetchUserProfile() {
        userId?.let {
            repository.getUserProfile(userId, { profile ->
                _userProfile.value = profile
            }, { exception ->
                exception.printStackTrace()
            })
        }
    }

    fun updateUserProfile(userProfile: UserProfile) {
        userId?.let {
            repository.updateUserProfile(userId, userProfile, {

            }, { exception ->
                exception.printStackTrace()
            })
            fetchUserProfile()
        }
    }

    fun uploadProfileImage(imageUri: Uri) {
        if (userId != null) {
            repository.uploadProfileImage(userId, imageUri, { imageUrl ->
                val updatedProfile = _userProfile.value.copy(profileImageUrl = imageUrl)
                updateUserProfile(updatedProfile)
            }, { exception ->
                exception.printStackTrace()
            })
        }
    }
}
