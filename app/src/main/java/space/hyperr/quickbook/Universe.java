package space.hyperr.quickbook;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;

import com.scottyab.rootbeer.RootBeer;

import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class Universe {

    /* The following variable will store the root access state of the device */

    static boolean isDeviceRooted = false;

    /* The following object will store the device name and manufacturer */

    static String deviceName = Build.MANUFACTURER + ' ' + Build.MODEL;

    /* The following object will store the user's email address */

    static String email = "unknown";

    /* The following object will store the user's phone number */

    static String phone = "unknown";

    /* The following object will store the user's first name */

    static String first_name = "unknown";

    /* The following object will store the user's last name */

    static String last_name = "unknown";

    /* The following object will store the user's chosen venue */

    static String venue = "unknown";

    /* The following object will store the user's event's booking date */

    static Date booking_date = new Date();

    /* The following object will store the user's event's starting date and time */

    static Date start_time = new Date();

    /* The following object will store the user's event's ending date and time */

    static Date end_time = new Date();

    /* The following method will check for internet access and return a boolean value:
     * true: internet access available
     * false: internet access unavailable */

    static boolean isNetworkAvailable(Context context)
    {

        final ConnectivityManager connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();

    }

    /* The following method will check for root access and return a boolean value:
     * true: root access available
     * false: root access unavailable */

    static boolean isDeviceRooted(Context context)
    {

        return new RootBeer(context).isRooted();

    }

    /* The following method will check if the app is being launched for the first time
     * since installation and return a boolean value:
     * true: app has been launched for the first time since installation
     * false: app has been launched earlier after installation and this is not the first time*/

    static boolean isAppLaunchFirst(Context context)
    {

        SharedPreferences sharedPreferences = context.getSharedPreferences("space.hyperr.quickbook_preferences", MODE_PRIVATE);
        return sharedPreferences.getBoolean("app_launch_first", true);

    }

    /* The following method will set the value of 'APP_LAUNCH_FIRST' in the app's shared preferences
     * to false to ensure that the introduction screen is not displayed more than once */

    static void setAppLaunchFirst(Context context)
    {

        SharedPreferences sharedPreferences = context.getSharedPreferences("space.hyperr.quickbook_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("app_launch_first", false);
        editor.apply();

    }

    /* The following method will check if an email address is valid and return a boolean value:
     * true: email address is valid
     * false: email address is not valid */

    static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /* The following method will format the date and time */

    static String generateTimeString(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String dateString;
        if(calendar.get(Calendar.HOUR_OF_DAY) < 10) dateString = "0" + calendar.get(Calendar.HOUR_OF_DAY);
        else dateString = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        if(calendar.get(Calendar.MINUTE) < 10) dateString += ":0" + calendar.get(Calendar.MINUTE);
        else dateString += ":" + calendar.get(Calendar.MINUTE);
        return dateString;
    }

}
