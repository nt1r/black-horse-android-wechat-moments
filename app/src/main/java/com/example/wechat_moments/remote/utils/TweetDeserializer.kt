package com.example.wechat_moments.remote.utils

import com.example.wechat_moments.remote.AbstractTweet
import com.example.wechat_moments.remote.TweetError
import com.example.wechat_moments.viewmodels.Comment
import com.example.wechat_moments.viewmodels.Tweet
import com.example.wechat_moments.viewmodels.User
import com.google.gson.*
import java.lang.reflect.Type

class TweetDeserializer : JsonDeserializer<AbstractTweet> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): AbstractTweet {
        if (json == null) {
            return TweetError(
                "json null"
            )
        }
        val jsonObject: JsonObject = json.asJsonObject
        if (!jsonObject.keySet().contains("sender")) {
            return TweetError(
                "invalid json"
            )
        }

        val gson = GsonBuilder().registerTypeAdapter(User::class.java, UserDeserializer()).create()
        val images: MutableList<String> = mutableListOf()
        if (jsonObject.keySet().contains("images")) {
            jsonObject.getAsJsonArray("images").forEach { jsonElement ->
                images.add(jsonElement.asString)
            }
        }

        val user: User = gson.fromJson(jsonObject.getAsJsonObject("sender"), User::class.java)
        val comments: MutableList<Comment> = mutableListOf()
        if (jsonObject.keySet().contains("comments")) {
            jsonObject.getAsJsonArray("comments").forEach { jsonElement ->
                comments.add(gson.fromJson(jsonElement, Comment::class.java))
            }
        }

        var content: String = ""
        if (jsonObject.keySet().contains("content")) {
            content = jsonObject.get("content").asString
        }

        return Tweet(
            content,
            images,
            user,
            comments
        )
    }
}