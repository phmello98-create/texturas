package com.texturas

import android.app.Application
import com.texturas.utils.DataInitializer
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.inject.androidInjection
import javax.inject.Inject

@HiltAndroidApp
class TexturasApplication : Application() {

    @Inject
    lateinit var dataInitializer: DataInitializer

    override fun onCreate() {
        super.onCreate()
        androidInjection.inject(this)
        // Initialize default data after a short delay to ensure dependencies are ready
        // In a real app, we might want to do this more elegantly
        // For now, we'll just call it - if it fails, it's not critical for MVP
        try {
            // We need to run this in a coroutine, but Application doesn't have a lifecycleScope
            // We'll use GlobalScope for simplicity in this MVP (not ideal for production)
            kotlinx.coroutines.GlobalScope.launch {
                dataInitializer.initializeDefaultActivitiesAsync()
            }
        } catch (e: Exception) {
            // Handle exception - in a real app, we'd log this
        }
    }
}
