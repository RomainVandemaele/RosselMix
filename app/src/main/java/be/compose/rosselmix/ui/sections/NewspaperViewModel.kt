package be.compose.rosselmix.ui.sections

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.compose.rosselmix.data.room.Newspaper
import be.compose.rosselmix.data.room.RosselMixDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewspaperViewModel(val context : Context) : ViewModel() {

    private val _state = MutableStateFlow<NewspaperViewState>( NewspaperViewState())
    private val selectedNewspaper: MutableStateFlow<String?> = MutableStateFlow(null)

    val state : StateFlow<NewspaperViewState>
        get() = _state

    init {
        viewModelScope.launch {
            getDao().getAll().collect() {
                _state.value = _state.value.copy(newspapers = it)
            }
        }
    }

    private fun getDao() = RosselMixDatabase.instance(context).newspaperDao()


    fun selectNewspaper(newspaper: String?) {
        selectedNewspaper.value = newspaper
        _state.value = _state.value.copy(selectedNewspaper = selectedNewspaper.value)
    }

    companion object {
        //TODO : better factory than can be used ad parameter in viewModel() function
        fun factory(context: Context) = NewspaperViewModel(context)
    }
}

data class NewspaperViewState(
    val selectedNewspaper: String? = null,
    val newspapers: List<Newspaper> = emptyList()
)