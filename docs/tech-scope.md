# Escopo técnico — Sai da Bet (MVP)

## Decisão
- Bloqueio via **Accessibility Service** (detecta apps/sites de aposta abertos e oferece fechar / avisar).
- Sem VPN no MVP (menos atrito na revisão da Play Store).
- DNS local opcional como fase 2.
- Código **escrito do zero** (Kotlin + Jetpack Compose). Referências DetoxDroid/Nudge só como estudo — **não fork GPL**.

## Pacotes a bloquear (lista inicial BR)
Bet365, Sportingbet, Betano, Blaze, Stake, Pixbet, Esportes da Sorte, Betfair, Rivalo, Novibet, MrJack, Superbet, e domínios genéricos *bet*, *blaze*, *crash*, *tigrinho* em WebView/Chrome via Accessibility (heurística + lista editável).

## Telas MVP
1. Onboarding (3 passos + pedir permissão Accessibility)
2. Home: dias limpos + dinheiro estimado economizado
3. Lista de bloqueios (ligar/desligar)
4. Botão emergência → 188 / contato de apoio
5. Paywall (Play Billing) após trial 7 dias

## Fora do MVP
iOS, comunidade, coach, gamificação, VPN.

## Pra atingir R$ 1.500
Pré-venda Pix R$ 39 (3 meses) em paralelo ao desenvolvimento.

## Implementação
- Projeto: `android/` (Kotlin + Compose)
- APK debug: `Release v0.1.0-debug (GitHub)` (gerado 2026-11-05 America/Cuiaba)
- Package debug: `br.app.saidabet.debug`
