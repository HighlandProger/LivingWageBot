package ru.rusguardian.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateUtils {

    private DateUtils() {
    }

    public static String getRolledFormattedDate(int monthsToRoll) {
        Calendar calendar = Calendar.getInstance();
        calendar.roll(Calendar.MONTH, -monthsToRoll);
        return new SimpleDateFormat("LLLL yyyy", new Locale("ru", "RU")).format(calendar.getTime());

    }
}
