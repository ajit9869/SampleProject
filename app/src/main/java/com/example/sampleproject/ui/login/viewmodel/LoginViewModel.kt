package com.example.sampleproject.ui.login.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sampleproject.R
import com.example.sampleproject.api.ApiInterface
import com.example.sampleproject.data.LoginRepository
import com.example.sampleproject.data.Result
import com.example.sampleproject.model.Resource
import com.example.sampleproject.model.SignUpRequestModel
import com.example.sampleproject.model.SignupResponseModel
import com.example.sampleproject.ui.login.LoginFormState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<SignupResponseModel>()
    val loginResult: LiveData<SignupResponseModel> = _loginResult

    fun login(username: String, job: String) {

        viewModelScope.launch {
            signup(SignUpRequestModel(name = username, job = job)).onEach { dataState ->

                if(dataState.isSuccessful)
                {
                    _loginResult.value = dataState.body()
                }else
                {
                    _loginResult.value = SignupResponseModel(error = R.string.login_failed)
                }
            }.launchIn(viewModelScope)
        }
    }

    fun loginDataChanged(username: String, job: String) {
        when {
            username.isEmpty() -> {
                _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
            }
            job.isEmpty() -> {
                _loginForm.value = LoginFormState(passwordError = R.string.invalid_job)
            }
            else -> {
                _loginForm.value = LoginFormState(isDataValid = true)
            }
        }
    }



    suspend fun signup(
        code: SignUpRequestModel
    ): Flow<Response<SignupResponseModel>> = flow {

        val apiInterface = ApiInterface.create()

        val data =
            withContext(Dispatchers.IO) {
                apiInterface.signupUser(code)
            }

        emit(data)

    }
}