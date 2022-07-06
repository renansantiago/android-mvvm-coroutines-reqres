package com.reqres.ui.component.users.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reqres.data.dto.users.User
import com.reqres.databinding.ItemUserBinding
import com.reqres.ui.base.listeners.RecyclerItemListener
import com.reqres.ui.component.users.UsersViewModel

class UserAdapter(private val usersViewModel: UsersViewModel, var users: ArrayList<User>) : RecyclerView.Adapter<UserViewHolder>() {

    private val onItemClickListener: RecyclerItemListener = object : RecyclerItemListener {
        override fun onItemSelected(user: User) {
            usersViewModel.openUserDetails(user)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemBinding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun updateItems(newUsers: List<User>, notifyFrom: Int, notifyTo: Int) {
        users.addAll(newUsers)
        notifyItemRangeInserted(notifyFrom, notifyTo)
    }

    fun refreshItems(refreshedUsers: ArrayList<User>) {
        users = refreshedUsers
        notifyDataSetChanged()
    }

    fun getItems(): ArrayList<User> {
        return users
    }
}

