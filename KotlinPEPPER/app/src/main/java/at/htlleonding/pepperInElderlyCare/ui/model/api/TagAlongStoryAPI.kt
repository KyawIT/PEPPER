package at.htlleonding.pepperInElderlyCare.ui.model.api

import at.htlleonding.pepperInElderlyCare.ui.model.data.Step
import at.htlleonding.pepperInElderlyCare.ui.model.data.TagAlongStory
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TagAlongStoryAPI {

    @GET("/tagalongstories")
    fun getAllTagAlongStories(@Query("withoutDisabled") withoutDisabled: Boolean = true): Call<List<TagAlongStory>?>?

    @GET("/tagalongstories/{id}/steps")
    fun getStoryById(@Path("id") id: Int): Call<List<Step>?>
}