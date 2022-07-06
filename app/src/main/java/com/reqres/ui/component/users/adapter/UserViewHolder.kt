package com.reqres.ui.component.users.adapter

import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.reqres.R
import com.reqres.data.dto.users.User
import com.reqres.databinding.ItemUserBinding
import com.reqres.ui.base.listeners.RecyclerItemListener

class UserViewHolder(private val itemBinding: ItemUserBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(user: User, recyclerItemListener: RecyclerItemListener) {
        itemBinding.tvName.text = user.fullName
        itemBinding.tvEmail.text = user.email
        Picasso.get().load(user.avatar).placeholder(R.drawable.ic_empty_avatar).error(R.drawable.ic_empty_avatar).into(itemBinding.ivAvatar)
        itemBinding.ivFavourite.setImageResource(if (user.isFavourite == true) R.drawable.ic_star_24 else R.drawable.ic_outline_star_border_24)
        itemBinding.cardItemUser.setOnClickListener { recyclerItemListener.onItemSelected(user) }
    }
}

