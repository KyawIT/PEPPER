package at.htlleonding.pepperInElderlyCare.ui.model.api

import at.htlleonding.pepperInElderlyCare.ui.model.data.Step
import at.htlleonding.pepperInElderlyCare.ui.model.data.TagAlongStory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TagAlongStoryRequest {

    companion object{
    fun getAllTagAlongStories(): Call<List<TagAlongStory>?>?{
        val baseURL = "http://152.67.78.190:8080"

        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val tagAlongStoryAPI = retrofit.create(TagAlongStoryAPI::class.java)
        val call = tagAlongStoryAPI.getAllTagAlongStories();

        return call
    }

        fun getStoryById(id: Int): Call<List<Step>?>{
            val baseURL = "http://152.67.78.190:8080"

            val retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val tagAlongStoryAPI = retrofit.create(TagAlongStoryAPI::class.java)
            val call = tagAlongStoryAPI.getStoryById(id);

            return call
        }
}}