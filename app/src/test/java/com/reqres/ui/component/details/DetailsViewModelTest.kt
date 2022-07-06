package com.reqres.ui.component.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.reqres.data.DataRepository
import com.reqres.data.Resource
import com.util.InstantExecutorExtension
import com.util.MainCoroutineRule
import com.util.TestModelsGenerator
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class DetailsViewModelTest {
    // Subject under test
    private lateinit var detailsViewModel: DetailsViewModel

    // Use a fake UseCase to be injected into the viewModel
    private val dataRepository: DataRepository = mockk()

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val testModelsGenerator: TestModelsGenerator = TestModelsGenerator()

    @Test
    fun testInitIntentData() {
        //1- Mock Data
        val userItem = testModelsGenerator.generateUsersItemModel()
        //2-Call
        detailsViewModel = DetailsViewModel(dataRepository)
        detailsViewModel.initIntentData(userItem)
        //active observer for livedata
        detailsViewModel.userData.observeForever { }

        //3-verify
        val usersData = detailsViewModel.userData.value
        assertEquals(userItem, usersData)
    }

    @Test
    fun testAddToFavourites() {
        //1- Mock calls
        val isFavourites = true
        val usersItem = testModelsGenerator.generateUsersItemModel()
        coEvery { dataRepository.addToFavourite(usersItem.id) } returns flow {
            emit(Resource.Success(true))
        }
        //2-Call
        detailsViewModel = DetailsViewModel(dataRepository)
        detailsViewModel.userPrivate.value = usersItem
        detailsViewModel.addToFavourites()
        //active observer for livedata
        detailsViewModel.isFavourite.observeForever { }

        //3-verify
        val usersData = detailsViewModel.isFavourite.value
        assertEquals(isFavourites, usersData?.data)
    }

    @Test
    fun testRemoveFromFavourites() {
        //1- Mock calls
        val isFavourites = false
        val userItem = testModelsGenerator.generateUsersItemModel()
        coEvery { dataRepository.removeFromFavourite(userItem.id) } returns flow {
            emit(Resource.Success(true))
        }
        //2-Call
        detailsViewModel = DetailsViewModel(dataRepository)
        detailsViewModel.userPrivate.value = userItem
        detailsViewModel.removeFromFavourites()
        //active observer for livedata
        detailsViewModel.isFavourite.observeForever { }

        //3-verify
        val usersData = detailsViewModel.isFavourite.value
        assertEquals(isFavourites, usersData?.data)
    }

    @Test
    fun testIsFavourite() {
        //1- Mock calls
        val isFavourites = true
        val userItem = testModelsGenerator.generateUsersItemModel()
        coEvery { dataRepository.isFavourite(userItem.id) } returns flow {
            emit(Resource.Success(true))
        }
        //2-Call
        detailsViewModel = DetailsViewModel(dataRepository)
        detailsViewModel.userPrivate.value = userItem
        detailsViewModel.isFavourites()
        //active observer for livedata
        detailsViewModel.isFavourite.observeForever { }

        //3-verify
        val usersData = detailsViewModel.isFavourite.value
        assertEquals(isFavourites, usersData?.data)
    }
}
