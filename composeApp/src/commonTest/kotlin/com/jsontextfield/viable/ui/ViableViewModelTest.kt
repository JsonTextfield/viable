@file:OptIn(ExperimentalCoroutinesApi::class)

package com.jsontextfield.viable.ui

import com.jsontextfield.viable.data.database.entities.Shape
import com.jsontextfield.viable.data.database.entities.Station
import com.jsontextfield.viable.data.model.Train
import com.jsontextfield.viable.data.repositories.ITrainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ViableViewModelTest {

    private lateinit var viableViewModel: ViableViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())

        viableViewModel = ViableViewModel(repo = object : ITrainRepository {
            override suspend fun getTrains(): List<Train> {
                return emptyList()
            }

            override suspend fun getStation(id: String): Station? {
                return null
            }

            override suspend fun getLine(id: String): List<Shape> {
                return emptyList()
            }

        })
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testOnTrainSelected() = runTest {
        assertEquals(null, viableViewModel.viableState.value.selectedTrain)
        viableViewModel.onTrainSelected(Train(number = "45", from = "Ottawa", to = "Toronto"))
        viableViewModel.stop()
        advanceTimeBy(1000)
        assertNotNull(viableViewModel.viableState.value.selectedTrain)
        assertEquals("45", viableViewModel.viableState.value.selectedTrain?.number)
    }

    @Test
    fun testOnStopSelected() = runTest {

    }
}