package com.bowleu.foodentify.data.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import timber.log.Timber

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: suspend () -> Flow<ResultType>, // запрос к БД
    crossinline fetch: suspend () -> RequestType, // запрос к API
    crossinline saveFetchResult: suspend (RequestType) -> Unit, // сохранение сетевого запроса в БД
    crossinline shouldFetch: (ResultType?) -> Boolean = { true } // проверка необходимости запроса
): Flow<ResultType> = flow {
    val data = query().first()
    if (shouldFetch(data)) {
        try {
            val fetched = fetch()
            saveFetchResult(fetched)
        } catch (e: Exception) {
            Timber.e("Exception happened during fetching results: ${e.message}")
        }
    }
    emitAll(query())
}