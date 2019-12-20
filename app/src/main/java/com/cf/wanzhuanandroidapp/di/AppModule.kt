package com.cf.wanzhuanandroidapp.di

import com.cf.wanzhuanandroidapp.model.respository.*
import com.cf.wanzhuanandroidapp.ui.home.ArticleViewModel
import com.cf.wanzhuanandroidapp.ui.login.LoginViewModel
import com.cf.wanzhuanandroidapp.ui.navigation.NavigationViewModel
import com.cf.wanzhuanandroidapp.ui.project.ProjectViewModel
import com.cf.wanzhuanandroidapp.ui.search.SearchViewModel
import com.cf.wanzhuanandroidapp.ui.system.SystemViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ArticleViewModel(get(), get(), get(), get(), get()) }
    viewModel { SystemViewModel(get(), get()) }
    viewModel { NavigationViewModel(get()) }
    viewModel { SearchViewModel(get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { ProjectViewModel(get()) }
}

val repositoryModule = module {
    single { HomeRepository() }
    single { CollectRepository() }
    single { ProjectRepository() }
    single { SquareRepository() }
    single { SystemRepository() }
    single { NavigationRepository() }
    single { SearchRepository() }
    single { LoginRepository() }
}

val appModule = listOf(viewModelModule, repositoryModule)