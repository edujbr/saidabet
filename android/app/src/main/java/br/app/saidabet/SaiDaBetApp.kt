package br.app.saidabet

import android.app.Application
import br.app.saidabet.data.PrefsRepository

class SaiDaBetApp : Application() {
    lateinit var prefs: PrefsRepository
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        prefs = PrefsRepository(this)
    }

    companion object {
        @Volatile
        lateinit var instance: SaiDaBetApp
            private set
    }
}
