package idris.adetunmbi.features.favoritemovies

import idris.adetunmbi.domain.api.Api
import idris.adetunmbi.domain.core.BaseViewModel
import idris.adetunmbi.domain.extenstions.plusAssign
import idris.adetunmbi.features.moviesdiscovery.MoviesListResponse
import io.reactivex.android.schedulers.AndroidSchedulers

class FavoriteMoviesViewModel(api: Api) : BaseViewModel<MoviesListResponse>() {
    init {
        compositeDisposable += api
            .getFavoriteMovies()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _dataSubject.onNext(it)
            }, {

            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}