package com.nothing.proxy

import android.os.Binder
import android.os.IBinder
import android.os.IInterface
import android.os.Parcel
import android.os.RemoteException


interface INothingService : IInterface {
    @Throws(RemoteException::class)
    fun deviceGoToSleep(j: Long)

    @Throws(RemoteException::class)
    fun isUninstallableSystemApp(str: String?): Boolean

    @Throws(RemoteException::class)
    fun uninstallSystemApp(str: String?, i: Int)

    abstract class Stub : Binder(), INothingService {
        override fun asBinder(): IBinder {
            return this
        }

        init {
            attachInterface(this, "com.nothing.proxy.INothingService")
        }

        @Throws(RemoteException::class)  // android.os.Binder
        public override fun onTransact(i: Int, parcel: Parcel, parcel2: Parcel?, i2: Int): Boolean {

            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.nothing.proxy.INothingService")
            }
            if (i == 1598968902) {
                parcel2!!.writeString("com.nothing.proxy.INothingService")
                return true
            }
            if (i == 1) {
                val readLong = parcel.readLong()
                parcel.enforceNoDataAvail()
                deviceGoToSleep(readLong)
                parcel2!!.writeNoException()
            } else if (i == 2) {
                val readString = parcel.readString()
                parcel.enforceNoDataAvail()
                val isUninstallableSystemApp = isUninstallableSystemApp(readString)
                parcel2!!.writeNoException()
                parcel2.writeBoolean(isUninstallableSystemApp)
            } else if (i == 3) {
                val readString2 = parcel.readString()
                val readInt = parcel.readInt()
                parcel.enforceNoDataAvail()
                uninstallSystemApp(readString2, readInt)
                parcel2!!.writeNoException()
            } else {
                return super.onTransact(i, parcel, parcel2, i2)
            }
            return true
        }
    }
}