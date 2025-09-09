package org.apie.lernhero

import android.app.Application
import com.google.firebase.FirebaseApp
import com.lernhero.di.initializeKoin
import org.koin.android.ext.koin.androidContext

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKoin(
            config = {
                androidContext(this@MyApp)
            }
        )
        FirebaseApp.initializeApp(this)

    }
}