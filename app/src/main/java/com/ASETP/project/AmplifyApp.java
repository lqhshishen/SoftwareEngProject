package com.ASETP.project;

import android.app.Application;
import android.util.Log;


import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.rx.RxAmplify;

/**
 * @author MirageLee
 * @date 2020/10/29
 */
public class AmplifyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());
            Log.i("AmplifyApp", "Initialized Amplify at AmplifyApp");
        } catch (AmplifyException error) {
            Log.e("AmplifyApp", "Could not initialize Amplify at AmplifyApp", error);
        }
    }
}
