import com.vishnu.alfred.model.Repository
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApiService {
    @GET("users/{owner}/repos")
    suspend fun getRepositoryList(
        @Path("owner") owner: String,
    ): Response<List<Repository>>
}
