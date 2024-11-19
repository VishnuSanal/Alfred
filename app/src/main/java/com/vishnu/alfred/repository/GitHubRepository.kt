import com.vishnu.alfred.model.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GitHubRepository(private val api: GitHubApiService) {

    suspend fun getUserRepositories(username: String): DataState<List<Repository>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getRepositoryList(username)
                if (response.isSuccessful)
                    DataState.Success(response.body()!!)
                else if (response.code() == 404)
                    DataState.Error("No such username")
                else
                    DataState.Error("Error loading repositories")
            } catch (e: Exception) {
                DataState.Error("Error loading repositories. Are you connected to the internet?")
            }
        }
    }
}
