package com.reqres.ui.base.listeners

import com.reqres.data.dto.users.User

interface RecyclerItemListener {
    fun onItemSelected(user: User)
}
