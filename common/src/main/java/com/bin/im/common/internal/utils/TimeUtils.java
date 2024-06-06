package com.bin.im.common.internal.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class TimeUtils {




    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");



    public static String formatSecond(long thatTime) {

        LocalDate localDate = LocalDate.ofInstant(Instant.ofEpochSecond(thatTime), ZoneId.systemDefault());

        return localDate.format(formatter);
    }

    public static long parseSecond(String day) {
        LocalDate ld = LocalDate.parse(day, formatter);

       return ld.atStartOfDay(ZoneOffset.of("+8")).toInstant().getEpochSecond();
    }

    public static void main(String[] args) {
        long epochSeconds = TimeUnit.MILLISECONDS.toSeconds(1463673600000L);
        System.out.println(epochSeconds);

        String s = formatSecond(epochSeconds);
        System.out.println(s);

        long l = parseSecond(s);
        System.out.println(l);
    }




    public static long costTimeMillis(long start) {
       return System.currentTimeMillis() - start;
    }

    public static String  nowYearMonthLast() {
        YearMonth y2 = YearMonth.now().minusMonths(1);
        return y2.toString().replace("-","");
    }

    public static int timestampGapDay(long timestamp, long timestamp2) {

        return (int) (timestamp / (24 * 60 * 60) - timestamp2 / (24 * 60 * 60));
    }

    public static long currTimeMillis() {
        return System.currentTimeMillis();
    }


    public static long currTime() {
        return System.currentTimeMillis()/1000;
    }


    public static void sleeps(long secsonds) {
        try {
            Thread.sleep(secsonds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepMillis(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
