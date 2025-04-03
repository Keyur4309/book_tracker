package com.keyur43.book_tracker.view

import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.keyur43.book_tracker.R
import com.keyur43.book_tracker.ui.theme.Book_trackerTheme

@Composable
fun AppNavigation()
{
    val navController = rememberNavController()

    NavHost(navController, startDestination = "home")
    {

        composable("home") { HomeScreen(navController)}

    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController : NavController, modifier: Modifier = Modifier)
{


    Scaffold(topBar = { TopAppBar(title = { Text("Book_Detail") }) }){
            padding ->
        Column(modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            Image(painter = painterResource(R.drawable.book_cover_1),
                contentDescription = "Dice 1",
                modifier.size(300.dp) .
                clickable { navController.navigate("Book_Detail")
                })

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Book_trackerTheme() {
        AppNavigation()
    }
}