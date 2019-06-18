package space.hyperr.quickbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hanks.htextview.base.HTextView;

public class IntroductionActivity2 extends AppCompatActivity {

    Handler handler;
    HTextView introduction_screen_2_title, introduction_screen_2_subtitle_1, introduction_screen_2_subtitle_2, introduction_screen_2_subtitle_3;
    Button buttonNext2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction2);

        /* Object Initialization */
        handler = new Handler();
        introduction_screen_2_title = findViewById(R.id.introduction_screen_2_title);
        introduction_screen_2_subtitle_1 = findViewById(R.id.introduction_screen_2_subtitle_1);
        introduction_screen_2_subtitle_2 = findViewById(R.id.introduction_screen_2_subtitle_2);
        introduction_screen_2_subtitle_3 = findViewById(R.id.introduction_screen_2_subtitle_3);
        buttonNext2 = findViewById(R.id.buttonNext2);

        /* Narrate Introduction */
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                introduction_screen_2_title.animateText(getString(R.string.introduction_screen_2_title));
            }
        }, 1500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                introduction_screen_2_subtitle_1.animateText(getString(R.string.introduction_screen_2_subtitle_1));
            }
        }, 2000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                introduction_screen_2_subtitle_2.animateText(getString(R.string.introduction_screen_2_subtitle_2));
            }
        }, 2500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                introduction_screen_2_subtitle_3.animateText(getString(R.string.introduction_screen_2_subtitle_3));
            }
        }, 3000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(getApplication() != null) ActivityCompat.requestPermissions(IntroductionActivity2.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},0);
            }
        }, 3500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonNext2.setForeground(getDrawable(R.drawable.transparent));
                buttonNext2.setClickable(true);
            }
        }, 4500);

        /* Handle 'Next' button click */
        buttonNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IntroductionActivity2.this, IntroductionActivity3.class));
            }
        });

    }

    @Override
    protected void onResume() {

        /* Queue transition animation */
        overridePendingTransition(R.anim.transition_toward_right, R.anim.transition_toward_left);

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

}
