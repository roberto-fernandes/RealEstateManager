package com.openclassrooms.realestatemanager.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.utils.Constants.NotificationsChannels;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.openclassrooms.realestatemanager.utils.Constants.Currencies.EURO;
import static com.openclassrooms.realestatemanager.utils.Constants.InternetType.*;
import static com.openclassrooms.realestatemanager.utils.Constants.InternetType.INTERNET_3G;
import static com.openclassrooms.realestatemanager.utils.Constants.InternetType.INTERNET_NONE;
import static com.openclassrooms.realestatemanager.utils.Constants.PrefesKeys.CURRENCY_KEY;
import static com.openclassrooms.realestatemanager.utils.Constants.PrefesKeys.PREFS_KEY;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    private static final double currencyChangeRatio = 0.812D;

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param dollars
     * @return
     */
    public static int convertDollarToEuro(int dollars) {
        return (int) Math.round(dollars * currencyChangeRatio);
    }

    public static int convertEuroToDollar(int dollars) {
        return (int) Math.round(dollars / currencyChangeRatio);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @return
     */
    public static String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        return formatDate(calendar);
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param context
     * @return
     */
    public static boolean isInternetAvailable(Context context) {
        return internetType(context) == INTERNET_3G || internetType(context) == INTERNET_WIFI;
    }

    public static int internetType(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network activeNetwork = connectivityManager.getActiveNetwork();
                NetworkCapabilities networkCapabilities = connectivityManager
                        .getNetworkCapabilities(activeNetwork);
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return INTERNET_WIFI;
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return INTERNET_3G;
                }
            } else {
                int type = connectivityManager.getActiveNetworkInfo().getType();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    //is connected to wifi
                    return INTERNET_WIFI;
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    //is connected to 3G
                    return INTERNET_3G;
                }
            }
            return INTERNET_NONE;
        } catch (Exception e) {
            return INTERNET_NONE;
        }
    }

    public static void createNotification(Context context, String title, String message) {
        createNotification(context, title, message, null);
    }

    public static void createNotification(
            Context context,
            String title,
            String message,
            String channelId
    ) {
        if (channelId == null) {
            channelId = NotificationsChannels.DEFAULT_CHANNEL_ID;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_playlist_add_check_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(1, builder.build());
    }

    @SuppressLint("DefaultLocale")
    public static String formatDoubleToString(Double value) {
        return String.format("%,.2f", value);
    }

    public static Calendar unixToCalendar(long unixTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(unixTime);
        return calendar;
    }

    public static String formatDate(long unixTime) {
        return formatDate(unixToCalendar(unixTime));
    }

    public static String formatDate(Calendar calendar) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(calendar.getTime());
    }

    public static void bitmapsFromUrl(
            final List<String> urlList,
            final OnReceivingBitmapFromUrl onReceivingBitmapFromUrl,
            final Activity activity
    ) {

        Thread getBitmapsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap image;
                final List<Bitmap> bitmaps = new ArrayList<>();
                try {
                    for (String urlString : urlList) {
                        URL url = new URL(urlString);
                        image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        bitmaps.add(image);
                    }
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            onReceivingBitmapFromUrl.onSucess(bitmaps);
                        }
                    });
                } catch (final IOException e) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            onReceivingBitmapFromUrl.onFailure(e);
                        }
                    });
                }
            }
        });
        getBitmapsThread.start();
    }

    public static Address getAddressClassFromString(String adressString, Context context) {
        Geocoder coder = new Geocoder(context);
        Address address = null;
        try {
            ArrayList<Address> adresses = (ArrayList<Address>)
                    coder.getFromLocationName(adressString, 1);
            address = adresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    public interface OnReceivingBitmapFromUrl {
        void onSucess(List<Bitmap> bitmaps);

        void onFailure(Exception e);
    }

    private static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
    }

    public static void storeCurrency(Context context, String value) {
        storeDataFromPrefs(context, value, CURRENCY_KEY);
    }

    public static String getCurrency(Context context) {
        return getDataFromPrefs(context, EURO, CURRENCY_KEY);
    }

    private static void storeDataFromPrefs(Context context, String value, String key) {
        getSharedPreference(context).edit().putString(key, value).commit();
    }

    private static String getDataFromPrefs(Context context, String DefaultValue, String key) {
        return getSharedPreference(context).getString(key, DefaultValue);
    }

    public static void setWifiEnabled(Context context, boolean enabled) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(enabled);
    }
}
