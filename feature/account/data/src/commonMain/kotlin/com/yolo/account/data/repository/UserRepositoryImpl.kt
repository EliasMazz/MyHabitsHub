package com.yolo.account.data.repository

import com.yolo.account.domain.repository.UserRepository
import com.yolo.account.domain.exceptions.UnAuthorizedException
import com.yolo.account.domain.entities.UserResponse
import com.yolo.core.data.networking.post
import com.yolo.core.data.util.ApplicationScope
import com.yolo.core.domain.util.DataError
import com.yolo.core.domain.util.fold
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class UserRepositoryImpl(
    private val applicationScope: ApplicationScope,
    private val httpClient: HttpClient
): UserRepository {
    private val currentUser = MutableStateFlow(getInitialCurrentUser())
    override fun getCurrentUser(): Flow<Result<UserResponse>> = currentUser

    init {
        observeCurrentUser()
    }

    private fun getInitialCurrentUser(): Result<UserResponse> {
        val initialUser = Firebase.auth.currentUser
        return if (initialUser == null) {
            Result.failure(UnAuthorizedException())
        } else {
            Result.success(initialUser.asUser())
        }
    }

    private fun observeCurrentUser() {
        applicationScope.launch {
            Firebase.auth.authStateChanged
                .distinctUntilChanged()
                .onEach { firebaseUser ->
                    val result = if (firebaseUser == null) {
                        Result.failure(UnAuthorizedException())
                    } else {
                        Result.success(firebaseUser.asUser())
                    }
                    currentUser.emit(result)
                }.collect {}
        }
    }

    override suspend fun logOut(){
        Firebase.auth.signOut()
    }

    //TODO send delete to the server
    override suspend fun deleteAccount() {
        val currentUser = Firebase.auth.currentUser
        currentUser?.delete()
        logOut()
    }

    override suspend fun sendAuthToken(): Result<UserResponse> {
        return httpClient.post<Unit, UserResponse>(
            route = "api/auth/google",
            body = Unit
        ).fold(
            onSuccess = { Result.success(it) },
            onFailure = { error ->
                when (error) {
                    DataError.Remote.UNAUTHORIZED,
                    DataError.Remote.FORBIDDEN -> Result.failure(UnAuthorizedException())
                    else -> Result.failure(Exception("API Error: $error"))
                }
            }
        )
    }

    /*
     TODO implement Anonymously login
     fun signInAnonymouslyIfNecessary() = applicationScope.launch {
        backgroundExecutor.execute {
            val isFirstTimeUser = userPreferences.getBoolean(KEY_FIRST_TIME_USER, true)
            if (Firebase.auth.currentUser == null && isFirstTimeUser) {
                Firebase.auth.signInAnonymously()
                userPreferences.putBoolean(KEY_FIRST_TIME_USER, false)
                AppLogger.d("Signed in anonymously")
            }
            Result.success(Unit)
        }.onFailure {
            AppLogger.e("signInAnonymouslyIfNecessary exception ${it.message}")
        }
    }

     */
    private fun FirebaseUser.asUser(): UserResponse {
        val emailFromProviders =
            providerData.firstOrNull { it.email.isNullOrEmpty().not() && it.email != "null" }?.email
        val displayNameFromProviders = providerData.firstOrNull {
            it.displayName.isNullOrEmpty().not() && it.displayName != "null"
        }?.displayName
        val pictureFromProviders = providerData.firstOrNull {
            it.photoURL.isNullOrEmpty().not() && it.photoURL != "null"
        }?.photoURL

        return UserResponse(
            id = uid,
            isAnonymous = isAnonymous,
            email = emailFromProviders ?: email,
            displayName = displayNameFromProviders ?: displayName,
            photoUrl = pictureFromProviders ?: photoURL
        )
    }
}



