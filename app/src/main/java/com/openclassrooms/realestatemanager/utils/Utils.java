package com.openclassrooms.realestatemanager.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.wifi.WifiManager;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.openclassrooms.realestatemanager.utils.Constants.Currencies.EURO;
import static com.openclassrooms.realestatemanager.utils.Constants.PrefesKeys.CURRENCY_KEY;
import static com.openclassrooms.realestatemanager.utils.Constants.PrefesKeys.PREFS_KEY;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param dollars
     * @return
     */
    public static int convertDollarToEuro(int dollars) {
        return (int) Math.round(dollars * 0.812);
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
    public static Boolean isInternetAvailable(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifi.isWifiEnabled();
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
            ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName(adressString, 1);
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
}
