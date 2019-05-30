package idris.adetunmbi.features.moviesdiscovery

import idris.adetunmbi.domain.api.Api
import idris.adetunmbi.domain.api.Resource
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.lang.Exception

@RunWith(MockitoJUnitRunner::class)
class MoviesDiscoveryViewModelTest {

    @Mock
    private lateinit var api: Api

    private lateinit var viewModel: MoviesDiscoveryViewModel

    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun `viewModel init makes the expected api call`() {
        val stubMovie = Movie(1, "overview", "", "", false)
        val results = listOf(stubMovie)
        val stubMovieResponse = MoviesListResponse(1, results)

        Mockito.`when`(api.getMovies()).thenReturn(Single.just(stubMovieResponse))

        viewModel = MoviesDiscoveryViewModel(api)
        viewModel.init()

        Mockito.verify(api).getMovies(1)
    }

    @Test
    fun `viewModel init sends the expected data on success`() {
        val testSubscriber by lazy { TestObserver<Resource<List<Movie>>>() }

        val stubMovie = Movie(1, "overview", "", "", false)
        val results = listOf(stubMovie)
        val stubMovieResponse = MoviesListResponse(1, results)

        Mockito.`when`(api.getMovies()).thenReturn(Single.just(stubMovieResponse))


        viewModel = MoviesDiscoveryViewModel(api)
        viewModel.dataSubject.subscribe(testSubscriber)
        viewModel.init()

        testSubscriber.assertValueCount(2)
        testSubscriber.assertValueAt(0) { it is Resource.Loading }
        testSubscriber.assertValueAt(1) { it.data == results }
    }

    @Test
    fun `viewModel init sends the expected data on failure`() {
        val testSubscriber by lazy { TestObserver<Resource<List<Movie>>>() }

        Mockito.`when`(api.getMovies()).thenReturn(Single.error(Exception()))

        viewModel = MoviesDiscoveryViewModel(api)
        viewModel.dataSubject.subscribe(testSubscriber)
        viewModel.init()

        testSubscriber.assertValueCount(2)
        testSubscriber.assertValueAt(0) { it is Resource.Loading }
        testSubscriber.assertValueAt(1) { it is Resource.Error }
    }

    @Test
    fun `viewModel loadNextPage calls the expected api method`() {
        Mockito.`when`(api.getMovies()).thenReturn(Single.error(Exception()))
        viewModel = MoviesDiscoveryViewModel(api)
        viewModel.loadNextPage()

        Mockito.verify(api).getMovies(1)
    }

    @Test
    fun `viewModel calls api method to search movies when invoked`() {
        Mockito.`when`(api.searchMovies(Mockito.anyString(), Mockito.anyInt())).thenReturn(Single.error(Exception()))
        viewModel = MoviesDiscoveryViewModel(api)
        val testSearchQuery = "random movie"
        viewModel.handleSearchSubmit(testSearchQuery)

        Mockito.verify(api).searchMovies(testSearchQuery, 1)
    }

    @Test
    fun `viewModel init sends the expected data on search movies success success`() {
        val testSubscriber = TestObserver<Resource<List<Movie>>>()

        val stubMovie = Movie(1, "overview", "", "", false)
        val results = listOf(stubMovie)
        val stubMovieResponse = MoviesListResponse(1, results)

        Mockito.`when`(api.searchMovies(Mockito.anyString(), Mockito.anyInt())).thenReturn(Single.just(stubMovieResponse))


        viewModel = MoviesDiscoveryViewModel(api)
        viewModel.dataSubject.subscribe(testSubscriber)
        viewModel.handleSearchSubmit("")

        testSubscriber.assertValueCount(2)
        testSubscriber.assertValueAt(0) { it is Resource.Loading }
        testSubscriber.assertValueAt(1) { it.data == results }
    }

    @Test
    fun `viewModel resets view data when search is cleared`() {
        val testSubscriber = TestObserver<Resource<List<Movie>>>()
        val testCommandSubscriber = TestObserver<MoviesDiscoveryViewModel.ViewCommands>()

        Mockito.`when`(api.searchMovies(Mockito.anyString(), Mockito.anyInt())).thenReturn(Single.error(Exception()))

        viewModel = MoviesDiscoveryViewModel(api)
        viewModel.dataSubject.subscribe(testSubscriber)
        viewModel.commandSubject.subscribe(testCommandSubscriber)
        viewModel.handleSearchSubmit("random query")

        viewModel.handleSearchChange("")
        testCommandSubscriber.assertValueAt(0) { it is MoviesDiscoveryViewModel.ViewCommands.ClearData }
        testSubscriber.assertValueAt(testSubscriber.valueCount() - 1) { it is Resource.Success && it.data?.size == 0 }
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }
}