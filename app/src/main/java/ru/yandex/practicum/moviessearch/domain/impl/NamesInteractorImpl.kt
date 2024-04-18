package ru.yandex.practicum.moviessearch.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.yandex.practicum.moviessearch.domain.api.NamesInteractor
import ru.yandex.practicum.moviessearch.domain.api.NamesRepository
import ru.yandex.practicum.moviessearch.domain.models.Person
import ru.yandex.practicum.moviessearch.util.Resource
import java.util.concurrent.Executors

class NamesInteractorImpl(private val repository: NamesRepository): NamesInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun searchName(expression: String, consumer: NamesInteractor.NameConsumer) {
       executor.execute {
           when(val resource = repository.searchName(expression)) {
               is Resource.Success -> { consumer.consume(resource.data, null) }
               is Resource.Error -> { consumer.consume(resource.data, resource.message) }
           }

       }
    }

    override fun searchNamesFlowInteractor(expression: String): Flow<Pair<List<Person>?, String?>> {
        return repository.searchNamesFlow(expression).map { result ->
            when(result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

}