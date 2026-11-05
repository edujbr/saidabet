package br.app.saidabet.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.app.saidabet.ui.theme.SdBg
import br.app.saidabet.ui.theme.SdDanger
import br.app.saidabet.ui.theme.SdGreen
import br.app.saidabet.ui.theme.SdMuted
import br.app.saidabet.ui.theme.SdSurface
import br.app.saidabet.ui.theme.SdText
import java.util.Locale

@Composable
fun HomeScreen(
    daysClean: Long,
    dailySpend: Float,
    moneySaved: Float,
    accessibilityOn: Boolean,
    trialDaysLeft: Int,
    unlocked: Boolean,
    onDailySpendChange: (Float) -> Unit,
    onResetStreak: () -> Unit,
    onOpenAccessibility: () -> Unit,
    onOpenBlocklist: () -> Unit,
    onOpenEmergency: () -> Unit,
    onOpenPaywall: () -> Unit
) {
    var editing by remember { mutableStateOf(false) }
    var spendText by remember(dailySpend) { mutableStateOf(dailySpend.toInt().toString()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SdBg)
            .padding(20.dp)
    ) {
        Text("Sai da Bet", color = SdGreen, style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
        Text("1 dia de cada vez 💚", color = SdMuted)

        Spacer(Modifier.height(24.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = SdSurface),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("DIAS LIMPOS", color = SdMuted)
                Text(
                    "$daysClean",
                    color = SdGreen,
                    style = androidx.compose.material3.MaterialTheme.typography.displayLarge
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Economia estimada: R$ ${String.format(Locale("pt", "BR"), "%.2f", moneySaved)}",
                    color = SdText
                )
                Text("com base no que você gastava por dia", color = SdMuted)
            }
        }

        Spacer(Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = SdSurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Quanto você gastava por dia?", color = SdText)
                Spacer(Modifier.height(8.dp))
                if (editing) {
                    OutlinedTextField(
                        value = spendText,
                        onValueChange = { spendText = it.filter { c -> c.isDigit() || c == ',' || c == '.' } },
                        label = { Text("R$ / dia") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SdGreen,
                            focusedLabelColor = SdGreen,
                            cursorColor = SdGreen
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row {
                        TextButton(onClick = {
                            val v = spendText.replace(',', '.').toFloatOrNull() ?: dailySpend
                            onDailySpendChange(v)
                            editing = false
                        }) { Text("Salvar", color = SdGreen) }
                        TextButton(onClick = { editing = false }) { Text("Cancelar", color = SdMuted) }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("R$ ${String.format(Locale("pt", "BR"), "%.2f", dailySpend)}", color = SdGreen)
                        TextButton(onClick = { editing = true }) { Text("Editar", color = SdGreen) }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        if (!accessibilityOn) {
            Card(
                colors = CardDefaults.cardColors(containerColor = SdSurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Warning, null, tint = SdDanger, modifier = Modifier.padding(end = 12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Bloqueio desligado", color = SdText)
                        Text("Ative a Acessibilidade pra detectar bets.", color = SdMuted)
                    }
                    TextButton(onClick = onOpenAccessibility) { Text("Ativar", color = SdGreen) }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        val status = when {
            unlocked -> "Assinatura ativa (modo local)"
            trialDaysLeft > 0 -> "Trial: $trialDaysLeft dia(s) restantes"
            else -> "Trial acabou — assine pra continuar"
        }
        Text(status, color = SdMuted)
        if (!unlocked && trialDaysLeft <= 0) {
            TextButton(onClick = onOpenPaywall) { Text("Ver planos", color = SdGreen) }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onOpenEmergency,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SdDanger),
            shape = RoundedCornerShape(14.dp)
        ) { Text("SOS — Emergência", color = SdText) }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = onOpenBlocklist,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SdGreen),
            shape = RoundedCornerShape(14.dp)
        ) { Text("Lista de bloqueios", color = SdBg) }

        TextButton(
            onClick = onResetStreak,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Zerar dias limpos (recomeçar)", color = SdMuted) }
    }
}
