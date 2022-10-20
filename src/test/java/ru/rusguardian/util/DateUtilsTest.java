package ru.rusguardian.util;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

class DateUtilsTest {

    @Test
    void testDate() {

        Calendar calendar = Calendar.getInstance();
        calendar.roll(Calendar.MONTH, -3);
        String formattedDate = new SimpleDateFormat("LLLL yyyy", Locale.getDefault()).format(calendar.getTime());

        System.out.println(formattedDate);
    }


}