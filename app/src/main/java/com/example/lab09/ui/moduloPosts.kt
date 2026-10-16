package com.example.lab09.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.lab09.data.*

@Composable
fun Heading(kicker: String, title: String, subtitle: String) {
 Column(Modifier.padding(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
 Text(kicker.uppercase(), color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge)
 Text(title, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
 Text(subtitle, style = MaterialTheme.typography.bodyMedium)
 }
}
@Composable
fun HomeScreen(open: (String) -> Unit) {
 LazyColumn(Modifier.fillMaxSize().padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
 item { Heading("Desarrollo de aplicaciones móviles", "Explora. Aprende. Conecta.", "Laboratorio 09 · Retrofit y Jetpack Compose") }
 item { Text("Tres experiencias, datos reales", style = MaterialTheme.typography.titleLarge) }
 items(listOf(Triple("posts", "01  Publicaciones", "Explora los posts y consulta su contenido completo."), Triple("recetas", "02  Cocina del mundo", "Descubre recetas, ingredientes e instrucciones."), Triple("anfibios", "03  Vida anfibia", "Conoce especies y sus características."))) { (route, title, description) ->
 Card(onClick = { open(route) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp)) {
 Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
 Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
 Text(description)
 Text("Explorar →", color = MaterialTheme.colorScheme.primary)
 }
 }
 }
 item { Text("MVVM · Coroutines · Gson", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(bottom = 20.dp)) }
 }
}
@Composable
fun <T> LoadContent(state: UiState<T>, retry: () -> Unit, content: @Composable (T) -> Unit) {
 when {
 state.loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Column(horizontalAlignment = Alignment.CenterHorizontally) { CircularProgressIndicator(); Text("Consultando API…", Modifier.padding(16.dp)) } }
 state.error != null -> Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Text(state.error); Button(onClick = retry, modifier = Modifier.padding(16.dp)) { Text("Reintentar") } } }
 state.data != null -> content(state.data)
 }
}
@Composable
fun PostsScreen(state: UiState<List<PostModel>>, retry: () -> Unit, open: (Int) -> Unit) {
 LoadContent(state, retry) { posts ->
 LazyColumn(Modifier.fillMaxSize().padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
 item { Heading("Parte guiada · JSONPlaceholder", "Publicaciones", "${posts.size} posts disponibles · selecciona uno") }
 if (posts.isEmpty()) item { Text("No hay publicaciones disponibles.") }
 items(posts, key = { it.id }) { post ->
 Card(onClick = { open(post.id) }, modifier = Modifier.fillMaxWidth()) {
 Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
 Text("POST ${post.id.toString().padStart(2, '0')} · AUTOR ${post.userId}", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
 Text(post.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
 Text("Leer publicación →", style = MaterialTheme.typography.labelLarge)
 }
 }
 }
 item { Spacer(Modifier.height(12.dp)) }
 }
 }
}
@Composable
fun PostScreen(state: UiState<PostModel>, retry: () -> Unit, back: () -> Unit) {
 LoadContent(state, retry) { post ->
 LazyColumn(Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
 item { TextButton(onClick = back) { Text("← Volver a publicaciones") } }
 item { Heading("Publicación ${post.id}", post.title, "Autor ${post.userId}") }
 item { Card(Modifier.fillMaxWidth()) { Text(post.body, Modifier.padding(24.dp), style = MaterialTheme.typography.bodyLarge) } }
 post.commentCount?.let { item { Text("$it comentarios") } }
 post.link?.let { item { Text("Referencia: $it") } }
 }
 }
}
@Composable
fun RecipesScreen(state: UiState<List<Recipe>>, retry: () -> Unit, open: (Int) -> Unit) {
 var search by remember { mutableStateOf("") }
 LoadContent(state, retry) { recipes ->
 val filtered = recipes.filter { it.name.contains(search, true) || it.cuisine.contains(search, true) }
 LazyColumn(Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
 item { Heading("Ejercicio 1 · DummyJSON", "Cocina del mundo", "Recetas para descubrir nuevos sabores") }
 item { OutlinedTextField(value = search, onValueChange = { search = it }, label = { Text("Buscar receta o cocina") }, modifier = Modifier.fillMaxWidth(), singleLine = true) }
 item { Text("${filtered.size} recetas", style = MaterialTheme.typography.labelLarge) }
 if (filtered.isEmpty()) item { Text("No se encontraron recetas.") }
 items(filtered, key = { it.id }) { recipe ->
 Card(onClick = { open(recipe.id) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp)) {
 AsyncImage(model = recipe.image, contentDescription = recipe.name, modifier = Modifier.fillMaxWidth().height(180.dp), contentScale = ContentScale.Crop)
 Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
 Text(recipe.cuisine.uppercase(), color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
 Text(recipe.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
 Text("${recipe.prepTimeMinutes + recipe.cookTimeMinutes} min · ${recipe.difficulty} · ★ ${recipe.rating}")
 Text("Ver preparación →", color = MaterialTheme.colorScheme.primary)
 }
 }
 }
 item { Spacer(Modifier.height(16.dp)) }
 }
 }
}
@Composable
fun RecipeScreen(state: UiState<Recipe>, retry: () -> Unit, back: () -> Unit) {
 LoadContent(state, retry) { recipe ->
 LazyColumn(Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
 item { TextButton(onClick = back) { Text("← Volver a recetas") } }
 item { AsyncImage(model = recipe.image, contentDescription = recipe.name, modifier = Modifier.fillMaxWidth().height(220.dp), contentScale = ContentScale.Crop) }
 item { Heading(recipe.cuisine, recipe.name, "${recipe.servings} porciones · ${recipe.prepTimeMinutes + recipe.cookTimeMinutes} min · ★ ${recipe.rating}") }
 item { Text("Ingredientes", style = MaterialTheme.typography.titleLarge) }
 items(recipe.ingredients) { Text("• $it") }
 item { Text("Preparación", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 12.dp)) }
 items(recipe.instructions.size) { index -> Text("${index + 1}. ${recipe.instructions[index]}") }
 item { Spacer(Modifier.height(24.dp)) }
 }
 }
}
@Composable
fun AmphibiansScreen(state: UiState<List<Amphibian>>, retry: () -> Unit) {
 LoadContent(state, retry) { amphibians ->
 LazyColumn(Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
 item { Heading("Reto adicional · Android", "Vida anfibia", "${amphibians.size} especies para conocer y proteger") }
 if (amphibians.isEmpty()) item { Text("No hay especies disponibles.") }
 items(amphibians) { animal ->
 Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp)) {
 AsyncImage(model = animal.image, contentDescription = animal.name, modifier = Modifier.fillMaxWidth().height(200.dp), contentScale = ContentScale.Crop)
 Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
 Text(animal.type.uppercase(), color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge)
 Text(animal.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
 Text(animal.description)
 }
 }
 }
 item { Spacer(Modifier.height(16.dp)) }
 }
 }
}
