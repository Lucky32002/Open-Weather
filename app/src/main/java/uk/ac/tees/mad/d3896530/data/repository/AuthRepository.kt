package uk.ac.tees.mad.d3896530.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.d3896530.models.UserProfile

class AuthRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    suspend fun login(email: String, password: String): Result<Boolean> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(name: String, email: String, password: String): Result<Boolean> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = authResult.user ?: throw Exception("User registration failed")
            firestore.collection("users").document(user.uid).set(
                hashMapOf(
                    "name" to name,
                    "email" to email
                )
            ).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getUserProfile(
        userId: String,
        onSuccess: (UserProfile) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                val userProfile = document.toObject(UserProfile::class.java)
                if (userProfile != null) {
                    onSuccess(userProfile)
                } else {
                    onFailure(Exception("User profile not found"))
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun updateUserProfile(
        userId: String,
        userProfile: UserProfile,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection("users").document(userId).set(userProfile)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun uploadProfileImage(
        userId: String,
        imageUri: Uri,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val storageRef = storage.reference.child("profile_images/$userId.jpg")
        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    onSuccess(uri.toString())
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}

