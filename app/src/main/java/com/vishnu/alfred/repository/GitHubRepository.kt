import android.util.Log
import com.vishnu.alfred.model.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GitHubRepository(private val api: GitHubApiService) {

    suspend fun getUserRepositories(username: String): DataState<List<Repository>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getRepositoryList(username)
                Log.e("vishnu", "getUserRepositories: $response")
                if (response.isSuccessful)
                    DataState.Success(response.body()!!)
                else DataState.Error("An unknown error occurred")
            } catch (e: Exception) {
                DataState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}
