package com.usb.sms.generator.utils;

import com.sun.istack.internal.NotNull;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.Locale;

public class CurrencyUtils {

    public static Double minus(@NotNull Double from, @NotNull Double what){
        BigDecimal d1 = new BigDecimal(Double.toString(from));
        BigDecimal d2 = new BigDecimal(Double.toString(what));
        BigDecimal result = d1.subtract(d2);
        BigDecimal scalled = result.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return scalled.doubleValue();
    }

    public static Double plus(@NotNull Double from, @NotNull Double what){
        BigDecimal d1 = new BigDecimal(Double.toString(from));
        BigDecimal d2 = new BigDecimal(Double.toString(what));
        BigDecimal result = d1.add(d2);
        BigDecimal scalled = result.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return scalled.doubleValue();
    }

    public static Double percent(@NotNull Double from, @NotNull Double percent){
        BigDecimal d1 = new BigDecimal(Double.toString(from));
        BigDecimal dPercent = new BigDecimal(Double.toString(percent));
        BigDecimal result = d1.multiply(dPercent);
        BigDecimal scalled = result.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return scalled.doubleValue();
    }

    public static Double minusTax(@NotNull Double from, @NotNull Double taxPercent){
        BigDecimal d1 = new BigDecimal(Double.toString(from));
        BigDecimal dPercent = new BigDecimal(Double.toString(taxPercent));
        BigDecimal tax = d1.multiply(dPercent).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal result = d1.subtract(tax).setScale(2, BigDecimal.ROUND_HALF_EVEN);;
        return result.doubleValue();
    }

    public static String convertToString(@NotNull Double value){
        Format format = DecimalFormat.getNumberInstance(Locale.forLanguageTag("uk"));
        return format.format(value);
    }
}
