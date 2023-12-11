package com.example.graphemail.dagger.deps

import dagger.Module

@Module(
    includes = [
        LoginDepsModule::class,
        MailListDepsModule::class,
        FilterDepsModule::class,
    ]
)
interface FeatureDepsModule