package com.reqres.ui.component.users

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.reqres.data.DataRepositorySource
import com.reqres.data.Resource
import com.reqres.data.dto.users.Users
import com.reqres.data.dto.users.User
import com.reqres.ui.base.BaseViewModel
import com.reqres.utils.SingleEvent
import com.reqres.utils.wrapEspressoIdlingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Locale.ROOT
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject
constructor(private val dataRepository: DataRepositorySource) : BaseViewModel() {

    /**
     * Controls pagination
     */
    var usersPage: Int = 0

    /**
     * Data --> LiveData, Exposed as LiveData, Locally in viewModel as MutableLiveData
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val usersLiveDataPrivate = MutableLiveData<Resource<Users>>()
    val usersLiveData: LiveData<Resource<Users>> get() = usersLiveDataPrivate

    //TODO - Check to make live data as one resource
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val userSearchFoundPrivate: MutableLiveData<User> = MutableLiveData()
    val userSearchFound: LiveData<User> get() = userSearchFoundPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val noSearchFoundPrivate: MutableLiveData<Unit> = MutableLiveData()
    val noSearchFound: LiveData<Unit> get() = noSearchFoundPrivate

    /**
     * UI actions as event, user action is single one time event, Shouldn't be multiple time consumption
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val openUserDetailsPrivate = MutableLiveData<SingleEvent<User>>()
    val openUserDetails: LiveData<SingleEvent<User>> get() = openUserDetailsPrivate

    /**
     * Error handling as UI
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showSnackBarPrivate = MutableLiveData<SingleEvent<Any>>()
    val showSnackBar: LiveData<SingleEvent<Any>> get() = showSnackBarPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showToastPrivate = MutableLiveData<SingleEvent<Any>>()
    val showToast: LiveData<SingleEvent<Any>> get() = showToastPrivate


    fun getUsers() {
        viewModelScope.launch {
            val users = usersLiveDataPrivate.value?.data?.usersList
            usersLiveDataPrivate.value = Resource.Loading()
            usersPage++
            wrapEspressoIdlingResource {
                dataRepository.requestUsers(usersPage).collect {
                    if (usersPage > 1) {
                        it.data?.usersList?.let { newUsers -> users?.addAll(newUsers) }
                        users?.let { newUsers -> it.data?.usersList = newUsers }
                    }
                    usersLiveDataPrivate.value = it
                }
            }
        }
    }

    fun openUserDetails(user: User) {
        openUserDetailsPrivate.value = SingleEvent(user)
    }

    fun showToastMessage(errorCode: Int) {
        val error = errorManager.getError(errorCode)
        showToastPrivate.value = SingleEvent(error.description)
    }

    fun onSearchClick(userName: String) {
        usersLiveDataPrivate.value?.data?.usersList?.let {
            if (it.isNotEmpty()) {
                for (user in it) {
                    if (user.fullName.toLowerCase(ROOT).contains(userName.toLowerCase(ROOT))) {
                        userSearchFoundPrivate.value = user
                        return
                    }
                }
            }
        }
        return noSearchFoundPrivate.postValue(Unit)
    }
}
