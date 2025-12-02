package com.example.eatsbuddy.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eatsbuddy.data.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class AuthState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val user: FirebaseUser? = null,
    val userProfile: UserProfile? = null,
    val isNewUser: Boolean = false,
    val error: String? = null
)

class AuthViewModel : ViewModel() {
    
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    init {
        // Check if user is already logged in
        checkAuthStatus()
    }
    
    private fun checkAuthStatus() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            viewModelScope.launch {
                loadUserProfile(currentUser)
            }
        } else {
            _authState.value = AuthState(
                isAuthenticated = false,
                user = null
            )
        }
    }
    
    private suspend fun loadUserProfile(user: FirebaseUser) {
        try {
            val document = firestore.collection("users")
                .document(user.uid)
                .get()
                .await()
            
            val profile = if (document.exists()) {
                UserProfile(
                    uid = user.uid,
                    email = user.email ?: "",
                    firstName = document.getString("firstName") ?: "",
                    lastName = document.getString("lastName") ?: "",
                    profilePictureUrl = document.getString("profilePictureUrl"),
                    isProfileComplete = document.getBoolean("isProfileComplete") ?: false
                )
            } else {
                null
            }
            
            _authState.value = AuthState(
                isAuthenticated = true,
                user = user,
                userProfile = profile,
                isNewUser = profile == null || !profile.isProfileComplete
            )
        } catch (e: Exception) {
            _authState.value = AuthState(
                isAuthenticated = true,
                user = user,
                isNewUser = true
            )
        }
    }
    
    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = _authState.value.copy(error = "Please fill in all fields")
            return
        }
        
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                result.user?.let { user ->
                    loadUserProfile(user)
                }
            } catch (e: Exception) {
                _authState.value = AuthState(
                    isLoading = false,
                    isAuthenticated = false,
                    error = getErrorMessage(e)
                )
            }
        }
    }
    
    fun register(email: String, password: String, confirmPassword: String) {
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _authState.value = _authState.value.copy(error = "Please fill in all fields")
            return
        }
        
        if (password != confirmPassword) {
            _authState.value = _authState.value.copy(error = "Passwords do not match")
            return
        }
        
        if (password.length < 6) {
            _authState.value = _authState.value.copy(error = "Password must be at least 6 characters")
            return
        }
        
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                _authState.value = AuthState(
                    isLoading = false,
                    isAuthenticated = true,
                    user = result.user,
                    isNewUser = true
                )
            } catch (e: Exception) {
                _authState.value = AuthState(
                    isLoading = false,
                    isAuthenticated = false,
                    error = getErrorMessage(e)
                )
            }
        }
    }
    
    fun saveUserProfile(firstName: String, lastName: String, imageUri: Uri?) {
        val user = auth.currentUser ?: return
        
        if (firstName.isBlank() || lastName.isBlank()) {
            _authState.value = _authState.value.copy(error = "Please fill in your name")
            return
        }
        
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            
            try {
                var profilePictureUrl: String? = null
                
                // Upload image if provided (skip if fails)
                if (imageUri != null) {
                    try {
                        val storageRef = storage.reference
                            .child("profile_pictures")
                            .child("${user.uid}.jpg")
                        
                        storageRef.putFile(imageUri).await()
                        profilePictureUrl = storageRef.downloadUrl.await().toString()
                    } catch (e: Exception) {
                        // Skip image upload if it fails, continue with profile save
                        android.util.Log.e("AuthViewModel", "Image upload failed: ${e.message}")
                    }
                }
                
                // Save profile to Firestore
                val profileData = hashMapOf(
                    "uid" to user.uid,
                    "email" to (user.email ?: ""),
                    "firstName" to firstName,
                    "lastName" to lastName,
                    "profilePictureUrl" to profilePictureUrl,
                    "isProfileComplete" to true
                )
                
                firestore.collection("users")
                    .document(user.uid)
                    .set(profileData)
                    .await()
                
                val profile = UserProfile(
                    uid = user.uid,
                    email = user.email ?: "",
                    firstName = firstName,
                    lastName = lastName,
                    profilePictureUrl = profilePictureUrl,
                    isProfileComplete = true
                )
                
                _authState.value = AuthState(
                    isLoading = false,
                    isAuthenticated = true,
                    user = user,
                    userProfile = profile,
                    isNewUser = false
                )
            } catch (e: Exception) {
                android.util.Log.e("AuthViewModel", "Save profile failed: ${e.message}", e)
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    error = "Failed to save profile. Please make sure Firestore is enabled in Firebase Console."
                )
            }
        }
    }
    
    fun updateUserProfile(firstName: String, lastName: String, imageUri: Uri?) {
        val user = auth.currentUser ?: return
        val currentProfile = _authState.value.userProfile
        
        if (firstName.isBlank() || lastName.isBlank()) {
            _authState.value = _authState.value.copy(error = "Please fill in your name")
            return
        }
        
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            
            try {
                var profilePictureUrl = currentProfile?.profilePictureUrl
                
                // Upload new image if provided
                if (imageUri != null) {
                    val storageRef = storage.reference
                        .child("profile_pictures")
                        .child("${user.uid}.jpg")
                    
                    storageRef.putFile(imageUri).await()
                    profilePictureUrl = storageRef.downloadUrl.await().toString()
                }
                
                // Update profile in Firestore
                val updates = hashMapOf<String, Any?>(
                    "firstName" to firstName,
                    "lastName" to lastName,
                    "profilePictureUrl" to profilePictureUrl
                )
                
                firestore.collection("users")
                    .document(user.uid)
                    .update(updates as Map<String, Any>)
                    .await()
                
                val profile = UserProfile(
                    uid = user.uid,
                    email = user.email ?: "",
                    firstName = firstName,
                    lastName = lastName,
                    profilePictureUrl = profilePictureUrl,
                    isProfileComplete = true
                )
                
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    userProfile = profile,
                    error = null
                )
            } catch (e: Exception) {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    error = "Failed to update profile: ${e.message}"
                )
            }
        }
    }
    
    fun logout() {
        auth.signOut()
        _authState.value = AuthState(
            isAuthenticated = false,
            user = null,
            userProfile = null,
            isNewUser = false
        )
    }
    
    fun clearError() {
        _authState.value = _authState.value.copy(error = null)
    }
    
    fun clearNewUserFlag() {
        _authState.value = _authState.value.copy(isNewUser = false)
    }
    
    private fun getErrorMessage(exception: Exception): String {
        return when {
            exception.message?.contains("email address is badly formatted") == true -> 
                "Invalid email format"
            exception.message?.contains("no user record") == true -> 
                "No account found with this email"
            exception.message?.contains("password is invalid") == true -> 
                "Incorrect password"
            exception.message?.contains("email address is already in use") == true -> 
                "An account already exists with this email"
            exception.message?.contains("network error") == true -> 
                "Network error. Please check your connection"
            exception.message?.contains("too many requests") == true ->
                "Too many attempts. Please try again later"
            else -> exception.message ?: "An error occurred"
        }
    }
}
