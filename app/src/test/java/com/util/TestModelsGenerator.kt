package com.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.reqres.data.dto.users.User
import com.reqres.data.dto.users.Users
import java.io.File

class TestModelsGenerator {
    private var users: Users = Users(arrayListOf())

    init {
        val jsonString = getJson("UsersApiResponse.json")
        users = Users(ArrayList(Gson().fromJson<List<User>>(jsonString, object : TypeToken<List<User>>() {}.type)))
        print("this is $users")
    }

    fun generateUsers(): Users {
        return users
    }

    fun generateUsersModelWithEmptyList(): Users {
        return Users(arrayListOf())
    }

    fun generateUsersItemModel(): User {
        return users.usersList[0]
    }

    fun getStubSearchTitle(): String {
        return users.usersList[0].fullName
    }

    /**
     * Helper function which will load JSON from
     * the path specified
     *
     * @param path : Path of JSON file
     * @return json : JSON from file at given path
     */

    private fun getJson(path: String): String {
        // Load the JSON response
        val uri = this.javaClass.classLoader?.getResource(path)
        val file = File(uri?.path)
        return String(file.readBytes())
    }
}
