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
        Text(
            kicker.uppercase(),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelLarge
        )
        Text(title, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        Text(subtitle, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun HomeScreen(open: (String) -> Unit) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Heading(
                "Desarrollo de aplicaciones móviles",
                "Explora. Aprende. Conecta.",
                "Laboratorio 09 · Retrofit y Jetpack Compose"
            )
        }
        item {
            Text(
                "Tres experiencias, datos reales",
                style = MaterialTheme.typography.titleLarge
            )
        }
        items(
            listOf(
                Triple(
                    "posts",
                    "01  Publicaciones",
                    "Explora los posts y consulta su contenido completo."
                ),
                Triple(
                    "recetas",
                    "02  Cocina del mundo",
                    "Descubre recetas, ingredientes e instrucciones."
                ),
                Triple("anfibios", "03  Vida anfibia", "Conoce especies y sus características.")
            )
        ) { (route, title, description) ->
            Card(
                onClick = { open(route) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(description)
                    Text("Explorar →", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        item {
            Text(
                "MVVM · Coroutines · Gson",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }
    }
}

@Composable
fun <T> LoadContent(state: UiState<T>, retry: () -> Unit, content: @Composable (T) -> Unit) {
    when {
        state.loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) { CircularProgressIndicator(); Text("Consultando API…", Modifier.padding(16.dp)) }
        }

        state.error != null -> Box(
            Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(state.error); Button(
                onClick = retry,
                modifier = Modifier.padding(16.dp)
            ) { Text("Reintentar") }
            }
        }

        state.data != null -> content(state.data)
    }
}

@Composable
fun PostsScreen(state: UiState<List<PostModel>>, retry: () -> Unit, open: (Int) -> Unit) {
    LoadContent(state, retry) { posts ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Heading(
                    "Parte guiada · JSONPlaceholder",
                    "Publicaciones",
                    "${posts.size} publicaciones disponibles · selecciona uno"
                )
            }
            if (posts.isEmpty()) item { Text("No hay publicaciones disponibles.") }
            items(posts, key = { it.id }) { post ->
                Card(onClick = { open(post.id) }, modifier = Modifier.fillMaxWidth()) {
                    Column(
                        Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            "PUBLICACIÓN ${
                                post.id.toString().padStart(2, '0')
                            } · AUTOR ${post.userId}",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            post.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
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
        LazyColumn(
            Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { TextButton(onClick = back) { Text("← Volver a publicaciones") } }
            item { Heading("Publicación ${post.id}", post.title, "Autor ${post.userId}") }
            item {
                Card(Modifier.fillMaxWidth()) {
                    Text(
                        post.body,
                        Modifier.padding(24.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            post.commentCount?.let { item { Text("$it comentarios") } }
            post.link?.let { item { Text("Referencia: $it") } }
        }
    }
}

@Composable
fun RecipesScreen(state: UiState<List<Recipe>>, retry: () -> Unit, open: (Int) -> Unit) {
    var search by remember { mutableStateOf("") }
    LoadContent(state, retry) { recipes ->
        val filtered =
            recipes.filter { it.name.contains(search, true) || it.cuisine.contains(search, true) }
        LazyColumn(
            Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Heading(
                    "Ejercicio 1 · DummyJSON",
                    "Cocina del mundo",
                    "Cinco recetas traducidas para descubrir nuevos sabores"
                )
            }
            item {
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    label = { Text("Buscar receta o cocina") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
            item { Text("${filtered.size} recetas", style = MaterialTheme.typography.labelLarge) }
            if (filtered.isEmpty()) item { Text("No se encontraron recetas.") }
            items(filtered, key = { it.id }) { recipe ->
                Card(
                    onClick = { open(recipe.id) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    AsyncImage(
                        model = recipe.image,
                        contentDescription = recipe.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentScale = ContentScale.Crop
                    )
                    Column(
                        Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            recipe.cuisine.uppercase(),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            recipe.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
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
        LazyColumn(
            Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { TextButton(onClick = back) { Text("← Volver a recetas") } }
            item {
                AsyncImage(
                    model = recipe.image,
                    contentDescription = recipe.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentScale = ContentScale.Crop
                )
            }
            item {
                Heading(
                    recipe.cuisine,
                    recipe.name,
                    "${recipe.servings} porciones · ${recipe.prepTimeMinutes + recipe.cookTimeMinutes} min · ★ ${recipe.rating}"
                )
            }
            item { Text("Ingredientes", style = MaterialTheme.typography.titleLarge) }
            items(recipe.ingredients) { Text("• $it") }
            item {
                Text(
                    "Preparación",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
            items(recipe.instructions.size) { index -> Text("${index + 1}. ${recipe.instructions[index]}") }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun AmphibiansScreen(state: UiState<List<Amphibian>>, retry: () -> Unit) {
    var query by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf("") }
    var type by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf("Todos") }
    var selectedName by androidx.compose.runtime.saveable.rememberSaveable {
        mutableStateOf<String?>(
            null
        )
    }
    LoadContent(state, retry) { amphibians ->
        val selected = amphibians.firstOrNull { it.name == selectedName }
        if (selected != null) {
            AmphibianDetails(selected) { selectedName = null }
        } else {
            val filtered = amphibians.filter {
                (type == "Todos" || it.type == type) &&
                        it.name.contains(query.trim(), ignoreCase = true)
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Heading(
                        "Reto adicional · API REST", "Vida anfibia",
                        "Busca, filtra y descubre las especies del catálogo"
                    )
                }
                item {
                    Row(
                        Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "${amphibians.size} especies cargadas",
                            style = MaterialTheme.typography.labelLarge
                        )
                        OutlinedButton(onClick = retry) { Text("Actualizar") }
                    }
                }
                item {
                    OutlinedTextField(value = query, onValueChange = { query = it },
                        label = { Text("Buscar especie") }, singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            if (query.isNotEmpty()) TextButton(onClick = { query = "" }) {
                                Text("Borrar")
                            }
                        })
                }
                item {
                    androidx.compose.foundation.lazy.LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            listOf("Todos") + amphibians.map { it.type }.distinct().sorted()
                        ) { option ->
                            FilterChip(selected = type == option, onClick = { type = option },
                                label = { Text(option) })
                        }
                    }
                }
                item {
                    Text(
                        "${filtered.size} de ${amphibians.size} especies",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                if (filtered.isEmpty()) item {
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(20.dp)) {
                            Text("No se encontraron especies con esos criterios.")
                            TextButton(onClick = { query = ""; type = "Todos" }) {
                                Text("Limpiar filtros")
                            }
                        }
                    }
                }
                items(filtered, key = { it.name }) { animal ->
                    Card(
                        onClick = { selectedName = animal.name },
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp)
                    ) {
                        AsyncImage(
                            model = animal.image, contentDescription = animal.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                animal.type.uppercase(), color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelLarge
                            )
                            Text(
                                animal.name, style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(animal.description, maxLines = 2)
                            Text(
                                "Ver ficha de la especie →",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
                item { Spacer(Modifier.height(20.dp)) }
            }
        }
    }
}

@Composable
private fun AmphibianDetails(animal: Amphibian, back: () -> Unit) {
    androidx.activity.compose.BackHandler(onBack = back)
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { TextButton(onClick = back) { Text("← Volver al catálogo") } }
        item {
            AsyncImage(
                model = animal.image, contentDescription = animal.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp), contentScale = ContentScale.Crop
            )
        }
        item { Heading(animal.type, animal.name, "Ficha de la especie") }
        item {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Características y hábitat", style = MaterialTheme.typography.titleLarge)
                    Text(animal.description, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        item {
            Text(
                "Información del catálogo de anfibios de Android Developers.",
                style = MaterialTheme.typography.labelMedium
            )
        }
        item { Spacer(Modifier.height(24.dp)) }
    }
}
