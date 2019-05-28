package idris.adetunmbi.features.moviesdiscovery

import androidx.lifecycle.ViewModel
import idris.adetunmbi.domain.api.Api
import idris.adetunmbi.domain.api.Resource
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class MoviesDiscoveryViewModel(private val api: Api) : ViewModel() {
    private val _dataSubject: PublishSubject<Resource<List<Movie>>> = PublishSubject.create()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private var currentPage = 0

    val dataSubject: Observable<Resource<List<Movie>>>
        get() = _dataSubject.hide()

    init {
        fetchData()
    }

    fun loadNextPage() = fetchData(currentPage + 1)

    private fun fetchData(page: Int = 1) {
        _dataSubject.onNext(Resource.Loading())
        compositeDisposable.add(
            api.getMovies(page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    currentPage = it.page
                    _dataSubject.onNext(Resource.Success(it.results))
                }, {
                    _dataSubject.onNext(Resource.Error("An error occurred"))
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}