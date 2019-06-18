package space.hyperr.quickbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.hanks.htextview.base.HTextView;

public class IntroductionActivity1 extends AppCompatActivity {

    Handler handler;
    HTextView introduction_screen_1_title, introduction_screen_1_subtitle_1, introduction_screen_1_subtitle_2;
    Button buttonNext1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction1);

        /* Object Initialization */
        handler = new Handler();
        introduction_screen_1_title = findViewById(R.id.introduction_screen_1_title);
        introduction_screen_1_subtitle_1 = findViewById(R.id.introduction_screen_1_subtitle_1);
        introduction_screen_1_subtitle_2 = findViewById(R.id.introduction_screen_1_subtitle_2);
        buttonNext1 = findViewById(R.id.buttonNext1);

        /* Narrate Introduction */
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                introduction_screen_1_title.animateText(getString(R.string.introduction_screen_1_title));
            }
        }, 1500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                introduction_screen_1_subtitle_1.animateText(getString(R.string.introduction_screen_1_subtitle_1));
            }
        }, 2000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                introduction_screen_1_subtitle_2.animateText(getString(R.string.introduction_screen_1_subtitle_2));
            }
        }, 2500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonNext1.setForeground(getDrawable(R.drawable.transparent));
                buttonNext1.setClickable(true);
            }
        }, 3500);

        /* Handle 'Next' button click */
        buttonNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IntroductionActivity1.this, IntroductionActivity2.class));
            }
        });

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

}
