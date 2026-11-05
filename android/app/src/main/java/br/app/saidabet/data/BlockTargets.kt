package br.app.saidabet.data

/**
 * Lista inicial BR de apps/sites de apostas.
 * Pacotes reais variam por loja; nomes genéricos + packages conhecidos.
 * Domínios usados na heurística de Chrome/WebView via Accessibility.
 */
data class BlockTarget(
    val id: String,
    val displayName: String,
    val packageNames: List<String> = emptyList(),
    val domains: List<String> = emptyList(),
    val enabledByDefault: Boolean = true
)

object DefaultBlockTargets {
    val all: List<BlockTarget> = listOf(
        BlockTarget(
            id = "bet365",
            displayName = "Bet365",
            packageNames = listOf("com.bet365.android.app", "com.bet365Mobile.droid"),
            domains = listOf("bet365.com", "bet365.bet.br")
        ),
        BlockTarget(
            id = "blaze",
            displayName = "Blaze",
            packageNames = listOf("com.blaze.casino", "com.blaze"),
            domains = listOf("blaze.com", "blaze.bet.br")
        ),
        BlockTarget(
            id = "betano",
            displayName = "Betano",
            packageNames = listOf("com.kaizengaming.betano", "bg.betano"),
            domains = listOf("betano.bet.br", "betano.com")
        ),
        BlockTarget(
            id = "sportingbet",
            displayName = "Sportingbet",
            packageNames = listOf("com.sportingbet.android", "sports.sportingbet.android"),
            domains = listOf("sportingbet.bet.br", "sportingbet.com")
        ),
        BlockTarget(
            id = "pixbet",
            displayName = "Pixbet",
            packageNames = listOf("com.pixbet.app", "br.com.pixbet"),
            domains = listOf("pixbet.bet.br", "pixbet.com")
        ),
        BlockTarget(
            id = "stake",
            displayName = "Stake",
            packageNames = listOf("com.stake.android", "com.stake"),
            domains = listOf("stake.com", "stake.bet.br")
        ),
        BlockTarget(
            id = "esportesdasorte",
            displayName = "Esportes da Sorte",
            packageNames = listOf("com.esportesdasorte.app", "br.com.esportesdasorte"),
            domains = listOf("esportesdasorte.bet.br", "esportesdasorte.com")
        ),
        BlockTarget(
            id = "betfair",
            displayName = "Betfair",
            packageNames = listOf("com.betfair.android.app"),
            domains = listOf("betfair.bet.br", "betfair.com")
        ),
        BlockTarget(
            id = "rivalo",
            displayName = "Rivalo",
            packageNames = listOf("com.rivalo.app"),
            domains = listOf("rivalo.bet.br", "rivalo.com")
        ),
        BlockTarget(
            id = "novibet",
            displayName = "Novibet",
            packageNames = listOf("com.novibet.android"),
            domains = listOf("novibet.bet.br", "novibet.com")
        ),
        BlockTarget(
            id = "mrjack",
            displayName = "MrJack",
            packageNames = listOf("com.mrjack.bet", "br.com.mrjack"),
            domains = listOf("mrjack.bet.br", "mrjack.bet")
        ),
        BlockTarget(
            id = "superbet",
            displayName = "Superbet",
            packageNames = listOf("com.superbet.sport"),
            domains = listOf("superbet.bet.br", "superbet.com")
        ),
        BlockTarget(
            id = "betnacional",
            displayName = "Betnacional",
            packageNames = listOf("br.com.betnacional"),
            domains = listOf("betnacional.bet.br")
        ),
        BlockTarget(
            id = "generic_crash",
            displayName = "Sites crash / tigrinho (heurística)",
            packageNames = emptyList(),
            domains = listOf("tigrinho", "crash", "aviator", "mines", "fortune-tiger"),
            enabledByDefault = true
        )
    )
}
