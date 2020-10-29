package com.ASETP.project;

import android.app.Application;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.core.Amplify;

/**
 * @author MirageLee
 * @date 2020/10/29
 */
public class AmplifyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Amplify.configure(this);
            Log.i("AmplifyApp", "Initialized Amplify at AmplifyApp");
        } catch (AmplifyException error) {
            Log.e("AmplifyApp", "Could not initialize Amplify at AmplifyApp", error);
        }
    }
}
