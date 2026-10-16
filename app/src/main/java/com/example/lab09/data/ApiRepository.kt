package com.example.lab09.data

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

data class PostModel(@SerializedName("userId") val userId: Int, @SerializedName("id") val id: Int,
 @SerializedName("title") val title: String, @SerializedName("body") val body: String,
 @SerializedName("link") val link: String? = null, @SerializedName("comment_count") val commentCount: Int? = null)
data class Recipe(val id: Int, val name: String, val image: String, val cuisine: String,
 val difficulty: String, val prepTimeMinutes: Int, val cookTimeMinutes: Int, val servings: Int,
 val rating: Double, val ingredients: List<String>, val instructions: List<String>)
data class RecipeResponse(val recipes: List<Recipe>)
data class Amphibian(val name: String, val type: String, val description: String,
 @SerializedName("img_src") val image: String)
interface PostApiService {
 @GET("posts") suspend fun getUserPosts(): List<PostModel>
 @GET("posts/{id}") suspend fun getUserPostById(@Path("id") id: Int): PostModel
}
interface RecipeApiService {
 @GET("recipes?limit=5") suspend fun getRecipes(): RecipeResponse
 @GET("recipes/{id}") suspend fun getRecipe(@Path("id") id: Int): Recipe
}
interface AmphibianApiService { @GET("amphibians") suspend fun getAmphibians(): List<Amphibian> }
class ApiRepository {
 private fun client(url: String) = Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build()
 private val posts = client("https://json-placeholder.mock.beeceptor.com/").create(PostApiService::class.java)
 private val recipes = client("https://dummyjson.com/").create(RecipeApiService::class.java)
 private val amphibians = client("https://android-kotlin-fun-mars-server.appspot.com/").create(AmphibianApiService::class.java)
 suspend fun posts() = posts.getUserPosts().map(ContenidoEspanol::post)
 suspend fun post(id: Int) = ContenidoEspanol.post(posts.getUserPostById(id))
 suspend fun recipes() = recipes.getRecipes().recipes.map(ContenidoEspanol::recipe)
 suspend fun recipe(id: Int) = ContenidoEspanol.recipe(recipes.getRecipe(id))
 suspend fun amphibians() = amphibians.getAmphibians().map(ContenidoEspanol::amphibian)
}

