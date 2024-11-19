import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance(val context: Context) {

    companion object {
        private var INSTANCE: RetrofitInstance? = null

        fun getInstance(context: Context): RetrofitInstance {
            if (INSTANCE == null)
                INSTANCE = RetrofitInstance(context)

            return INSTANCE!!
        }
    }

    private val BASE_URL = "https://api.github.com/"

    // https://bapspatil.medium.com/caching-with-retrofit-store-responses-offline-71439ed32fda
    val okHttpClient = OkHttpClient.Builder()
        .cache(Cache(context.cacheDir, (5 * 1024 * 1024).toLong()))
        .addInterceptor { chain ->
            var request = chain.request()
            request = if (isOnline(context))
                request.newBuilder().header("Cache-Control", "public, max-age=" + 60).build()
            else
                request.newBuilder().header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                ).build()
            chain.proceed(request)
        }
        .build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val api: GitHubApiService by lazy {
        retrofit.create(GitHubApiService::class.java)
    }

    // https://stackoverflow.com/a/57237708/9652621
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }
}