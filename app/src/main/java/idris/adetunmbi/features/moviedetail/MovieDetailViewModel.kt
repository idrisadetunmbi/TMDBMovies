package idris.adetunmbi.features.moviedetail

import androidx.lifecycle.ViewModel
import idris.adetunmbi.domain.api.Api
import idris.adetunmbi.domain.extenstions.plusAssign
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class MovieDetailViewModel(private val api: Api) : ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val _dataSubject = PublishSubject.create<MovieResponse>()

    val dataSubject: Observable<MovieResponse>
        get() = _dataSubject.hide()

    fun init(movieId: Int) {
        compositeDisposable += api.getMovie(movieId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _dataSubject.onNext(it)
            }, {

            })
    }
}