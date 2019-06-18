package space.hyperr.quickbook;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.hanks.htextview.base.HTextView;

import io.fabric.sdk.android.Fabric;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class SplashActivity extends AppCompatActivity {

    /**
     * Copyright © Volatile, Inc - All Rights Reserved
     * Unauthorized copying of this file, via any medium is strictly prohibited
     * Proprietary and confidential
     * Written by Siddhant Vinchurkar <siddhantvinchurkar@gmail.com>, December 2018
     */

    /* Object and variable declarations */
    PulsatorLayout splashScreenPulsator;
    HTextView splashScreenTitle;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);

        /* Listen for dynamic links */
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            System.out.println("App opened using " + deepLink.toString());
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Dynamic link failed to work as expected!");
                    }
                });

        /* Check for root access */
        if(Universe.isDeviceRooted(getApplicationContext())) Universe.isDeviceRooted = true;

        /* Handle context creation */
        splashScreenPulsator = findViewById(R.id.splash_screen_pulsator);
        splashScreenTitle = findViewById(R.id.splash_screen_title);
        handler = new Handler();

        /* Start pulsation visual effects */
        splashScreenPulsator.start();

        /* Change splash screen title after a 3s pause */
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                splashScreenTitle.animateText("SJC");
            }
        }, 3000);

        /* Check for internet access */
        if(Universe.isNetworkAvailable(getApplicationContext())){

            /* Check if the user has launched the app before */
            if(Universe.isAppLaunchFirst(getApplicationContext())){

                /* Navigate to the main activity after 5s */
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashActivity.this, IntroductionActivity1.class));
                        overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in); // Transition animation to create a smooth flow to the introduction screen
                    }
                }, 5000);

            }

            else{

                /* Navigate to the main activity after 5s */
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in); // Transition animation to create a smooth flow to the main content
                    }
                }, 5000);

            }

        }

        else {

            /* TODO: Notify user and kill app */

        }

    }

    @Override
    protected void onResume() {

        /* The following code will enable immersive mode for the splash screen */

            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        super.onResume();
    }

    @Override
    protected void onPause() {

        /* The 'finish()' method will end the splash activity to prevent it from appearing
         * should the user press the back button */

        finish();
        handler.removeCallbacksAndMessages(null);

        super.onPause();
    }
}
