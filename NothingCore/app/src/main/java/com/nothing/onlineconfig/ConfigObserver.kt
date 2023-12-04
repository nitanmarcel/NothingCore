package com.nothing.onlineconfig

import android.content.ContentResolver
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import org.json.JSONArray

class ConfigObserver(
    context: Context,
    handler: Handler?,
    configUpdater: ConfigUpdater?,
    projectName: String
) :
    ContentObserver(handler) {
    private var mConfigGrabber: ConfigGrabber? = null
    private var mConfigUpdater: ConfigUpdater? = null
    private var mHandler: Handler? = null
    private var mResolver: ContentResolver? = null
    private var mUri: Uri? = null

    /* loaded from: classes4.dex */
    interface ConfigUpdater {
        fun updateConfig(jSONArray: JSONArray?)
    }

    init {
        mConfigGrabber = ConfigGrabber(context, projectName)
        mConfigUpdater = configUpdater
        mResolver = context.contentResolver
        mHandler = handler
    }

    fun register() {
    }

    fun unregister() {
        // mResolver!!.unregisterContentObserver(this)
    }

    // android.database.ContentObserver
    override fun onChange(selfChange: Boolean) {
        mConfigUpdater!!.updateConfig(mConfigGrabber!!.grabConfig())
    }
}