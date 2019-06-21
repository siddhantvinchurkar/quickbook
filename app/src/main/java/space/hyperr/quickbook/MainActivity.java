package space.hyperr.quickbook;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    /* Object Declarations */
    FloatingActionButton fab1, fab2;
    String bookingId;
    FirebaseFirestore db;
    StringBuilder classList;
    String[] week = {"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    String[] month = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    TextView main_screen_placeholder_1, main_screen_placeholder_2, main_screen_placeholder_3, main_screen_main_content;
    SharedPreferences sharedPreferences;

    /* Variable Declarations */
    int REQUEST_CODE_QR_SCAN;
    boolean booking_status, payment_status, event_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(getDrawable(R.drawable.ic_launcher));
        setSupportActionBar(toolbar);

        /* Variable Initialization */
        REQUEST_CODE_QR_SCAN = 101;
        booking_status = false;
        payment_status = false;
        event_status = false;

        /* Object Initialization */
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        main_screen_placeholder_1 = findViewById(R.id.main_screen_placeholder_1);
        main_screen_placeholder_2 = findViewById(R.id.main_screen_placeholder_2);
        main_screen_placeholder_3 = findViewById(R.id.main_screen_placeholder_3);
        main_screen_main_content = findViewById(R.id.main_screen_main_content);
        bookingId = "unknown";
        db = FirebaseFirestore.getInstance();
        classList = new StringBuilder();
        sharedPreferences = getSharedPreferences("space.hyperr.quickbook_preferences", MODE_PRIVATE);

        Universe.email = sharedPreferences.getString("email_address", "siddhantvinchurkar@gmail.com");
        Universe.phone = sharedPreferences.getString("phone_number", "+919900608821");

        db.collection("quickbook-bookings").whereEqualTo("email", Universe.email).whereEqualTo("phone", Universe.phone).orderBy("first_name").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    main_screen_main_content.setText(null);
                    if (queryDocumentSnapshots.getDocuments().toArray().length > 1) {
                        main_screen_placeholder_1.setVisibility(View.GONE);
                        main_screen_placeholder_2.setVisibility(View.GONE);
                        main_screen_placeholder_3.setVisibility(View.GONE);
                        main_screen_main_content.setVisibility(View.VISIBLE);
                    } else {
                        main_screen_placeholder_1.setVisibility(View.VISIBLE);
                        main_screen_placeholder_2.setVisibility(View.VISIBLE);
                        main_screen_placeholder_3.setVisibility(View.VISIBLE);
                        main_screen_main_content.setVisibility(View.GONE);
                    }
                    classList = new StringBuilder();
                    classList.append("Here's your booking history: <br /><br />");
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        booking_status = doc.getBoolean("booking_status");
                        payment_status = doc.getBoolean("payment_status");
                        event_status = doc.getBoolean("event_status");
                        Date start_time = doc.getDate("start_timestamp"), end_time = doc.getDate("end_timestamp");
                        classList.append("<b>");
                        classList.append(doc.get("first_name"));
                        classList.append(" ");
                        classList.append(doc.get("last_name"));
                        classList.append("<br />");
                        Calendar newCalendar = Calendar.getInstance();
                        newCalendar.setTime(doc.getDate("start_timestamp"));
                        classList.append(week[newCalendar.get(Calendar.DAY_OF_WEEK)]);
                        classList.append(" ");
                        classList.append(month[newCalendar.get(Calendar.MONTH)]);
                        classList.append(" ");
                        classList.append(newCalendar.get(Calendar.DATE));
                        classList.append(", ");
                        if (start_time != null)
                            classList.append(Universe.generateTimeString(start_time));
                        classList.append(" - ");
                        if (end_time != null)
                            classList.append(Universe.generateTimeString(end_time));
                        classList.append(" - ");
                        classList.append(doc.getString("venue_name"));
                        classList.append("<br />");
                        if (!booking_status) {
                            classList.append("<span style=\"color: ");
                            classList.append("#AA0000");
                            classList.append("\">");
                            classList.append("Booking Unconfirmed");
                            classList.append("</span>");
                        } else {
                            classList.append("<span style=\"color: ");
                            classList.append("#00AA00");
                            classList.append("\">");
                            classList.append("Booking Confirmed!");
                            classList.append("</span>");
                        }
                        classList.append("<br />");
                        if (!payment_status) {
                            classList.append("<span style=\"color: ");
                            classList.append("#AA0000");
                            classList.append("\">");
                            classList.append("Payment Unconfirmed");
                            classList.append("</span>");
                        } else {
                            classList.append("<span style=\"color: ");
                            classList.append("#00AA00");
                            classList.append("\">");
                            classList.append("Payment Confirmed!");
                            classList.append("</span>");
                        }
                        classList.append("<br />");
                        if (!event_status) {
                            classList.append("<span style=\"color: ");
                            classList.append("#AA0000");
                            classList.append("\">");
                            classList.append("Event Unconfirmed");
                            classList.append("</span>");
                        } else {
                            classList.append("<span style=\"color: ");
                            classList.append("#00AA00");
                            classList.append("\">");
                            classList.append("Event Completed!");
                            classList.append("</span>");
                        }
                        classList.append("</b><br /><br /><br />");
                    }
                    main_screen_main_content.setText(Html.fromHtml(classList.toString()));
                }
            }
        });

        /* Handle QR Code Scan Button */
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, QrCodeActivity.class), REQUEST_CODE_QR_SCAN);
                /* Queue transition animation */
                overridePendingTransition(R.anim.transition_toward_right, R.anim.transition_toward_left);
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, BookingActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {

        /* Queue transition animation */
        overridePendingTransition(R.anim.transition_toward_right, R.anim.transition_toward_left);

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode != Activity.RESULT_OK) {
            if(data==null) return;
            if(data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image")!=null) Toast.makeText(getApplicationContext(), "Error in reading QR code", Toast.LENGTH_SHORT).show();
            return;
        }

        if(requestCode == REQUEST_CODE_QR_SCAN){
            if (data == null) return;
            bookingId = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Map<String, Object> bookingExtras = new HashMap<>();
            bookingExtras.put("event_status", true);
            db.collection("quickbook-bookings").document(bookingId).update(bookingExtras);
            Toast.makeText(getApplicationContext(), bookingId, Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
