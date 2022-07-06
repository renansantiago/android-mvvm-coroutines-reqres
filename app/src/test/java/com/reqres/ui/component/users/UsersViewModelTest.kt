package com.reqres.ui.component.users

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.reqres.data.DataRepository
import com.reqres.data.Resource
import com.reqres.data.dto.users.Users
import com.reqres.data.error.NETWORK_ERROR
import com.util.InstantExecutorExtension
import com.util.MainCoroutineRule
import com.util.TestModelsGenerator
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class UsersViewModelTest {
    // Subject under test
    private lateinit var usersViewModel: UsersViewModel

    // Use a fake UseCase to be injected into the viewModel
    private val dataRepository: DataRepository = mockk()

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var userName: String
    private val testModelsGenerator: TestModelsGenerator = TestModelsGenerator()

    @Before
    fun setUp() {
        // Create class under test
        // Initializing the repository with no tasks
        userName = testModelsGenerator.getStubSearchTitle()
    }

    @Test
    fun testFetchUsersList() {
        // Let's do an answer for the liveData
        val usersModel = testModelsGenerator.generateUsers()

        //1- Mock calls
        coEvery { dataRepository.requestUsers(1) } returns flow {
            emit(Resource.Success(usersModel))
        }

        //2-Call
        usersViewModel = UsersViewModel(dataRepository)
        usersViewModel.getUsers()
        //active observer for livedata
        usersViewModel.usersLiveData.observeForever { }

        //3-verify
        val isEmptyList = usersViewModel.usersLiveData.value?.data?.usersList.isNullOrEmpty()
        assertEquals(usersModel, usersViewModel.usersLiveData.value?.data)
        assertEquals(false, isEmptyList)
    }

    @Test
    fun testFetchUsersListEmpty() {
        // Let's do an answer for the liveData
        val usersModel = testModelsGenerator.generateUsersModelWithEmptyList()

        //1- Mock calls
        coEvery { dataRepository.requestUsers(1) } returns flow {
            emit(Resource.Success(usersModel))
        }

        //2-Call
        usersViewModel = UsersViewModel(dataRepository)
        usersViewModel.getUsers()
        //active observer for livedata
        usersViewModel.usersLiveData.observeForever { }

        //3-verify
        val isEmptyList = usersViewModel.usersLiveData.value?.data?.usersList.isNullOrEmpty()
        assertEquals(usersModel, usersViewModel.usersLiveData.value?.data)
        assertEquals(true, isEmptyList)
    }

    @Test
    fun testFetchUsersError() {
        // Let's do an answer for the liveData
        val error: Resource<Users> = Resource.DataError(NETWORK_ERROR)

        //1- Mock calls
        coEvery { dataRepository.requestUsers(1) } returns flow {
            emit(error)
        }

        //2-Call
        usersViewModel = UsersViewModel(dataRepository)
        usersViewModel.getUsers()
        //active observer for livedata
        usersViewModel.usersLiveData.observeForever { }

        //3-verify
        assertEquals(NETWORK_ERROR, usersViewModel.usersLiveData.value?.errorCode)
    }

    @Test
    fun testSearchSuccess() {
        // Let's do an answer for the liveData
        val user = testModelsGenerator.generateUsersItemModel()
        val title = user.fullName
        //1- Mock calls
        usersViewModel = UsersViewModel(dataRepository)
        usersViewModel.usersLiveDataPrivate.value = Resource.Success(testModelsGenerator.generateUsers())

        //2-Call
        usersViewModel.onSearchClick(title)
        //active observer for livedata
        usersViewModel.userSearchFound.observeForever { }

        //3-verify
        assertEquals(user, usersViewModel.userSearchFound.value)
    }

    @Test
    fun testSearchFail() {
        // Wrong search
        val title = "*&*^%"

        //1- Mock calls
        usersViewModel = UsersViewModel(dataRepository)
        usersViewModel.usersLiveDataPrivate.value = Resource.Success(testModelsGenerator.generateUsers())

        //2-Call
        usersViewModel.onSearchClick(title)
        //active observer for livedata
        usersViewModel.noSearchFound.observeForever { }

        //3-verify
        assertEquals(Unit, usersViewModel.noSearchFound.value)
    }
}
