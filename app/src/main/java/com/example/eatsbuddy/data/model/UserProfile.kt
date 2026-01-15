package com.example.eatsbuddy.data.model

data class UserProfile(
    val uid: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val profilePictureUrl: String? = null,
    val isProfileComplete: Boolean = false
)
