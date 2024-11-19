package com.vishnu.alfred

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vishnu.alfred.model.Repository
import com.vishnu.alfred.ui.theme.AlfredTheme

private lateinit var viewModel: GitHubViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        viewModel = viewModels<GitHubViewModel>().value

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
    Column(modifier = modifier.fillMaxSize()) {
        SearchTextField()
        GitHubRepositoriesScreen(Modifier.align(Alignment.CenterHorizontally))
    }
}

@Composable
fun GitHubRepositoriesScreen(modifier: Modifier) {
    val reposState by viewModel.reposState.collectAsState()

    when (reposState) {
        is DataState.Loading -> {
            CircularProgressIndicator(modifier)
        }

        is DataState.Success -> {
            val repos = (reposState as DataState.Success<List<Repository>>).data
            if (repos.isNotEmpty()) {
                LazyColumn {
                    items(repos.sortedByDescending { it.stargazersCount }) {
                        RepoCard(it)
                    }
                }
            } else {
                Text(
                    text = "Error: This user has no repositories.",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        is DataState.Error -> {
            val errorMessage = (reposState as DataState.Error).message
            Text(
                modifier = modifier.padding(horizontal = 8.dp),
                text = "Error: $errorMessage",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun RepoCard(repository: Repository) {
    val uriHandler = LocalUriHandler.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        onClick = {
            uriHandler.openUri(repository.htmlUrl)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = repository.name,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            repository.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth(),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Stars",
                        tint = Color.Yellow,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = repository.stargazersCount.toString(), fontSize = 16.sp)
                }

                repository.language?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SearchTextField() {
    var text = remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        value = text.value,
        onValueChange = { text.value = it },
        label = { Text("Search for Users") },
        trailingIcon = @Composable {
            if (text.value.isNotEmpty())
                Icon(
                    modifier = Modifier.clickable {
                        viewModel.fetchUserRepositories(text.value)
                    },
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )

        },
        shape = RoundedCornerShape(16.dp)
    )
}