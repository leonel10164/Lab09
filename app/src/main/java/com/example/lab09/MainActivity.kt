package com.example.lab09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.lab09.ui.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF176B58),
                    secondary = Color(0xFFB45B35),
                    background = Color(0xFFF5F7F3)
                )
            ) { ProgPrincipal9() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgPrincipal9(vm: LabViewModel = viewModel()) {
    val nav = rememberNavController()
    val current by nav.currentBackStackEntryAsState()
    val route = current?.destination?.route ?: "inicio"
    val tabs = listOf(
        "inicio" to "Inicio",
        "posts" to "Publicaciones",
        "recetas" to "Recetas",
        "anfibios" to "Anfibios"
    )
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { Text("Lab 09 · Explora las API") },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = Color.White
            )
        )
    },
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, (path, label) ->
                    NavigationBarItem(
                        selected = route.startsWith(path),
                        onClick = {
                            nav.navigate(path) {
                                popUpTo(nav.graph.startDestinationId) {
                                    saveState = true
                                }; launchSingleTop = true; restoreState = true
                            }
                        },
                        icon = { Text(listOf("⌂", "≡", "♧", "≈")[index]) },
                        label = { Text(label) })
                }
            }
        }) { padding ->
        NavHost(
            navController = nav,
            startDestination = "inicio",
            modifier = Modifier.padding(padding)
        ) {
            composable("inicio") { HomeScreen { nav.navigate(it) } }
            composable("posts") {
                LaunchedEffect(Unit) { if (vm.posts.data == null) vm.loadPosts() }; PostsScreen(
                vm.posts,
                vm::loadPosts
            ) { nav.navigate("postsVer/$it") }
            }
            composable(
                "postsVer/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entry ->
                val id =
                    entry.arguments!!.getInt("id"); LaunchedEffect(id) { vm.loadPost(id) }; PostScreen(
                vm.post,
                { vm.loadPost(id) }) { nav.popBackStack() }
            }
            composable("recetas") {
                LaunchedEffect(Unit) { if (vm.recipes.data == null) vm.loadRecipes() }; RecipesScreen(
                vm.recipes,
                vm::loadRecipes
            ) { nav.navigate("receta/$it") }
            }
            composable(
                "receta/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entry ->
                val id =
                    entry.arguments!!.getInt("id"); LaunchedEffect(id) { vm.loadRecipe(id) }; RecipeScreen(
                vm.recipe,
                { vm.loadRecipe(id) }) { nav.popBackStack() }
            }
            composable("anfibios") {
                LaunchedEffect(Unit) { if (vm.amphibians.data == null) vm.loadAmphibians() }; AmphibiansScreen(
                vm.amphibians,
                vm::loadAmphibians
            )
            }
        }
    }
}

