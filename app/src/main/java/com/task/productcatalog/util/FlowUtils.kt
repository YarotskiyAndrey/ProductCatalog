package com.task.productcatalog.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

fun <T> requestUntilSuccessFlow(
    ioDispatcher: CoroutineDispatcher,
    errorChannel: SendChannel<Exception>,
    request: suspend () -> Resource<T>,
    retryDelay: Long = RETRY_REQUEST_DELAY
) =
    flow {
        while (true) {
            when (val result = withContext(ioDispatcher) { request() }) {
                is Resource.Success -> {
                    emit(result.value)
                    break
                }
                is Resource.Failure -> {
                    errorChannel.send(result.exception)
                    delay(retryDelay)
                }
            }
        }
    }

const val RETRY_REQUEST_DELAY = 5000L
const val STOP_FLOW_DELAY = 3000L