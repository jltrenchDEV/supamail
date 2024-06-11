package com.fiap.supamail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiap.supamail.data.entity.EmailEntity
import com.fiap.supamail.data.repository.EmailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: EmailRepository,
) : ViewModel() {

    private val _emailDetailsList = MutableStateFlow(emptyList<EmailEntity>())
    val emailDetailsList = _emailDetailsList.asStateFlow()

    fun getEmailDetails() {
        viewModelScope.launch(IO) {
            repository.getAllEmails().collectLatest {
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

    private val _emailSender = MutableStateFlow("")
    val emailSender = _emailSender.asStateFlow()
    fun setEmailSender(sender: String) {
        _emailSender.tryEmit(sender)
    }

    private val _emailSubject = MutableStateFlow("")
    val emailSubject = _emailSubject.asStateFlow()
    fun setEmailSubject(subject: String) {
        _emailSubject.tryEmit(subject)
    }

    private val _emailBody = MutableStateFlow("")
    val emailBody = _emailBody.asStateFlow()
    fun setEmailBody(body: String) {
        _emailBody.tryEmit(body)
    }

    private val _emailOpened = MutableStateFlow(false)
    val emailOpened = _emailOpened.asStateFlow()
    fun setEmailOpened(opened: Boolean) {
        _emailOpened.tryEmit(opened)
    }

    private val _emailFavorite = MutableStateFlow(false)
    val emailFavorite = _emailFavorite.asStateFlow()
    fun setEmailFavorite(favorited: Boolean) {
        _emailFavorite.tryEmit(favorited)
    }

    private val _emailDate = MutableStateFlow("")
    val emailDate = _emailDate.asStateFlow()
    fun setEmailDate(date: String) {
        _emailDate.tryEmit(date)
    }
}