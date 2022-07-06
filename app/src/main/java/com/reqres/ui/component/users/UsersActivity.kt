package com.reqres.ui.component.users

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.reqres.R
import com.reqres.USER_ITEM_KEY
import com.reqres.data.Resource
import com.reqres.data.dto.users.Users
import com.reqres.data.dto.users.User
import com.reqres.data.error.SEARCH_ERROR
import com.reqres.databinding.ActivityUsersBinding
import com.reqres.ui.base.BaseActivity
import com.reqres.ui.component.details.DetailsActivity
import com.reqres.ui.component.users.adapter.UserAdapter
import com.reqres.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersActivity : BaseActivity() {
    private lateinit var binding: ActivityUsersBinding

    private val usersViewModel: UsersViewModel by viewModels()
    private var userAdapter: UserAdapter? = null

    override fun initViewBinding() {
        binding = ActivityUsersBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = getString(R.string.users_toolbar)
        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
        binding.rvUsers.setHasFixedSize(true)
        usersViewModel.getUsers()
        handleEndlessListUsers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_actions, menu)
        // Associate searchable configuration with the SearchView
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.queryHint = getString(R.string.search_by_name)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                handleSearch(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> {
                usersViewModel.usersPage = 0
                usersViewModel.getUsers()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleSearch(query: String) {
        if (query.isNotEmpty()) {
            binding.pbLoading.visibility = VISIBLE
            usersViewModel.onSearchClick(query)
        }
    }

    private fun bindListData(users: Users) {
        if (!users.usersList.isNullOrEmpty() || userAdapter?.getItems()?.size ?: 0 > 0) {
            //first load
            if (userAdapter == null) {
                userAdapter = UserAdapter(usersViewModel, users.usersList)
                binding.rvUsers.adapter = userAdapter

                //scroll update
            } else if (users.usersList.size > 0 && userAdapter?.getItems()?.size ?: 0 > 0 && users.usersList[0].id != userAdapter?.getItems()?.get(0)?.id) {
                userAdapter?.updateItems(users.usersList, userAdapter?.getItems()?.size ?: 0 - 1, users.usersList.size - 1)

                //refresh
            } else if (!users.usersList.isNullOrEmpty()) {
                userAdapter?.refreshItems(users.usersList)
            }
            showDataView(true)
        } else {
            showDataView(false)
        }
    }

    private fun navigateToDetailsScreen(navigateEvent: SingleEvent<User>) {
        navigateEvent.getContentIfNotHandled()?.let {
            val nextScreenIntent = Intent(this, DetailsActivity::class.java).apply {
                putExtra(USER_ITEM_KEY, it)
            }
            startActivity(nextScreenIntent)
        }
    }

    private fun observeSnackBarMessages(event: LiveData<SingleEvent<Any>>) {
        binding.root.setupSnackbar(this, event, Snackbar.LENGTH_LONG)
    }

    private fun observeToast(event: LiveData<SingleEvent<Any>>) {
        binding.root.showToast(this, event, Snackbar.LENGTH_LONG)
    }

    private fun showSearchError() {
        usersViewModel.showToastMessage(SEARCH_ERROR)
    }

    private fun showDataView(show: Boolean) {
        binding.tvNoData.visibility = if (show) GONE else VISIBLE
        binding.rvUsers.visibility = if (show) VISIBLE else GONE
        binding.pbLoading.toGone()
    }

    private fun showLoadingView() {
        binding.pbLoading.toVisible()
        binding.tvNoData.toGone()
        binding.rvUsers.toGone()
    }

    private fun showSearchResult(user: User) {
        usersViewModel.openUserDetails(user)
        binding.pbLoading.toGone()
    }

    private fun noSearchResult(unit: Unit) {
        showSearchError()
        binding.pbLoading.toGone()
    }

    private fun handleUsersList(status: Resource<Users>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let { bindListData(users = it) }
            is Resource.DataError -> {
                showDataView(false)
                status.errorCode?.let { usersViewModel.showToastMessage(it) }
            }
        }
    }

    private fun handleEndlessListUsers() {
        binding.rvUsers.addOnScrolledToEnd {
            usersViewModel.getUsers()
        }
    }

    override fun observeViewModel() {
        observe(usersViewModel.usersLiveData, ::handleUsersList)
        observe(usersViewModel.userSearchFound, ::showSearchResult)
        observe(usersViewModel.noSearchFound, ::noSearchResult)
        observeEvent(usersViewModel.openUserDetails, ::navigateToDetailsScreen)
        observeSnackBarMessages(usersViewModel.showSnackBar)
        observeToast(usersViewModel.showToast)
    }
}
