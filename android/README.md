# Sai da Bet — Android MVP

Kotlin + Jetpack Compose. Min SDK 26, target 34. Código escrito do zero (sem fork GPL).

## O que tem no MVP

1. **Onboarding** (3 telas) + pedido de Acessibilidade  
2. **Home** — dias limpos + economia estimada (valor/dia editável)  
3. **Bloqueios** — Bet365, Blaze, Betano, Sportingbet, Pixbet, Stake, Esportes da Sorte, etc. (DataStore local)  
4. **SOS** — discar 188 + salvar/abrir WhatsApp de apoio  
5. **Paywall** — trial 7 dias / R$ 14,90 (Play Billing stub + flag local de desbloqueio)  
6. **`BetBlockAccessibilityService`** — best-effort: Toast + `GLOBAL_ACTION_HOME` quando pacote/domínio da lista sobe

## Abrir no Android Studio

1. Instale [Android Studio](https://developer.android.com/studio) (Ladybug+).  
2. **File → Open** → pasta `saidabet/android`.  
3. Espere o Gradle sync.  
4. Conecte um celular com USB debugging **ou** suba um emulador API 26+.  
5. Run ▶ `app`.

## Gerar APK (debug) na linha de comando

```bash
export JAVA_HOME=/path/to/jdk-17
export ANDROID_HOME=/path/to/Android/Sdk
echo "sdk.dir=$ANDROID_HOME" > local.properties
./gradlew :app:assembleDebug
# APK: app/build/outputs/apk/debug/app-debug.apk
```

Instalar no aparelho:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

Depois de instalar: abra o app → ative **Sai da Bet — Bloqueio** em Ajustes → Acessibilidade.

## Pacote

`br.app.saidabet` (debug: `br.app.saidabet.debug`)

## Limitações conhecidas

- Bloqueio via Accessibility **não é 100% confiável** em todos os OEMs; sites no Chrome dependem de heurística de texto/URL.  
- Play Billing ainda é stub (desbloqueio local pra beta/pré-venda).  
- Sem VPN/DNS no MVP.

## Baixar o APK pronto

APK debug (beta) na Release: https://github.com/edujbr/saidabet/releases/tag/v0.1.0-debug

Package debug: `br.app.saidabet.debug`
