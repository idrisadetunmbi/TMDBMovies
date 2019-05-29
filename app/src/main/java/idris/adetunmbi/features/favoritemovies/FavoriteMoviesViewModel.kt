package idris.adetunmbi.features.favoritemovies

import androidx.lifecycle.ViewModel
import idris.adetunmbi.domain.api.Api
import idris.adetunmbi.domain.extenstions.plusAssign
import idris.adetunmbi.features.moviesdiscovery.MoviesListResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class FavoriteMoviesViewModel(api: Api) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val _dataSubject = PublishSubject.create<MoviesListResponse>()

    val dataSubject: Observable<MoviesListResponse>
        get() = _dataSubject.hide()

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