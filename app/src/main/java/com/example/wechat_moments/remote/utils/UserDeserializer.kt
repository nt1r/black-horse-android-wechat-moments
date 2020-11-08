package com.example.wechat_moments.remote.utils

import com.example.wechat_moments.viewmodels.User
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class UserDeserializer : JsonDeserializer<User> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): User {
        if (json == null) {
            return User(
                "",
                "",
                "",
                ""
            )
        }
        val jsonObject = json.asJsonObject

        return User(
            jsonObject.get("username")?.asString ?: "",
            jsonObject.get("nick")?.asString ?: "",
            jsonObject.get("avatar")?.asString ?: "",
            jsonObject.get("profile-image")?.asString ?: ""
        )
    }
}