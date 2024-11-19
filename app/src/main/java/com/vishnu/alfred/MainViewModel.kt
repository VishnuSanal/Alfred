import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vishnu.alfred.model.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(@get:JvmName("application") val application: Application) :
    AndroidViewModel(application) {

    private val repository = GitHubRepository(RetrofitInstance.getInstance(application).api)

    private val _reposState = MutableStateFlow<DataState<List<Repository>>>(DataState.Loading)
    val reposState: StateFlow<DataState<List<Repository>>> = _reposState

    fun fetchUserRepositories(user: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _reposState.value = DataState.Loading
            val result = repository.getUserRepositories(user)
            _reposState.value = result
        }
    }
}