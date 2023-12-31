package com.example.graphemail.dagger.component

import com.example.filter.presentation.di.FilterDeps
import com.example.graph.presentation.di.GraphDeps
import com.example.login.presentation.di.LoginDeps
import com.example.mail_list.presentation.di.MailListDeps

interface FeaturesScreenDeps :
    LoginDeps,
    MailListDeps,
    FilterDeps,
    GraphDeps