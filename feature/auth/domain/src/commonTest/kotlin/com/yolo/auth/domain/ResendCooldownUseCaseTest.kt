package com.yolo.auth.domain

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ResendCooldownUseCaseTest {

    @Test
    fun countsDownOncePerSecondAndEndsAtZero() = runTest {
        val useCase = ResendCooldownStreamUseCase(StandardTestDispatcher(testScheduler))

        val emissions = useCase(3).toList()

        assertEquals(listOf(3, 2, 1, 0), emissions)
        assertEquals(3_000, currentTime)
    }

    @Test
    fun zeroSecondsEmitsOnlyZeroImmediately() = runTest {
        val useCase = ResendCooldownStreamUseCase(StandardTestDispatcher(testScheduler))

        val emissions = useCase(0).toList()

        assertEquals(listOf(0), emissions)
        assertEquals(0, currentTime)
    }
}
