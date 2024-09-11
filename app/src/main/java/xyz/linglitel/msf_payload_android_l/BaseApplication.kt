package xyz.linglitel.msf_payload_android_l

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class BaseApplication: Application() {

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context
    }
    @Override
    override fun onCreate() {
        super.onCreate();
        mContext=applicationContext;
    }
}