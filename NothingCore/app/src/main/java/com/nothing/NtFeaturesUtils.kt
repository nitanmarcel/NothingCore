package com.nothing

import android.util.Log

object NtFeaturesUtils {

    const val NTF_DOUBLE_TAP_POWER = 42
    const val NTF_BATTERY_SAVER_MODE = 47
    const val NTF_SCREENSHOT_SOUND = 1
    const val NTF_SCREEN_ON_OFF_ANIMATION = 26
    const val NTF_NAVBAR_SWITCH = 0

    private val supportedFeatures = mutableMapOf<Int, Boolean>()

    init {
        supportedFeatures[NTF_DOUBLE_TAP_POWER] = true
        supportedFeatures[NTF_BATTERY_SAVER_MODE] = true
        supportedFeatures[NTF_SCREENSHOT_SOUND] = true
        supportedFeatures[NTF_SCREEN_ON_OFF_ANIMATION] = true
        supportedFeatures[NTF_NAVBAR_SWITCH] = true

    }

    @JvmStatic
    fun isSupport(vararg features: Int): Boolean {
        for (feature in features) {
            if (!supportedFeatures.containsKey(feature) || !supportedFeatures[feature]!!) {
                return false
            }
        }
        return true
    }
}
