package nich.work.aequorea.di

import android.app.Application
import dagger.Module
import dagger.Provides
import nich.work.aequorea.common.di.ViewModelProviderFactory
import nich.work.aequorea.common.network.ApiService
import nich.work.aequorea.view.author.AuthorViewModel
import nich.work.aequorea.view.home.HomeViewModel

@Module
class HomeFragmentModule {
    @Provides
    fun provideViewModel(application: Application, apiService: ApiService): HomeViewModel {
        return HomeViewModel(application, apiService)
    }

    @Provides
    fun provideViewModelFactory(viewModel: HomeViewModel): ViewModelProviderFactory {
        return ViewModelProviderFactory(viewModel)
    }
}

@Module
class AuthorFragmentModule {
    @Provides
    fun provideViewModel(application: Application, apiService: ApiService): AuthorViewModel {
        return AuthorViewModel(application, apiService)
    }

    @Provides
    fun provideViewModelFactory(viewModel: AuthorViewModel): ViewModelProviderFactory {
        return ViewModelProviderFactory(viewModel)
    }
}