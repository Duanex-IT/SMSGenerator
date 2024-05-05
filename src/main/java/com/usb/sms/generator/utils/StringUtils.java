package com.usb.sms.generator.utils;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static String interpolate(String format, Map<String, ? extends Object> args) {
        String out = format;
        for (String var : getTemplateVars(format)){
            String value ="";
            if (args.containsKey(var) && args.get(var)!=null){
                Object val = args.get(var);
                if (val instanceof Double){
                    value = CurrencyUtils.convertToString((Double) val);
                } else {
                    value = args.get(var).toString();
                }
            }
            out = Pattern.compile(Pattern.quote("#{" + var + "}")).
                    matcher(out).
                    replaceAll(value);
        }
        /*
        for (String arg : args.keySet()) {
            Object value = args.get(arg);
            if (value==null){
                value = new String("");
            }
            out = Pattern.compile(Pattern.quote("#{" + args + "}")).
                    matcher(out).
                    replaceAll(args.get(arg).toString());
        }
        */
        return out;
    }

    public static List<String> getTemplateVars(@NotNull String template){
        Matcher matcher = Pattern.compile("#\\{(\\S+)\\}" ).
                matcher(template);
        List<MatchResult> matches = new ArrayList<>();
        List<String> result = new ArrayList<>();
        while(matcher.find()) {
            result.add(matcher.group(matcher.groupCount()));
        }
        return result;
    }
}
