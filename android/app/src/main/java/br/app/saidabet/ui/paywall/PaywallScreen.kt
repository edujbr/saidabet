package br.app.saidabet.ui.paywall

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.app.saidabet.ui.theme.SdBg
import br.app.saidabet.ui.theme.SdGreen
import br.app.saidabet.ui.theme.SdMuted
import br.app.saidabet.ui.theme.SdSurface
import br.app.saidabet.ui.theme.SdText

@Composable
fun PaywallScreen(
    trialDaysLeft: Int,
    unlocked: Boolean,
    onUnlockLocal: () -> Unit,
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
            Text("Assinatura", color = SdText, style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
        }

        Spacer(Modifier.height(12.dp))

        Text(
            if (unlocked) "Você já desbloqueou o Sai da Bet neste aparelho."
            else if (trialDaysLeft > 0) "Seu trial grátis: $trialDaysLeft dia(s)."
            else "Seu trial de 7 dias acabou.",
            color = SdMuted
        )

        Spacer(Modifier.height(20.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = SdSurface),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(24.dp)) {
                Text("Mensal", color = SdMuted)
                Text("R\$ 14,90", color = SdGreen, style = androidx.compose.material3.MaterialTheme.typography.displayLarge)
                Text("/mês · cancele quando quiser", color = SdMuted)
                Spacer(Modifier.height(12.dp))
                Text("• Bloqueio de bets", color = SdText)
                Text("• Contador de dias limpos", color = SdText)
                Text("• Economia estimada", color = SdText)
                Text("• Botão SOS (188 + WhatsApp)", color = SdText)
            }
        }

        Spacer(Modifier.height(16.dp))

        // Stub Play Billing — local unlock for beta / pré-venda
        Button(
            onClick = onUnlockLocal,
            enabled = !unlocked,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SdGreen),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                if (unlocked) "Já ativo" else "Ativar beta (stub Play Billing)",
                color = SdBg
            )
        }

        Text(
            "Play Billing real entra na versão da Store. No beta, fundadores desbloqueiam com o Pix da pré-venda.",
            color = SdMuted,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(Modifier.weight(1f))
        TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Voltar", color = SdMuted)
        }
        Text(
            "Ferramenta de apoio; não substitui tratamento. Precisa de ajuda? Ligue 188.",
            color = SdMuted,
            modifier = Modifier.padding(8.dp)
        )
    }
}
