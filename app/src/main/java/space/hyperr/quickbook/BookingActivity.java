package space.hyperr.quickbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hanks.htextview.base.HTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BookingActivity extends AppCompatActivity {

    /* Object Declarations */
    HTextView booking_screen_welcome, booking_screen_total_cost;
    Handler handler;
    Button booking_screen_previous, booking_screen_confirm;
    Spinner booking_screen_spinner;
    EditText booking_screen_first_name, booking_screen_last_name;
    String first_name, last_name, venue, d, m, y, hh, mm, ss;
    ArrayList<String> venues, venue_codes, venues2;
    ArrayList<Double> venue_rates;
    DatePicker booking_screen_date;
    TimePicker booking_screen_start_time, booking_screen_end_time;
    Date booking_date, start_time, end_time;
    FirebaseFirestore db;
    ArrayAdapter<CharSequence> adapter;

    /* Variable Declarations */
    boolean first_name_bool, last_name_bool, venue_bool, date_bool, start_time_bool, end_time_bool;
    int day, month, year, selectedOptionPosition;
    long hours;
    double cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        /* Object Initialization */
        handler = new Handler();
        first_name = "unknown";
        last_name = "unknown";
        venue = "unknown";
        d = "unknown";
        m = "unknown";
        y="unknown";
        day = 11;
        month = 4;
        year = 2019;
        hours = 0;
        cost = 0.0f;
        hh = "00";
        mm = "00";
        ss = "00";
        booking_date = new Date();
        start_time = new Date();
        end_time = new Date();
        booking_screen_welcome = findViewById(R.id.booking_screen_welcome);
        booking_screen_total_cost = findViewById(R.id.booking_screen_total_cost);
        booking_screen_previous = findViewById(R.id.buttonPrevious);
        booking_screen_confirm = findViewById(R.id.buttonConfirmBooking);
        booking_screen_spinner = findViewById(R.id.booking_screen_spinner);
        booking_screen_first_name = findViewById(R.id.booking_screen_first_name);
        booking_screen_last_name = findViewById(R.id.booking_screen_last_name);
        booking_screen_date = findViewById(R.id.booking_screen_date);
        booking_screen_start_time = findViewById(R.id.booking_screen_start_time);
        booking_screen_end_time = findViewById(R.id.booking_screen_end_time);
        db = FirebaseFirestore.getInstance();
        venues = new ArrayList<String>();
        venue_rates = new ArrayList<Double>();
        venue_codes = new ArrayList<String>();
        venues2 = new ArrayList<String>();

        /* Variable Initialization */
        first_name_bool = false;
        last_name_bool = false;
        venue_bool = false;
        date_bool = false;
        start_time_bool = true;
        end_time_bool = true;
        selectedOptionPosition = 0;

        /* Venue List Dropdown Initialization */
        try {
            db.collection("quickbook-venues").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        venues.add(document.getString("venue_name"));
                        venue_rates.add(document.getDouble("venue_rate"));
                        venue_codes.add(document.getString("venue_code"));
                        venues2.add(document.getString("venue_name") + " @ ₹ " + document.getDouble("venue_rate") + "/hr");
                    }
                    booking_screen_spinner.setAdapter(new ArrayAdapter<String>(BookingActivity.this, R.layout.spinner_item, venues2));
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }

        /* Wait 1.5s before displaying welcome message */
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                booking_screen_welcome.animateText(getString(R.string.booking_screen_welcome));
            }
        }, 1500);

        booking_screen_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BookingActivity.this, MainActivity.class));
            }
        });

        booking_screen_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> booking = new HashMap<>();
                booking.put("booking_status", false);
                booking.put("booking_timestamp", Universe.booking_date);
                booking.put("comments", "");
                booking.put("cost_per_hour", venue_rates.get(selectedOptionPosition));
                booking.put("duration", hours);
                booking.put("email", Universe.email);
                booking.put("email_status", false);
                booking.put("end_timestamp", Universe.end_time);
                booking.put("event_status", false);
                booking.put("first_name", Universe.first_name);
                booking.put("last_name", Universe.last_name);
                booking.put("mode_of_payment", "unknown");
                booking.put("notes", "");
                booking.put("payment_reference_number", "unknown");
                booking.put("payment_status", false);
                booking.put("payment_timestamp", new Date());
                booking.put("phone", "+91" + Universe.phone);
                booking.put("qr_code_url", "");
                booking.put("sequence_number", "a");
                booking.put("start_timestamp", Universe.start_time);
                booking.put("total_cost", cost);
                booking.put("venue_code", venue_codes.get(selectedOptionPosition));

                final ProgressDialog pd = new ProgressDialog(BookingActivity.this);
                pd.setMessage("Loading");
                pd.show();

                db.collection("quickbook-bookings").add(booking).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Map<String, Object> bookingExtras = new HashMap<>();
                        bookingExtras.put("qr_code_url", "https://chart.googleapis.com/chart?cht=qr&chl=" + task.getResult().getId() + "&chs=547x547&chld=L%7C0");
                        db.collection("quickbook-bookings").document(task.getResult().getId()).update(bookingExtras).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), "Booking request created successfully!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(BookingActivity.this, MainActivity.class));
                            }
                        });
                    }
                });
            }
        });

        booking_screen_first_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                first_name = charSequence.toString();
                Universe.first_name = first_name;
                if(first_name != null) first_name_bool = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(first_name_bool && last_name_bool && venue_bool && date_bool && start_time_bool && end_time_bool) switchConfirmButton(true);
                else switchConfirmButton(false);
            }
        });

        booking_screen_last_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                last_name = charSequence.toString();
                Universe.last_name = last_name;
                if(last_name != null) last_name_bool = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(first_name_bool && last_name_bool && venue_bool && date_bool && start_time_bool && end_time_bool) switchConfirmButton(true);
                else switchConfirmButton(false);
            }
        });

        booking_screen_last_name.setOnKeyListener(new View.OnKeyListener() {
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

        booking_screen_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedOptionPosition = i;
                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                venue = adapterView.getSelectedItem().toString();
                Universe.venue = venue;
                if(venue != null) venue_bool = true;
                if(first_name_bool && last_name_bool && venue_bool && date_bool && start_time_bool && end_time_bool) switchConfirmButton(true);
                else switchConfirmButton(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        booking_screen_date.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month+1;
                if(dayOfMonth<10) d = "0" + String.valueOf(dayOfMonth);
                else d = String.valueOf(dayOfMonth);
                if(month<10) m = "0" + String.valueOf(month);
                else m = String.valueOf(month);
                y = String.valueOf(year);
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-hh:mm:ss");
                Date localDate = new Date();
                if(localDate.getHours()<10) hh = "0" + String.valueOf(localDate.getHours());
                else hh = String.valueOf(localDate.getHours());
                if(localDate.getMinutes()<10) mm = "0" + String.valueOf(localDate.getHours());
                else mm = String.valueOf(localDate.getMinutes());
                if(localDate.getSeconds()<10) ss = "0" + String.valueOf(localDate.getHours());
                else ss = String.valueOf(localDate.getSeconds());
                try {
                    booking_date = formatter.parse(d + "-" + m + "-" + y + "-" + hh + ":" + mm + ":" + ss);
                    Universe.booking_date = booking_date;
                }
                catch (ParseException pe){
                    pe.printStackTrace();
                }
                finally {
                    date_bool = true;
                    if(first_name_bool && last_name_bool && venue_bool && date_bool && start_time_bool && end_time_bool) switchConfirmButton(true);
                    else switchConfirmButton(false);
                }
            }
        });

        booking_screen_start_time.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                try{
                    start_time = new SimpleDateFormat("dd-MM-yyyy-hh:mm").parse(d + "-" + m + "-" + y + "-" + i + ":" + i1);
                    Universe.start_time = start_time;
                }
                catch (ParseException pe){
                    pe.printStackTrace();
                }
                finally {
                    if(first_name_bool && last_name_bool && venue_bool && date_bool && start_time_bool && end_time_bool) switchConfirmButton(true);
                    else switchConfirmButton(false);
                }
            }
        });

        booking_screen_end_time.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                try{
                    end_time = new SimpleDateFormat("dd-MM-yyyy-hh:mm").parse(d + "-" + m + "-" + y + "-" + i + ":" + i1);
                    Universe.end_time = end_time;
                }
                catch (ParseException pe){
                    pe.printStackTrace();
                }
                finally {
                    if(first_name_bool && last_name_bool && venue_bool && date_bool && start_time_bool && end_time_bool) switchConfirmButton(true);
                    else switchConfirmButton(false);
                }
            }
        });

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
    protected void onPause() {
        finish();
        handler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    void switchConfirmButton(boolean state){
        if(state){
            booking_screen_confirm.setForeground(getDrawable(R.drawable.transparent));
            booking_screen_confirm.setClickable(true);
            hours = (end_time.getTime() - start_time.getTime())/3600000;
            cost = hours*venue_rates.get(selectedOptionPosition).doubleValue();
            booking_screen_total_cost.animateText("Total Cost is ₹ " + cost);
        }
        else {
            booking_screen_confirm.setForeground(getDrawable(R.drawable.dg));
            booking_screen_confirm.setClickable(false);
        }
    }

}
