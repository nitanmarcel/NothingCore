package com.nothing.experience

import android.content.Context
import android.os.Bundle

class AppTracking private constructor(context: Context) {
    fun logProductEvent(eventName: String?, eventParams: Bundle?): Boolean {
        return false
    }

    fun logActivationEvent(eventName: String?, eventParams: Bundle?): Boolean {
        return false
    }

    fun logQualityEvent(eventName: String?, eventParams: Bundle?): Boolean {
        return false
    }

    companion object {
        @JvmStatic // This line is added to ensure the field is accessible from Java.
        private var instance: AppTracking? = null

        @JvmStatic
        fun getInstance(context: Context): AppTracking {
            if (instance == null) {
                synchronized(AppTracking::class.java) {
                    if (instance == null) {
                        instance = AppTracking(context)
                    }
                }
            }
            return instance!!
        }
    }
}
