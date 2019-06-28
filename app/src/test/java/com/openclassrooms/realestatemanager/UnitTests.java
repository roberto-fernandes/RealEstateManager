package com.openclassrooms.realestatemanager;

import org.junit.Test;

import java.util.Calendar;

import static com.openclassrooms.realestatemanager.ui.activities.LoanSimulationActivity.getInterestRate;
import static com.openclassrooms.realestatemanager.utils.Utils.convertDollarToEuro;
import static com.openclassrooms.realestatemanager.utils.Utils.formatDate;
import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTests {
    @Test
    public void convertDollarToEuro_5dollars() {
        int dollars = 5;
        int euros = convertDollarToEuro(dollars);
        assertEquals(4, euros);
    }

    @Test
    public void convertDollarToEuro_2dollars() {
        int dollars = 2;
        int euros = convertDollarToEuro(dollars);
        assertEquals(2, euros);
    }

    @Test
    public void convertDollarToEuro_213213dollars() {
        int dollars = 213213;
        int euros = convertDollarToEuro(dollars);
        assertEquals(173129, euros);
    }

    @Test
    public void convertDollarToEuro_0dollars() {
        int dollars = 0;
        int euros = convertDollarToEuro(dollars);
        assertEquals(0, euros);
    }

    @Test
    public void convertDollarToEuro_negative120dollars() {
        int dollars = -120;
        int euros = convertDollarToEuro(dollars);
        assertEquals(-97, euros);
    }

    @Test
    public void formatDate_size() {
        Calendar calendar = Calendar.getInstance();
        String formattedDate = formatDate(calendar);
        int length = formattedDate.length();

        assertEquals(10, length);
    }

    @Test
    public void interestRate_78months() {
        Calendar calendar = Calendar.getInstance();
        String formattedDate = formatDate(calendar);
        int length = formattedDate.length();

        assertEquals(10, length);
    }

    @Test
    public void interestRate_10months() {
        int months = 10;
        double interestRate = getInterestRate(months);

        assertEquals(3.0d, interestRate, 0.001);
    }

    @Test
    public void interestRate_14months() {
        int months = 14;
        double interestRate = getInterestRate(months);

        assertEquals(3.0d, interestRate, 0.001);
    }

    @Test
    public void interestRate_121months() {
        int months = 121;
        double interestRate = getInterestRate(months);

        assertEquals(13.444d, interestRate, 0.001);
    }

    @Test
    public void interestRate_0months() {
        int months = 0;
        double interestRate = getInterestRate(months);

        assertEquals(0.0d, interestRate, 0.001);
    }

    /*

    private double getTotalPayment() {
        return interestRate / 100 * amount + amount;
    }

    private double getMonthlyPayment() {
        return totalPayment / months;
    }
     */
}