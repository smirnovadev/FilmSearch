package ru.yandex.practicum.moviessearch.domain.api

import kotlinx.coroutines.flow.Flow
import ru.yandex.practicum.moviessearch.domain.models.Person
import ru.yandex.practicum.moviessearch.util.Resource

interface NamesRepository {
    fun searchName(expression: String): Resource<List<Person>>

}