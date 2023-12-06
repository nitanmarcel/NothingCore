package com.nothing.proxy

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.content.res.Resources
import android.os.Build
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
        "com.sunbird.apps.nothing",
        "com.nothing.wallpaper"
    )

    private val unsupportedApps = setOf(
        "com.nothing.camera"
    )

    private val SDK = Build.VERSION.SDK_INT


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

                    val resPath = "/data/local/tmp/res.jar"
                    addDexPathMethod.invoke(lpparam.classLoader, resPath)
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

            try {
                // Patches for Android 14
                if (lpparam.packageName == "com.nothing.launcher" && SDK == 34)
                {
                    XposedHelpers.findAndHookMethod(
                        "com.android.launcher3.util.SimpleBroadcastReceiver",
                        lpparam.classLoader,
                        "register",
                        Context::class.java,
                        Array<String>::class.java,
                        object : XC_MethodHook() {
                            @Throws(Throwable::class)
                            override fun beforeHookedMethod(param: MethodHookParam) {
                                val context = param.args[0] as Context
                                val strArr = param.args[1] as Array<String>
                                val filter = XposedHelpers.callMethod(param.thisObject, "getFilter", strArr)
                                context.registerReceiver(param.thisObject as BroadcastReceiver,
                                    filter as IntentFilter?, Context.RECEIVER_EXPORTED)
                                param.result = null
                            }
                        }
                    )
                }
            } catch (e: java.lang.Exception) {
                XposedBridge.log(e)
            }

            // Grant missing permission to launcher
            // Source: https://xdaforums.com/t/xposed-for-devs-how-to-dynamically-declare-permissions-for-a-target-app-without-altering-its-manifest-and-changing-its-signature.4440379/
        }
        if(lpparam.packageName.equals("android") && lpparam.processName.equals("android"))
        {
            try {
                val PermissionManagerService = XposedHelpers.findClass(
                    "com.android.server.pm.permission.PermissionManagerServiceImpl",
                    lpparam.classLoader
                )

                var AndroidPackageClass = "com.android.server.pm.parsing.pkg.AndroidPackage"
                if (SDK == 34) {
                    AndroidPackageClass = "com.android.server.pm.pkg.AndroidPackage"
                }

                val AndroidPackage = XposedHelpers.findClass(
                    AndroidPackageClass, lpparam.classLoader
                )

                val PermissionCallback = XposedHelpers.findClass(
                    "com.android.server.pm.permission.PermissionManagerServiceImpl\$PermissionCallback",
                    lpparam.classLoader
                )

                XposedHelpers.findAndHookMethod(PermissionManagerService, "restorePermissionState",
                    AndroidPackage,
                    Boolean::class.javaPrimitiveType,
                    String::class.java, PermissionCallback,
                    Int::class.javaPrimitiveType, object : XC_MethodHook() {
                        @Throws(Throwable::class)
                        override fun afterHookedMethod(param: MethodHookParam) {

                            val pkg = param.args[0]
                            val filterUserId = param.args[4] as Int

                            val mState = XposedHelpers.getObjectField(param.thisObject, "mState")
                            val mRegistry =
                                XposedHelpers.getObjectField(param.thisObject, "mRegistry")
                            val mPackageManagerInt =
                                XposedHelpers.getObjectField(param.thisObject, "mPackageManagerInt")

                            val packageName =
                                XposedHelpers.callMethod(pkg, "getPackageName") as String
                            val ps = XposedHelpers.callMethod(
                                mPackageManagerInt,
                                "getPackageStateInternal",
                                packageName
                            )
                                ?: return
                            val getAllUserIds = XposedHelpers.callMethod(
                                param.thisObject,
                                "getAllUserIds"
                            ) as IntArray
                            val userHandle_USER_ALL = XposedHelpers.getStaticIntField(
                                Class.forName("android.os.UserHandle"),
                                "USER_ALL"
                            )
                            val userIds =
                                if (filterUserId == userHandle_USER_ALL) getAllUserIds else intArrayOf(
                                    filterUserId
                                )
                            for (userId in userIds) {
                                val userState =
                                    XposedHelpers.callMethod(mState, "getOrCreateUserState", userId)
                                val appId = XposedHelpers.callMethod(ps, "getAppId") as Int
                                val uidState = XposedHelpers.callMethod(
                                    userState,
                                    "getOrCreateUidState",
                                    appId
                                )

                                if (packageName == "com.nothing.launcher") {
                                    grantInstallOrRuntimePermission(uidState, mRegistry,
                                        "android.permission.MANAGE_ACTIVITY_TASKS"
                                    )
                                    grantInstallOrRuntimePermission(uidState, mRegistry,
                                        "android.permission.PACKAGE_USAGE_STATS")
                                }
                            }
                        }
                    })
            } catch (e: java.lang.Exception) {
                XposedBridge.log(e)
            }
        }
        return
    }

    private fun grantInstallOrRuntimePermission(uidState: Any,
        registry: Any, permission: String
    ) {
        XposedHelpers.callMethod(
            uidState, "grantPermission",
            XposedHelpers.callMethod(registry, "getPermission", permission)
        )
        XposedBridge.log("Allowing permission")
    }
}
