package com.runehou.cleanoutloud;

import android.content.res.Resources;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Class which has Utility methods
 *
 */
public class Utility {
    private static Pattern pattern;
    private static Matcher matcher;
    //Email Pattern
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Validate Email with regular expression
     *Is not used, but we might want to use emails later on
     *
     * @param email
     * @return true for Valid Email and false for Invalid Email
     */
    public static boolean validate(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    /**
     * Checks for Null String object
     *
     * @param txt the text to be checked
     * @return true for not null and false for null String object
     */
    public static boolean isNotNull(String txt){
        return txt!=null && txt.trim().length()>0;
    }

    public static String dateFormat(String dateString, String error) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date;
        try {
            date = format.parse(dateString);
            return new SimpleDateFormat("dd MMM HH:mm").format(date);
        } catch (ParseException e) {
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date date1;
            try {
                date1 = format2.parse(dateString);
                return new SimpleDateFormat("dd MMM HH:mm").format(date1);
            } catch (ParseException e1) {
                e1.printStackTrace();
                return error;
            }
        }
    }
}
