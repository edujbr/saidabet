package br.app.saidabet.ui.blocklist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.app.saidabet.data.BlockTarget
import br.app.saidabet.ui.theme.SdBg
import br.app.saidabet.ui.theme.SdGreen
import br.app.saidabet.ui.theme.SdMuted
import br.app.saidabet.ui.theme.SdSurface
import br.app.saidabet.ui.theme.SdText

@Composable
fun BlocklistScreen(
    targets: List<BlockTarget>,
    disabledIds: Set<String>,
    onToggle: (String, Boolean) -> Unit,
    onBack: () -> Unit
) {
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
            Text("Bloqueios", color = SdText, style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
        }
        Text(
            "Ligue/desligue casas de aposta. O serviço de Acessibilidade usa essa lista.",
            color = SdMuted,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(Modifier.height(12.dp))

        LazyColumn {
            items(targets, key = { it.id }) { target ->
                val enabled = target.id !in disabledIds
                Card(
                    colors = CardDefaults.cardColors(containerColor = SdSurface),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(target.displayName, color = SdText)
                            val detail = buildList {
                                if (target.packageNames.isNotEmpty()) add("${target.packageNames.size} app(s)")
                                if (target.domains.isNotEmpty()) add("${target.domains.size} domínio(s)")
                            }.joinToString(" · ")
                            if (detail.isNotEmpty()) Text(detail, color = SdMuted)
                        }
                        Switch(
                            checked = enabled,
                            onCheckedChange = { onToggle(target.id, it) },
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = SdGreen,
                                checkedThumbColor = SdBg
                            )
                        )
                    }
                }
            }
        }
    }
}
