# Sai da Bet 💚

App pra quem quer parar de apostar: **bloqueia bets (Android)**, **conta os dias limpos**, mostra **quanto dinheiro você já economizou** e tem um **botão de emergência** (CVV 188 + WhatsApp de alguém de confiança).

Divulgação: página faceless no Instagram [@saidabet.app](https://www.instagram.com/saidabet.app/).

## 🔗 Links públicos

| O quê | Link |
|---|---|
| Lista de espera (landing) | https://edujbr.github.io/saidabet/ |
| **App web (PWA) — funciona no iPhone** | https://edujbr.github.io/saidabet/app/ |
| **APK Android (beta)** | https://github.com/edujbr/saidabet/releases/tag/v0.1.0-debug |
| Download direto do APK | https://github.com/edujbr/saidabet/releases/download/v0.1.0-debug/SaiDaBet-0.1.0-debug.apk |

## 📱 Como usar

**iPhone (PWA):** abra https://edujbr.github.io/saidabet/app/ no **Safari** → botão **Compartilhar** → **Adicionar à Tela de Início**. Abre em tela cheia, com ícone, e funciona offline.

**Android (APK):** baixe o APK → permita “instalar de fontes desconhecidas” → abra o app → ative **Sai da Bet — Bloqueio** em Configurações › Acessibilidade.

## ⚖️ Android x iPhone — o que cada versão faz

| Recurso | Android (APK) | iPhone / Web (PWA) |
|---|---|---|
| Onboarding | ✅ | ✅ |
| Contador de dias limpos | ✅ | ✅ (salvo no aparelho) |
| Dinheiro economizado | ✅ | ✅ |
| Botão de emergência (188 / WhatsApp) | ✅ | ✅ |
| Respiração guiada 90s | — | ✅ |
| **Bloqueio automático de apps/sites de aposta** | ✅ best-effort via Acessibilidade | ❌ **impossível em PWA** — a Apple não deixa um site bloquear apps. O app ensina o bloqueio nativo do iOS (Tempo de Uso) e copia a lista de sites. |
| Assinatura R$ 14,90/mês | tela pronta, cobrança ainda não ligada | só prévia, nenhuma cobrança |

App nativo pra App Store (iOS) ainda **não existe** — a meta é juntar R$ 1.500 pra licença de desenvolvedor Apple.

## 📁 Estrutura

```
index.html        → landing / lista de espera (GitHub Pages, raiz)
app/              → PWA (HTML/CSS/JS, manifest, service worker, ícones) → /saidabet/app/
android/          → código-fonte do app Android (Kotlin + Jetpack Compose)
docs/             → textos do Instagram, escopo técnico, pré-venda
releases/         → índice das versões (APK fica no GitHub Releases)
```

O GitHub Pages publica a partir da raiz da branch `main`.

## 🔒 Privacidade

- O PWA guarda tudo **só no aparelho** (localStorage). Nada é enviado a servidor.
- A landing envia e-mail/WhatsApp da lista de espera para o Supabase usando apenas a chave pública (anon), que é feita pra ficar no navegador. Nenhuma senha, keystore ou chave privada está no repositório.

## 💰 Meta

R$ 1.500 pra licença Apple. Assinatura alvo R$ 14,90/mês · pré-venda Fundador R$ 39 / 3 meses no Pix ([docs/presale.md](docs/presale.md)).

## Aviso

Ferramenta de apoio; não substitui tratamento profissional. Precisa conversar agora? **CVV 188** (24h, gratuito).
