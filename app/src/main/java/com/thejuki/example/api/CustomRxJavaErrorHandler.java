package com.thejuki.example.api;

import android.util.Log;

import java.io.IOException;

import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Custom RxJava Error Handler
 * <p>
 * Handles RxJava Exceptions. IOException and InterruptedException will crash the app if left out.
 *
 * @author <strong>TheJuki</strong> (<a href="https://github.com/TheJuki">GitHub</a>)
 * @version 1.0
 * @see <a href="https://github.com/ReactiveX/RxJava/wiki/What%27s-different-in-2.0#error-handling</a>
 */
public class CustomRxJavaErrorHandler {

    public static void setErrorHandler() {
        RxJavaPlugins.setErrorHandler(e -> {
            if (e instanceof UndeliverableException) {
                e = e.getCause();
            }
            if ((e instanceof IOException)) {
                // fine, irrelevant network problem or API that throws on cancellation
                return;
            }
            if (e instanceof InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return;
            }
            if ((e instanceof NullPointerException) || (e instanceof IllegalArgumentException)) {
                // that's likely a bug in the application
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                return;
            }
            if (e instanceof IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                return;
            }
            Log.w("Undeliverable exception", e);
        });
    }
}
