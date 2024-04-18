package ru.yandex.practicum.moviessearch.domain.api

import kotlinx.coroutines.flow.Flow
import ru.yandex.practicum.moviessearch.domain.models.Person

interface NamesInteractor {
    fun searchName(expression: String, consumer: NameConsumer)
    interface NameConsumer {
        fun consume(searchName: List<Person>?, errorMessage: String?)
    }
    fun searchNamesFlowInteractor(expression: String): Flow<Pair<List<Person>?, String?>>
}