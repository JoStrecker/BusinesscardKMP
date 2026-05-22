package de.jstrecker.businesscard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import io.github.alexzhirkevich.qrose.rememberQrCodePainter

@Composable
fun BusinessCard(
    name: String,
    title: String,
    phone: String,
    mail: String,
    github: String,
    linkedin: String,
    imageUri: String,
    platformActions: PlatformActions
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 64.dp, bottom = 64.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (imageUri.isNotBlank()) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                val initials = remember(name) {
                    name.split(" ")
                        .filter { it.isNotBlank() }
                        .take(2)
                        .joinToString("") { it.take(1).uppercase() }
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text(
                        text = initials,
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            Text(text = name, fontSize = 32.sp)
            Text(
                text = title,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 80.dp)
        ) {
            if (phone.isNotBlank()) {
                ContactRow(
                    icon = Icons.Rounded.Call,
                    contentDescription = "Call",
                    text = phone,
                    onOpen = { platformActions.makeCall(phone) },
                    qrContent = "tel:$phone"
                )
            }
            if (mail.isNotBlank()) {
                ContactRow(
                    icon = Icons.Rounded.Email,
                    contentDescription = "Mail",
                    text = mail,
                    onOpen = { platformActions.sendEmail(mail) },
                    qrContent = "mailto:$mail"
                )
            }
            if (github.isNotBlank()) {
                ContactRow(
                    icon = Icons.Rounded.Info,
                    contentDescription = "GitHub",
                    text = "GitHub",
                    onOpen = { platformActions.openUrl("https://www.github.com/$github") },
                    qrContent = "https://www.github.com/$github"
                )
            }
            if (linkedin.isNotBlank()) {
                ContactRow(
                    icon = Icons.Rounded.Info,
                    contentDescription = "LinkedIn",
                    text = "LinkedIn",
                    onOpen = { platformActions.openUrl("https://www.linkedin.com/in/$linkedin") },
                    qrContent = "https://www.linkedin.com/in/$linkedin"
                )
            }
        }
    }
}

@Composable
fun ContactRow(
    icon: ImageVector,
    contentDescription: String,
    text: String,
    onOpen: () -> Unit,
    qrContent: String
) {
    var showMenu by remember { mutableStateOf(false) }
    var showQR by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier.clickable { showMenu = true }
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = text)
        }

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                text = { Text("Open") },
                onClick = {
                    showMenu = false
                    onOpen()
                }
            )
            DropdownMenuItem(
                text = { Text("Show QR") },
                onClick = {
                    showMenu = false
                    showQR = true
                }
            )
        }
    }

    if (showQR) {
        QRCodeDialog(
            content = qrContent,
            onDismiss = { showQR = false }
        )
    }
}

@Composable
fun QRCodeDialog(content: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("QR Code") },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = rememberQrCodePainter(content),
                    contentDescription = "QR code for content: $content",
                    modifier = Modifier.size(200.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}
