package br.app.saidabet.ui.emergency

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.app.saidabet.ui.theme.SdBg
import br.app.saidabet.ui.theme.SdDanger
import br.app.saidabet.ui.theme.SdGreen
import br.app.saidabet.ui.theme.SdMuted
import br.app.saidabet.ui.theme.SdSurface
import br.app.saidabet.ui.theme.SdText

@Composable
fun EmergencyScreen(
    supportName: String,
    supportPhone: String,
    onSaveContact: (name: String, phone: String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var name by remember(supportName) { mutableStateOf(supportName) }
    var phone by remember(supportPhone) { mutableStateOf(supportPhone) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SdBg)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Voltar", tint = SdText)
            }
            Text("Emergência", color = SdText, style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
        }

        Text(
            "Bateu a fissura? Respira. A vontade passa. Você não precisa enfrentar isso sozinho.",
            color = SdMuted,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:188"))
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SdDanger),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Ligar 188 — CVV (24h)", color = SdText)
        }

        Text(
            "Centro de Valorização da Vida — ligação gratuita, sigilosa.",
            color = SdMuted,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(Modifier.height(24.dp))

        Text("Contato de apoio", color = SdText)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SdGreen,
                focusedLabelColor = SdGreen,
                cursorColor = SdGreen,
                unfocusedContainerColor = SdSurface,
                focusedContainerColor = SdSurface
            )
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("WhatsApp (com DDD)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SdGreen,
                focusedLabelColor = SdGreen,
                cursorColor = SdGreen,
                unfocusedContainerColor = SdSurface,
                focusedContainerColor = SdSurface
            )
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = { onSaveContact(name.trim(), phone.trim()) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = SdSurface),
            shape = RoundedCornerShape(12.dp)
        ) { Text("Salvar contato", color = SdGreen) }

        Spacer(Modifier.height(12.dp))

        val digits = phone.filter { it.isDigit() }
        Button(
            onClick = {
                if (digits.isBlank()) return@Button
                val wa = if (digits.startsWith("55")) digits else "55$digits"
                val text = Uri.encode("Oi, tô numa hora difícil com vontade de apostar. Pode me ajudar a distrair um pouco?")
                val uri = Uri.parse("https://wa.me/$wa?text=$text")
                context.startActivity(Intent(Intent.ACTION_VIEW, uri))
            },
            enabled = digits.length >= 10,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SdGreen),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                if (name.isNotBlank()) "WhatsApp pra $name" else "Abrir WhatsApp",
                color = SdBg
            )
        }
    }
}
