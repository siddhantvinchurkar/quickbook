package space.hyperr.quickbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hanks.htextview.base.HTextView;

public class IntroductionActivity3 extends AppCompatActivity {

    /* Object Declarations */
    Handler handler;
    HTextView introduction_screen_3_title, introduction_screen_3_subtitle_1, introduction_screen_3_subtitle_2, introduction_screen_3_subtitle_3;
    Button buttonNext3;
    EditText introduction_screen_3_email_address, introduction_screen_3_phone_number;
    String email_address, phone_number;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    /* Variable Declarations */
    boolean email, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction3);

        /* Object Initialization */
        handler = new Handler();
        introduction_screen_3_title = findViewById(R.id.introduction_screen_3_title);
        introduction_screen_3_subtitle_1 = findViewById(R.id.introduction_screen_3_subtitle_1);
        introduction_screen_3_subtitle_2 = findViewById(R.id.introduction_screen_3_subtitle_2);
        introduction_screen_3_subtitle_3 = findViewById(R.id.introduction_screen_3_subtitle_3);
        buttonNext3 = findViewById(R.id.buttonNext3);
        introduction_screen_3_email_address = findViewById(R.id.introduction_screen_3_email_address);
        introduction_screen_3_phone_number = findViewById(R.id.introduction_screen_3_phone_number);
        sharedPreferences = getSharedPreferences("space.hyperr.quickbook_preferences", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        /* Variable Initialization */
        email = false;
        phone = false;
        /* Narrate Introduction */
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                introduction_screen_3_title.animateText(getString(R.string.introduction_screen_3_title));
            }
        }, 1500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                introduction_screen_3_subtitle_1.animateText(getString(R.string.introduction_screen_3_subtitle_1));
            }
        }, 2000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                introduction_screen_3_subtitle_2.animateText(getString(R.string.introduction_screen_3_subtitle_2));
            }
        }, 2500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                introduction_screen_3_subtitle_3.animateText(getString(R.string.introduction_screen_3_subtitle_3));
            }
        }, 3500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                introduction_screen_3_email_address.setVisibility(View.VISIBLE);
                introduction_screen_3_phone_number.setVisibility(View.VISIBLE);
            }
        }, 4500);

        introduction_screen_3_email_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                email_address = charSequence.toString();
                editor.putString("email_address", email_address);
                email = Universe.isEmailValid(email_address);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                editor.apply();
                if(email && phone) switchNextButton(true);
                else switchNextButton(false);
            }
        });

        introduction_screen_3_phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                phone_number = "+91" + charSequence.toString();
                editor.putString("phone_number", phone_number);
                phone = phone_number.length() == 13;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                editor.apply();
                if(email && phone) switchNextButton(true);
                else switchNextButton(false);
            }
        });

        introduction_screen_3_phone_number.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    View decorView = getWindow().getDecorView();
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                    return true;
                }
                return false;
            }
        });

        /* Handle 'Next' button click */
        buttonNext3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IntroductionActivity3.this, MainActivity.class));
                Universe.setAppLaunchFirst(getApplicationContext());
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

    void switchNextButton(boolean state){
        if(state){
            buttonNext3.setForeground(getDrawable(R.drawable.transparent));
            buttonNext3.setClickable(true);
        }
        else {
            buttonNext3.setForeground(getDrawable(R.drawable.dg));
            buttonNext3.setClickable(false);
        }
    }

    @Override
    protected void onPause() {
        finish();
        handler.removeCallbacksAndMessages(null);
        super.onPause();
    }
}
