package ru.yandex.practicum.moviessearch.presentation.movies

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import debounce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.yandex.practicum.moviessearch.R
import ru.yandex.practicum.moviessearch.domain.api.MoviesInteractor
import ru.yandex.practicum.moviessearch.domain.models.Movie
import ru.yandex.practicum.moviessearch.presentation.SingleLiveEvent
import ru.yandex.practicum.moviessearch.ui.details.DetailsFragment
import ru.yandex.practicum.moviessearch.ui.root.RootActivity

class MoviesViewModel(private val context: Context,
                      private val moviesInteractor: MoviesInteractor) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }
    

    private val stateLiveData = MutableLiveData<MoviesState>()
    fun observeState(): LiveData<MoviesState> = stateLiveData

    private val showToast = SingleLiveEvent<String?>()
    fun observeShowToast(): LiveData<String?> = showToast

    private var latestSearchText: String? = null

    private val onTextSearchDebounce = debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
        searchRequest(changedText)
    }



    fun searchDebounce(changedText: String) {
        if (latestSearchText != changedText) {
            latestSearchText = changedText
            onTextSearchDebounce(changedText)

        }
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(MoviesState.Loading)

            moviesInteractor.searchMovies(newSearchText, object : MoviesInteractor.MoviesConsumer {
                override fun consume(foundMovies: List<Movie>?, errorMessage: String?) {
                    val movies = mutableListOf<Movie>()
                    if (foundMovies != null) {
                        movies.addAll(foundMovies)
                    }

                    when {
                        errorMessage != null -> {
                            renderState(
                                    MoviesState.Error(
                                            message = context.getString(
                                                    R.string.something_went_wrong),
                                    )
                            )
                            showToast.postValue(errorMessage)
                        }

                        movies.isEmpty() -> {
                            renderState(
                                    MoviesState.Empty(
                                            message = context.getString(R.string.nothing_found),
                                    )
                            )
                        }

                        else -> {
                            renderState(
                                    MoviesState.Content(
                                            movies = movies,
                                    )
                            )
                        }
                    }

                }
            })
        }
    }

    private fun renderState(state: MoviesState) {
        stateLiveData.postValue(state)
    }
}