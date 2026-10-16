package com.example.lab09.ui

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab09.data.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

data class UiState<T>(val loading: Boolean = false, val data: T? = null, val error: String? = null)
class LabViewModel : ViewModel() {
    private val repository = ApiRepository()
    var posts by mutableStateOf(UiState<List<PostModel>>()); private set
    var post by mutableStateOf(UiState<PostModel>()); private set
    var recipes by mutableStateOf(UiState<List<Recipe>>()); private set
    var recipe by mutableStateOf(UiState<Recipe>()); private set
    var amphibians by mutableStateOf(UiState<List<Amphibian>>()); private set
    private fun <T> request(update: (UiState<T>) -> Unit, block: suspend () -> T) {
        update(UiState(loading = true))
        viewModelScope.launch {
            try {
                update(UiState(data = block()))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                update(UiState(error = "No se pudo cargar. Revisa tu conexión y vuelve a intentar."))
            }
        }
    }

    fun loadPosts() = request({ posts = it }) { repository.posts() }
    fun loadPost(id: Int) = request({ post = it }) { repository.post(id) }
    fun loadRecipes() = request({ recipes = it }) { repository.recipes() }
    fun loadRecipe(id: Int) = request({ recipe = it }) { repository.recipe(id) }
    fun loadAmphibians() = request({ amphibians = it }) { repository.amphibians() }
}
