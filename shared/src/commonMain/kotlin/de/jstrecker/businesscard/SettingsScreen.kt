package de.jstrecker.businesscard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun Settings(
    name: String,
    title: String,
    phone: String,
    mail: String,
    github: String,
    linkedin: String,
    imageUri: String,
    onSave: (name: String, title: String, phone: String, mail: String, github: String, linkedin: String) -> Unit,
    onImagePicked: (String) -> Unit,
    platformActions: PlatformActions
) {
    val nameState = remember(name) { mutableStateOf(name) }
    val titleState = remember(title) { mutableStateOf(title) }
    val phoneState = remember(phone) { mutableStateOf(phone) }
    val mailState = remember(mail) { mutableStateOf(mail) }
    val gitState = remember(github) { mutableStateOf(github) }
    val linkedinState = remember(linkedin) { mutableStateOf(linkedin) }

    val pickImage = platformActions.rememberImagePicker(onImagePicked)

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                label = { Text("Name") },
                value = nameState.value,
                onValueChange = { nameState.value = it },
            )
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                label = { Text("Title") },
                value = titleState.value,
                onValueChange = { titleState.value = it },
            )
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                label = { Text("Phone") },
                value = phoneState.value,
                onValueChange = { phoneState.value = it },
            )
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                label = { Text("Mail") },
                value = mailState.value,
                onValueChange = { mailState.value = it },
            )
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                label = { Text("GitHub") },
                value = gitState.value,
                onValueChange = { gitState.value = it },
            )
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                label = { Text("LinkedIn") },
                value = linkedinState.value,
                onValueChange = { linkedinState.value = it },
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (imageUri.isNotBlank()) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(onClick = { pickImage() }) {
                Text("Pick Image")
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    onSave(
                        nameState.value,
                        titleState.value,
                        phoneState.value,
                        mailState.value,
                        gitState.value,
                        linkedinState.value
                    )
                }
            ) {
                Text("Save")
            }
        }
    }
}
