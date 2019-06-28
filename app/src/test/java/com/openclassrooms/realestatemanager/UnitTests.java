package com.openclassrooms.realestatemanager;

import org.junit.Test;

import java.util.Calendar;

import static com.openclassrooms.realestatemanager.ui.activities.LoanSimulationActivity.getInterestRate;
import static com.openclassrooms.realestatemanager.ui.activities.LoanSimulationActivity.getMonthlyPayment;
import static com.openclassrooms.realestatemanager.ui.activities.LoanSimulationActivity.getTotalPayment;
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

    @Test
    public void totalPayment_3rate_20m() {
        double interestRate = 3d;
        int amount = 20000;

        double totalPayment = getTotalPayment(interestRate, amount);

        assertEquals(20600.0d, totalPayment, 0.001);
    }

    @Test
    public void totalPayment_5rate_48m() {
        double interestRate = 5d;
        int amount = 48000;

        double totalPayment = getTotalPayment(interestRate, amount);

        assertEquals(50400.0d, totalPayment, 0.001);
    }

    @Test
    public void monthlyPayment_50400amount_18months() {
        double totalPayment = 50400;
        int months = 18;

        double monthlyPayment = getMonthlyPayment(totalPayment, months);

        assertEquals(2800.0d, monthlyPayment, 0.001);
    }

    @Test
    public void monthlyPayment_120000amount_96months() {
        double totalPayment = 120000;
        int months = 96;

        double monthlyPayment = getMonthlyPayment(totalPayment, months);

        assertEquals(1250.0d, monthlyPayment, 0.001);
    }
}