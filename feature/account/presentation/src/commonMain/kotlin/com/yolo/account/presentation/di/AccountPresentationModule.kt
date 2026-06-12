package com.yolo.account.presentation.di

import com.yolo.account.presentation.account.AccountViewModel
import com.yolo.account.presentation.helpandsupport.HelpAndSupportViewModel
import com.yolo.account.presentation.settings.SettingsViewModel
import com.yolo.account.presentation.signin.SignInViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val accountPresentationModule = module {
    viewModelOf(::SettingsViewModel)
    viewModelOf(::AccountViewModel)
    viewModelOf(::HelpAndSupportViewModel)
    viewModelOf(::SignInViewModel)
}
