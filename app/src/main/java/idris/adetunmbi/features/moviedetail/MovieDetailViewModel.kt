package idris.adetunmbi.features.moviedetail

import idris.adetunmbi.domain.api.Api
import idris.adetunmbi.domain.api.Resource
import idris.adetunmbi.domain.core.BaseViewModel
import idris.adetunmbi.domain.extenstions.plusAssign
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

class MovieDetailViewModel(private val api: Api) : BaseViewModel<Resource<MovieResponse>>() {
    private val _commandSubject = PublishSubject.create<ViewCommands>()

    val commandSubject: Observable<ViewCommands>
        get() = _commandSubject.hide()

    fun init(movieId: Int) {
        _dataSubject.onNext(Resource.Loading())
        compositeDisposable += api.getMovie(movieId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _dataSubject.onNext(Resource.Success(it))
            }, {
                _dataSubject.onNext(Resource.Error("An error occurred"))
            })
    }

    fun handleFavoriteButtonClick(movieId: Int) {
        _dataSubject.onNext(Resource.Loading())
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