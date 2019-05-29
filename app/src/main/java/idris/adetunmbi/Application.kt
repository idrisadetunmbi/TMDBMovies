package idris.adetunmbi

import android.app.Application
import idris.adetunmbi.domain.api.Api
import idris.adetunmbi.features.moviesdiscovery.MoviesDiscoveryViewModel
import idris.adetunmbi.domain.api.WebServiceGenerator
import idris.adetunmbi.features.moviedetail.MovieDetailViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@Application)
            modules(module {
                single { WebServiceGenerator(applicationContext).createService<Api>() }
                viewModel { MoviesDiscoveryViewModel(get()) }
                viewModel { MovieDetailViewModel(get()) }
            })
        }
    }
}