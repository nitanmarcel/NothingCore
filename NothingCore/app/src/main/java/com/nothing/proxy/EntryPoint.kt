package com.nothing.proxy

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam


class EntryPoint : IXposedHookLoadPackage {

    private val enableForRegex = setOf(
        Regex("com.nothing.*"),
        Regex("com.sunbird.apps.nothing")
    )

    // These packages should not be hooked to load nothing framework
    // TODO REGEX
    private val jarInjectBlacklist = setOf(
        "com.nothing.experimental",
        "com.nothing.icon",
        "com.nothing.appservice",
        "com.nothing.hearthstone",
        "com.nothing.cardservice",
        "com.nothing.proxy",
        "com.sunbird.apps.nothing"
    )

    private val unsupportedApps = setOf(
        "com.nothing.camera"
    )

    @Throws(Throwable::class)
    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        if (enableForRegex.any { it.containsMatchIn(lpparam.packageName) }) {

            if (!jarInjectBlacklist.contains(lpparam.packageName))
            {
                try {
                    val jarPath = "/system/framework/not-a-framework.jar"

                    val addDexPathMethod = XposedHelpers.findMethodExact(
                        "dalvik.system.BaseDexClassLoader", lpparam.classLoader,
                        "addDexPath", String::class.java
                    )

                    addDexPathMethod.invoke(lpparam.classLoader, jarPath)
                } catch (e: Exception) {
                    XposedBridge.log(e.toString())
                }

                // Other apps might crash upon loading ConfigLoader due to conflicts with their own class.
                if (lpparam.packageName != "com.nothing.launcher") {
                    XposedBridge.hookAllMethods(Class::class.java, "forName", object : XC_MethodHook() {
                        @Throws(Throwable::class)
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            if (param.args.size > 0 && param.args[0] == "com.nothing.onlineconfig.ConfigObserver") {
                                param.result = EntryPoint::class.java
                            }
                        }
                    })
                }
            }

            val build = XposedHelpers.findClass("android.os.Build", lpparam.classLoader)
            XposedHelpers.setStaticObjectField(build, "MANUFACTURER", "nothing")
            XposedHelpers.setStaticObjectField(build, "MODEL", "A065")

            if (unsupportedApps.contains(lpparam.packageName))
            {
                XposedHelpers.findAndHookMethod(
                    Activity::class.java, "onCreate",
                    Bundle::class.java, object : XC_MethodHook() {
                        override fun afterHookedMethod(param: MethodHookParam?) {
                            val activity = param!!.thisObject as Activity
                            val context: Context = activity
                            Toast.makeText(context, "NothingCore: Unsupported", Toast.LENGTH_SHORT).show();
                        }
                    }
                )
            }
        }
        else {
            return
        }
    }
}
