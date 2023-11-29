package com.example.mail_list.presentation.di

import com.example.core.dagger.FeatureScoped
import com.example.core.dependency.Dependencies
import com.example.mail_list.domain.MailListInteractor
import com.example.mail_list.presentation.ui.MailListViewModel
import dagger.Component

@FeatureScoped
@Component(
    dependencies = [MailListDeps::class]
)
interface MailListComponent {
    @Component.Builder
    interface Builder {
        fun mailListDeps(deps: MailListDeps): Builder
        fun build(): MailListComponent
    }
    val mailListViewModel: MailListViewModel
}

interface MailListDeps : Dependencies {
    val mailListInteractor: MailListInteractor
}
