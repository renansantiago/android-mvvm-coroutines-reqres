package com.reqres

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.reqres.data.dto.users.Users
import com.reqres.data.dto.users.User
import java.io.InputStream

object TestUtil {
    var dataStatus: DataStatus = DataStatus.Success
    var users: Users = Users(arrayListOf())
    fun initData(): Users {
        val jsonString = getJson("UsersApiResponse.json")
        users = Users(ArrayList(Gson().fromJson<List<User>>(jsonString, object : TypeToken<List<User>>() {}.type)))
        return users
    }

    private fun getJson(path: String): String {
        val ctx: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val inputStream: InputStream = ctx.classLoader.getResourceAsStream(path)
        return inputStream.bufferedReader().use { it.readText() }
    }
}
