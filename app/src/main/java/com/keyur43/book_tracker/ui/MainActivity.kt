package com.keyur43.book_tracker.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.keyur43.book_tracker.ui.add.AddScreen
import com.keyur43.book_tracker.ui.add.AddViewModel
import com.keyur43.book_tracker.ui.detail.DetailScreen
import com.keyur43.book_tracker.ui.home.HomeScreen
import com.keyur43.book_tracker.ui.recent.RecentScreen
import com.keyur43.book_tracker.ui.stats.StatsScreen
import com.keyur43.book_tracker.ui.theme.BookTrackerTheme
import com.keyur43.book_tracker.util.Screen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BookTrackerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainContent()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    @Composable
    fun MainContent(modifier: Modifier = Modifier) {
        val addViewModel: AddViewModel = hiltViewModel()
        val keyboardController = LocalSoftwareKeyboardController.current
        val navController = rememberNavController()
        val scope = rememberCoroutineScope()

        val bottomSheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )

        var isSheetOpen by remember { mutableStateOf(false) }

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val isDetailScreen = currentRoute?.startsWith(Screen.BookDetail.route) == true

        // Back button behavior when sheet is open
        BackHandler(isSheetOpen) {
            scope.launch {
                bottomSheetState.hide()
                isSheetOpen = false
                keyboardController?.hide()
            }
        }

        LaunchedEffect(bottomSheetState.currentValue) {
            if (bottomSheetState.currentValue == SheetValue.Hidden) {
                addViewModel.clearSearchState()
                keyboardController?.hide()
                isSheetOpen = false
            } else {
                keyboardController?.show()
            }
        }

        Scaffold(
            bottomBar = {
                if (!isDetailScreen) {
                    BottomNav(navController = navController)
                }
            },
            floatingActionButton = {
                if (!isDetailScreen) {
                    FloatingActionButton(
                        onClick = {
                            scope.launch {
                                isSheetOpen = true
                                bottomSheetState.show()
                            }
                        },
                        containerColor = Color(0xFF80CBC4),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color(0xFF111111)
                        )
                    }
                }
            },
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                NavHost(navController = navController, startDestination = Screen.Recent.route) {
                    composable(Screen.Home.route) {
                        HomeScreen(
                            modifier = Modifier.fillMaxSize(),
                            viewModel = hiltViewModel(it)
                        ) {
                            navController.navigate("${Screen.BookDetail.route}/${it}")
                        }
                    }

                    composable(Screen.Stats.route) {
                        StatsScreen(
                            modifier = Modifier.fillMaxSize(),
                            viewModel = hiltViewModel(it)
                        )
                    }

                    composable(Screen.Recent.route) {
                        RecentScreen(
                            navController,
                            viewModel = hiltViewModel(it)
                        )
                    }

                    composable(
                        "${Screen.BookDetail.route}/{book_id}",
                        arguments = listOf(navArgument("book_id") { type = NavType.StringType })
                    ) {
                        DetailScreen(
                            bookId = it.arguments?.getString("book_id") ?: "",
                            modifier = Modifier.fillMaxSize(),
                            viewModel = hiltViewModel(it),
                            onBackClick = {
                                navController.navigateUp()
                            }

                        )
                    }
                }
            }

            if (isSheetOpen) {
                ModalBottomSheet(
                    onDismissRequest = {
                        scope.launch {
                            bottomSheetState.hide()
                            isSheetOpen = false
                            keyboardController?.hide()
                        }
                    },
                    sheetState = bottomSheetState,
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 5.dp
                ) {
                    AddScreen(viewModel = addViewModel) {
                        scope.launch {
                            bottomSheetState.hide()
                            isSheetOpen = false
                            keyboardController?.hide()
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun BottomNav(navController: NavController, modifier: Modifier = Modifier) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        NavigationBar(
            modifier = modifier.clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            val items = listOf(Screen.Recent, Screen.Home, Screen.Stats)

            items.forEach { screen ->
                NavigationBarItem(
                    selected = currentRoute == screen.route, onClick = {
                        if (currentRoute != screen.route) {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }, icon = {
                        Icon(imageVector = screen.icon!!, contentDescription = screen.title)
                    }, label = { Text(screen.title) }, alwaysShowLabel = true
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun BottomNavPreview() {
        BookTrackerTheme {
            BottomNav(rememberNavController())
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun MainContentPreview() {
        BookTrackerTheme {
            MainContent()
        }
    }
}