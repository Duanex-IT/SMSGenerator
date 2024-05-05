package com.usb.sms.generator.utils;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Sergey on 22.07.2015.
 */
public class CurrencyUtilsTest {
    @Test
    public void testMinus(){
        Double d1 = new Double(25.58d);
        Double d2 = new Double(20.21d);
        Double result = CurrencyUtils.minus(d1, d2);
        assertNotNull(result);
        assertTrue("Invalid result: " + result, result.doubleValue()==5.37d);
    }

    @Test
    public void testPercent(){
        Double percent = new BigDecimal("0.215").doubleValue(); // 21,5%
        Double from = new Double(87.14d);
        Double percentCalculated = CurrencyUtils.percent(from, percent);
        assertNotNull(percentCalculated);
        assertTrue("Invalid percent calculated: " + percentCalculated, percentCalculated.doubleValue() == 18.74d);
    }

    @Test
    public void testMinusTax(){
        Double percent = new BigDecimal("0.215").doubleValue(); // 21,5%
        Double from = new Double(273.14d);
        Double withoutTax = CurrencyUtils.minusTax(from, percent);
        Double percentSum = CurrencyUtils.percent(from, percent);
        Double allSum = CurrencyUtils.plus(withoutTax, percentSum);
        assertNotNull(withoutTax);
        assertTrue("Invalid amount calculated: " + withoutTax, withoutTax.doubleValue() == 214.41d);
        assertTrue("Invalid allsum calculated: " + allSum, allSum.equals(from));
        from = new Double(0d);
        withoutTax = CurrencyUtils.minusTax(from, percent);
        assertTrue("Invalid sum from zero calculated: " + withoutTax, withoutTax.doubleValue()==0d);
    }
}
