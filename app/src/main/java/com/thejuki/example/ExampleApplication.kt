package com.thejuki.example

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.multidex.MultiDexApplication
import com.thejuki.example.activity.SplashActivity

/**
 * Example Application
 *
 * Maintains the global application state. Used for showing the Lollipin
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class ExampleApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(AppLifecycleTracker())
    }
}


class AppLifecycleTracker : Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(activity: Activity?) {}

    override fun onActivityResumed(activity: Activity?) {}

    override fun onActivityDestroyed(activity: Activity?) {}

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {}

    private var numStarted = 0

    override fun onActivityStarted(activity: Activity?) {
        if (numStarted == 0) {
            if (LockManager.getInstance().appLock != null) {
                (LockManager.getInstance().appLock as AppLockImpl<*>).onActivityResumed(activity)
            }
        }
        numStarted++
    }

    override fun onActivityStopped(activity: Activity?) {
        numStarted--
        if (numStarted == 0) {
            if (activity is SplashActivity) {
                return
            }

            if (LockManager.getInstance().appLock != null) {
                LockManager.getInstance().appLock.setLastActiveMillis()
            }
        }
    }

}

