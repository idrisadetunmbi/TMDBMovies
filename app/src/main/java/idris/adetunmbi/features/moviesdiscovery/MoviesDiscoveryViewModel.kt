package idris.adetunmbi.features.moviesdiscovery

import idris.adetunmbi.domain.api.Api
import idris.adetunmbi.domain.api.Resource
import idris.adetunmbi.domain.core.BaseViewModel
import idris.adetunmbi.domain.extenstions.plusAssign
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

class MoviesDiscoveryViewModel(private val api: Api) : BaseViewModel<Resource<List<Movie>>>() {
    private val _commandSubject: PublishSubject<ViewCommands> = PublishSubject.create()

    private var currentPage = 0
    private var currentSearchPage = 0
    private var searchIsActive = false
    private var isLoading = false
    private lateinit var searchQuery: String

    private val discoveryList = mutableListOf<Movie>()

    val commandSubject: Observable<ViewCommands>
        get() = _commandSubject.hide()

    fun init() {
        fetchData()
    }

    fun loadNextPage() {
        if (isLoading) return
        if (searchIsActive) {
            searchMovies()
        } else {
            fetchData(currentPage + 1)
        }
    }

    fun handleSearchSubmit(query: String) {
        searchIsActive = true
        searchQuery = query
        searchMovies()
    }

    fun handleSearchChange(newText: String) {
        if (::searchQuery.isInitialized && searchQuery.isNotEmpty() && newText.isEmpty()) {
            searchIsActive = false
            currentSearchPage = 0
            _commandSubject.onNext(ViewCommands.ClearData)
            _dataSubject.onNext(Resource.Success(discoveryList))
        }
    }

    private fun searchMovies() {
        _dataSubject.onNext(Resource.Loading())
        isLoading = true
        compositeDisposable += api.searchMovies(searchQuery, currentSearchPage + 1)
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                isLoading = false
            }
            .subscribe({
                currentSearchPage = it.page
                _dataSubject.onNext(Resource.Success(it.results))
            }, {
                _dataSubject.onNext(Resource.Error("An error occurred"))
            })
    }

    private fun fetchData(page: Int = 1) {
        _dataSubject.onNext(Resource.Loading())
        isLoading = true
        compositeDisposable +=
            api.getMovies(page)
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    isLoading = false
                }
                .subscribe({
                    currentPage = it.page
                    _dataSubject.onNext(Resource.Success(it.results))
                    discoveryList.addAll(it.results)
                }, {
                    _dataSubject.onNext(Resource.Error("An error occurred"))
                })
    }

    sealed class ViewCommands {
        object ClearData : ViewCommands()
    }
}
