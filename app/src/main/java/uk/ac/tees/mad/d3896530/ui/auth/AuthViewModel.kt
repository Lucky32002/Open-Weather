package uk.ac.tees.mad.d3896530.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3896530.data.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<Result<Boolean>?>(null)
    val loginState: StateFlow<Result<Boolean>?> = _loginState

    private val _registerState = MutableStateFlow<Result<Boolean>?>(null)
    val registerState: StateFlow<Result<Boolean>?> = _registerState


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authRepository.login(email, password)
            _loginState.value = result
            _isLoading.value = false
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authRepository.register(name, email, password)
            _registerState.value = result
            _isLoading.value = false
        }
    }
}

