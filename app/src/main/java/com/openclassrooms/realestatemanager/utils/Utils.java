package com.openclassrooms.realestatemanager.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.wifi.WifiManager;
import android.os.StrictMode;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(new Date());
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
        return DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
    }

    public static Bitmap bitmapFromUrl(String urlString) {
        allowNetworkCallsOnMainThread();
        Bitmap image = null;
        try {
            URL url = new URL(urlString);
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            System.out.println(e);
        }
        return image;
    }

    public static void allowNetworkCallsOnMainThread() {
        StrictMode.ThreadPolicy threadPolicy;
        threadPolicy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(threadPolicy);
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

}
