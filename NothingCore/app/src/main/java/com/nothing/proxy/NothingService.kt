package com.nothing.proxy

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import android.os.RemoteException
import android.util.Log
import java.io.FileDescriptor
import java.io.PrintWriter


class NothingService : Service() {
    private var mContext: Context? = null
    private val mStub = object : INothingService.Stub() {
        // Needs the apk signed with product keys
        // https://stackoverflow.com/questions/37586255/signing-my-android-application-as-system-app
        override fun deviceGoToSleep(j: Long) {
            val powerManager = mContext?.getSystemService(Context.POWER_SERVICE) as PowerManager
            val isScreenOn = powerManager.isInteractive
            if (isScreenOn) {
                val methodName = "goToSleep"
                PowerManager::class.java.getMethod(methodName, Long::class.javaPrimitiveType)
                    .invoke(powerManager, j)
            }
        }

        override fun isUninstallableSystemApp(str: String?): Boolean {
            return false
        }

        override fun uninstallSystemApp(str: String?, i: Int) {
            return
        }
    }

    fun deviceGoToSleep(j: Long) {
    }

    fun isUninstallableSystemApp(str: String): Boolean {
        return false
    }

    @Throws(RemoteException::class)  // com.nothing.proxy.INothingService
    fun uninstallSystemApp(str: String, i: Int) {
        return
    }

    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY_COMPATIBILITY //super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d("NothingService", "onBind")
        return mStub
    }

    override fun dump(fd: FileDescriptor?, writer: PrintWriter?, args: Array<out String>?) {
        super.dump(fd, writer, args)
    }
}