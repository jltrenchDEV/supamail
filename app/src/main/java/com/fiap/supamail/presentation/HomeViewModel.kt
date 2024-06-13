package com.fiap.supamail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiap.supamail.data.entity.EmailEntity
import com.fiap.supamail.data.repository.EmailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: EmailRepository,
) : ViewModel() {

    private val _emailDetailsList = MutableStateFlow(emptyList<EmailEntity>())

    private val _email = MutableStateFlow<EmailEntity?>(null)
    val email = _email.asStateFlow()

    fun getEmailById(emailId: Int?) {
        emailId?.let { id ->
            viewModelScope.launch(IO) {
                val email = repository.getEmail(id = id)
                _email.tryEmit(email)
            }
        }
    }

    fun getEmailsBySubject(subject: String) {
        viewModelScope.launch(IO) {
            repository.getEmailsBySubject(subject).collectLatest {
                _emailDetailsList.tryEmit(it)
            }
        }
    }

    fun updateEmail(emailEntity: EmailEntity) {
        viewModelScope.launch(IO) {
            repository.update(emailEntity)
        }
    }

    fun insertEmail(emailEntity: EmailEntity) {
        viewModelScope.launch(IO) {
            repository.insert(emailEntity)
        }
    }

    fun deleteEmail(emailEntity: EmailEntity) {
        viewModelScope.launch(IO) {
            repository.delete(emailEntity)
        }
    }

    fun setEmailAsOpened(emailId: Int) {
        viewModelScope.launch(IO) {
            repository.updateEmailAsOpened(emailId)
        }
    }

    fun setFavoriteInDetails(emailId: Int, isFavorite: Int) {
        viewModelScope.launch(IO) {
            repository.setFavorite(emailId, isFavorite)
            getEmailById(emailId)
        }
    }

    enum class Filter {
        Favorite, DescDate, AscDate, None
    }

    private val _filter = MutableStateFlow(Filter.None)

    @OptIn(ExperimentalCoroutinesApi::class)
    val emails = _filter.flatMapLatest { filter ->
        when (filter) {
            Filter.Favorite -> repository.getFavoriteEmails()
            Filter.DescDate -> repository.getEmailsDescByDate()
            Filter.AscDate -> repository.getEmailsAscByDate()
            Filter.None -> repository.getAllEmails()
        }
    }

    fun setFilter(filter: Filter) {
        _filter.value = filter
    }
}