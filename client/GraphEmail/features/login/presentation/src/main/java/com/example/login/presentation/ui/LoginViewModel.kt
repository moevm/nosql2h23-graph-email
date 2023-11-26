package com.example.login.presentation.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.domain.Entity
import com.example.login.domain.LoginEntity
import com.example.login.domain.LoginInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginInteractor: LoginInteractor
) : ViewModel(){

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when(val response = loginInteractor.login(
                LoginEntity(
                    email = email,
                    password = password
                )
            )){
                is Entity.Success -> {
                    Log.d("Login", "Success")
                }
                is Entity.Error -> {
                    Log.d("Login", "response.message ${response.message}")
                }
            }
        }
    }
}