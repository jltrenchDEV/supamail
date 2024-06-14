package com.fiap.supamail.presentation.components

import android.Manifest
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.provider.CalendarContract
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.fiap.supamail.presentation.HomeViewModel
import java.util.Calendar
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailDetails(
    viewModel: HomeViewModel,
    navigateBack: () -> Unit,
    context: Context
) {
    val showDialog = remember { mutableStateOf(false) }
    val prevDatePickerDialog = remember { mutableStateOf(false) }
    val showDatePicker = remember { mutableStateOf(false) }
    val emailState = viewModel.email.collectAsState()
    val selectedEmail = emailState.value
    var eventStartTime by remember { mutableStateOf(System.currentTimeMillis()) }
    var eventEndTime by remember { mutableStateOf(System.currentTimeMillis() + 2 * 60 * 60 * 1000) }

    fun insertEvent(
        context: Context,
        startTime: Long,
        endTime: Long,
        title: String,
        description: String
    ) {
        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, startTime)
            put(CalendarContract.Events.DTEND, endTime)
            put(CalendarContract.Events.TITLE, title)
            put(CalendarContract.Events.DESCRIPTION, description)
            put(CalendarContract.Events.CALENDAR_ID, 1)
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        }
        val uri = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
        if (uri != null) {
            Toast.makeText(context, "Evento salvo com sucesso!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Erro ao salvar o evento.", Toast.LENGTH_SHORT).show()
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            insertEvent(
                context,
                eventStartTime,
                eventEndTime,
                selectedEmail?.subject ?: "Email",
                selectedEmail?.sender ?: "Supamail"
            )
        } else {
            Toast.makeText(
                context,
                "Permissão para acessar o calendário negada.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Scaffold(
        topBar = {
            selectedEmail?.let { email ->
                TopAppBar(
                    title = { Text(email.subject) },
                    navigationIcon = {
                        IconButton(onClick = navigateBack) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = "Voltar para a tela inicial"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            val permissionCheck = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.WRITE_CALENDAR
                            )
                            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                                requestPermissionLauncher.launch(Manifest.permission.WRITE_CALENDAR)
                            } else {
                                prevDatePickerDialog.value = true
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Calendar",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = {
                            viewModel.setFavoriteInDetails(
                                email.id,
                                if (email.favorite) 0 else 1
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Favoritar",
                                tint = if (email.favorite) Color.Yellow else Color.Gray
                            )
                        }
                        IconButton(onClick = { showDialog.value = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Deletar",
                                tint = Color.Red
                            )
                        }
                    }
                )
            }
        }
    ) { padding ->
        selectedEmail?.let { email ->
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 64.dp)) {
                Text(
                    "De: ${email.sender}",
                    fontSize = 18.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    "Para: ${email.receiver}",
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    "Assunto: ${email.subject}",
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Divider(Modifier.padding(vertical = 16.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(email.body)
            }
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Atenção!") },
            text = { Text("Tem certeza que deseja deletar este email?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedEmail?.let { email ->
                            viewModel.deleteEmail(email)
                            navigateBack()
                            showDialog.value = false
                        }
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog.value = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (prevDatePickerDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Crie um lembrete!") },
            text = { Text("Salve este e-mail no seu calendário. Escolha a data.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        prevDatePickerDialog.value = false
                        showDatePicker.value = true
                    }
                ) {
                    Text("Vamos lá")
                }
            },
            dismissButton = {
                TextButton(onClick = { prevDatePickerDialog.value = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showDatePicker.value) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
            val selectedCalendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, selectedYear)
                set(Calendar.MONTH, selectedMonth)
                set(Calendar.DAY_OF_MONTH, selectedDayOfMonth)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }
            eventStartTime = selectedCalendar.timeInMillis

            selectedCalendar.apply {
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
            }
            eventEndTime = selectedCalendar.timeInMillis

            insertEvent(
                context,
                eventStartTime,
                eventEndTime,
                selectedEmail?.subject ?: "Email",
                selectedEmail?.sender ?: "Subject"
            )
            showDatePicker.value = false
        }, year, month, day).show()
    }
}