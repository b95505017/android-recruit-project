package `in`.hahow.recruit.ui.classes

import `in`.hahow.recruit.data.HahowClassRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HahowClassesViewModel @Inject constructor(
    repository: HahowClassRepository,
) : ViewModel() {
    val classes by lazy {
        repository.classes
            .cachedIn(viewModelScope)
    }
}