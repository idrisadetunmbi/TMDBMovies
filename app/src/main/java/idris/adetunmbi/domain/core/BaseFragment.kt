package idris.adetunmbi.domain.core

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable

open class BaseFragment : Fragment() {
    protected val compositeDisposable = CompositeDisposable()
    protected val snackBar: Snackbar? by lazy {
        view?.let {
            Snackbar.make(it, "", Snackbar.LENGTH_SHORT)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}