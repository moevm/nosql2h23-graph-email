package com.example.login.presentation.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.domain.Entity
import com.example.login.domain.LoginEntity
import com.example.login.domain.LoginInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginInteractor: LoginInteractor
) : ViewModel(){
    private val _importSuccessful = MutableStateFlow(false)
    val inputSuccessful = _importSuccessful.asStateFlow()

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

    fun import(){
        viewModelScope.launch(Dispatchers.IO) {
            when(val response = loginInteractor.import()){
                is Entity.Success -> {
                    _importSuccessful.value = true
                    Log.d("Import", "Success")
                }
                is Entity.Error -> {
                    _importSuccessful.value = false
                    Log.d("Import", "response.message ${response.message}")
                }
            }
        }
    }
}