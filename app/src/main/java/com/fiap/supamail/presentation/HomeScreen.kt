package com.fiap.supamail.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fiap.supamail.data.entity.EmailEntity

@Composable
fun HomeScreen() {

    val viewModel = hiltViewModel<HomeViewModel>()
    Content(homeViewModel = viewModel)
}

@Composable
fun Content(
    homeViewModel: HomeViewModel
) {

    LaunchedEffect(key1 = true, block = {
        homeViewModel.getEmailDetails()
    })

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f), contentAlignment = Alignment.TopCenter
        ) {
            TopContent(homeViewModel = homeViewModel)
        }
        Text(
            text = "Emails:",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        BottomContent(homeViewModel = homeViewModel)


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopContent(
    homeViewModel: HomeViewModel,
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val subject by homeViewModel.emailSubject.collectAsStateWithLifecycle()
        val body by homeViewModel.emailBody.collectAsStateWithLifecycle()
        val opened by homeViewModel.emailOpened.collectAsStateWithLifecycle()
        val onNameEntered: (value: String) -> Unit = remember {
            return@remember homeViewModel::setEmailSubject
        }
        val onRollNoEntered: (value: String) -> Unit = remember {
            return@remember homeViewModel::setEmailBody
        }
        val onCheck: (value: Boolean) -> Unit = remember {
            return@remember homeViewModel::setEmailOpened
        }
        val onSubmit: (value: EmailEntity) -> Unit = remember {
            return@remember homeViewModel::insertEmail
        }
        OutlinedTextField(
            value = subject,
            onValueChange = {
                onNameEntered(it)
            },
            placeholder = {
                Text(text = "Titulo do Email")
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(), maxLines = 1
        )
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = body, onValueChange = {
                onRollNoEntered(it)
            },
            placeholder = {
                Text(text = "Corpo do Email")
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(),
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(15.dp))

        Checkbox(checked = opened, onCheckedChange = {
            onCheck(it)
        })
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedButton(onClick = {
            onSubmit(
                EmailEntity(
                    subject = subject,
                    body = body,
                    alreadyOpened = opened,
                    favorite = false,
                    sender = "Tester",
                    sentDate = "24/02/2024"
                )
            )
        }) {
            Text(text = "Criar Email")
        }

    }
}

@Composable
fun BottomContent(
    homeViewModel: HomeViewModel,
) {

    val contents by homeViewModel.emailDetailsList.collectAsStateWithLifecycle()

    val mod = Modifier
        .padding(15.dp)
        .fillMaxWidth()
        .height(80.dp)

    LazyColumn(
        content = {

            items(contents) {
                val item = ImutableEmail(it)
                EmailCard(wrapper = item, homeViewModel = homeViewModel, mod = mod)
            }
        }, modifier = Modifier.fillMaxSize()
    )
}

@Immutable
data class ImutableEmail(val emailEntity: EmailEntity)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailCard(
    wrapper: ImutableEmail,
    homeViewModel: HomeViewModel,
    mod: Modifier,
) {

    val onCheckedChange: (value: EmailEntity) -> Unit = remember {
        return@remember homeViewModel::updateEmail
    }
    Card(
        onClick = {

        }, modifier = mod
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically

        )
        {
            Column(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .weight(3f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {


                Text(
                    text = wrapper.emailEntity.subject,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = wrapper.emailEntity.body,
                    maxLines = 1,
                    fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {

                Checkbox(checked = wrapper.emailEntity.alreadyOpened, onCheckedChange = {
                    onCheckedChange(
                        EmailEntity(
                            wrapper.emailEntity.id,
                            wrapper.emailEntity.subject,
                            wrapper.emailEntity.body,
                        )
                    )
                })
            }
        }
    }
}