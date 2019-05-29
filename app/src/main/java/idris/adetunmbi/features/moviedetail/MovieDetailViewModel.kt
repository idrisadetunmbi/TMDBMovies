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
    private val _commandSubject = PublishSubject.create<ViewCommands>()

    val dataSubject: Observable<MovieResponse>
        get() = _dataSubject.hide()

    val commandSubject: Observable<ViewCommands>
        get() = _commandSubject.hide()

    fun init(movieId: Int) {
        compositeDisposable += api.getMovie(movieId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _dataSubject.onNext(it)
            }, {
                _commandSubject.onNext(ViewCommands.ShowNotification("An error occurred"))
            })
    }

    fun handleFavoriteButtonClick(movieId: Int) {
        compositeDisposable += api.favoriteMovie(
            FavoriteMovieRequestBody(
                true,
                movieId
            )
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _commandSubject.onNext(ViewCommands.ShowNotification("Movie favoured successfully"))
            }, {
                _commandSubject.onNext(ViewCommands.ShowNotification("An error occurred"))
            })
    }

    sealed class ViewCommands {
        class ShowNotification(val message: String) : ViewCommands()
    }
}