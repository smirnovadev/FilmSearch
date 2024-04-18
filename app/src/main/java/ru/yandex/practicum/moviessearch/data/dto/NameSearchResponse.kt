package ru.yandex.practicum.moviessearch.data.dto

class NameSearchResponse(val searchType: String,
    val expression: String,
    val results: List<NameDto>): Response()