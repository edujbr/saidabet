package br.app.saidabet.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.app.saidabet.ui.theme.SdBg
import br.app.saidabet.ui.theme.SdGreen
import br.app.saidabet.ui.theme.SdMuted
import br.app.saidabet.ui.theme.SdSurface
import br.app.saidabet.ui.theme.SdText

private data class Page(
    val icon: ImageVector,
    val title: String,
    val body: String
)

@Composable
fun OnboardingScreen(
    accessibilityEnabled: Boolean,
    onOpenAccessibility: () -> Unit,
    onFinished: () -> Unit
) {
    val pages = listOf(
        Page(
            Icons.Rounded.Favorite,
            "Você não é fraco",
            "As bets são feitas pra viciar: luz, som, \"quase ganhei\". O Sai da Bet é pra quem quer sair — um dia de cada vez."
        ),
        Page(
            Icons.Rounded.Block,
            "Bloqueio + dias limpos",
            "A gente detecta apps e sites de aposta e te empurra de volta. Você vê quantos dias limpos já fez e quanto dinheiro salvou."
        ),
        Page(
            Icons.Rounded.Shield,
            "Ative o bloqueio",
            "Pra funcionar, o Android pede a permissão de Acessibilidade. Usamos só pra ver se um app da lista de bets abriu — nada é enviado pra fora."
        )
    )

    var index by remember { mutableIntStateOf(0) }
    val page = pages[index]
    val last = index == pages.lastIndex

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SdBg)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            pages.indices.forEach { i ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (i <= index) SdGreen else SdSurface)
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(SdSurface),
                contentAlignment = Alignment.Center
            ) {
                Icon(page.icon, contentDescription = null, tint = SdGreen, modifier = Modifier.size(48.dp))
            }
            Spacer(Modifier.height(28.dp))
            Text(page.title, color = SdText, textAlign = TextAlign.Center,
                style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(12.dp))
            Text(page.body, color = SdMuted, textAlign = TextAlign.Center,
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge)
        }

        Column {
            if (last) {
                if (!accessibilityEnabled) {
                    Button(
                        onClick = onOpenAccessibility,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SdGreen),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Abrir Acessibilidade", color = SdBg)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Ative \"Sai da Bet — Bloqueio\" na lista. Depois volte aqui.",
                        color = SdMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))
                }
                Button(
                    onClick = onFinished,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (accessibilityEnabled) SdGreen else SdSurface),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        if (accessibilityEnabled) "Começar" else "Continuar sem ativar agora",
                        color = if (accessibilityEnabled) SdBg else SdText
                    )
                }
            } else {
                Button(
                    onClick = { index++ },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SdGreen),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Próximo", color = SdBg)
                }
                TextButton(
                    onClick = { index = pages.lastIndex },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pular", color = SdMuted)
                }
            }
            if (!last) {
                OutlinedButton(
                    onClick = { /* noop spacer alternative */ },
                    enabled = false,
                    modifier = Modifier.height(0.dp)
                ) {}
            }
        }
    }
}
