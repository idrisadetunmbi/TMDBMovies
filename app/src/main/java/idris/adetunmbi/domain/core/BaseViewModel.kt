package idris.adetunmbi.domain.core

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

open class BaseViewModel<T> : ViewModel() {
    protected val compositeDisposable = CompositeDisposable()
    protected val _dataSubject = PublishSubject.create<T>()

    val dataSubject: Observable<T>
        get() = _dataSubject.hide()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}