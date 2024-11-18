package com.vishnu.alfred

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vishnu.alfred.ui.theme.AlfredTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlfredTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Layout(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Layout(modifier: Modifier) {

    var isLoading by remember { mutableStateOf(true) }

    Column(modifier = modifier) {
        SimpleOutlinedTextFieldSample()

        if (isLoading)
            CircularProgressIndicator(
                Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally)
            )
    }
}

@Composable
fun SimpleOutlinedTextFieldSample() {
    var text = remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        value = text.value,
        onValueChange = { text.value = it },
        label = { Text("Search for Users") },
        trailingIcon = @Composable {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AlfredTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Layout(Modifier.padding(innerPadding))
        }
    }
}