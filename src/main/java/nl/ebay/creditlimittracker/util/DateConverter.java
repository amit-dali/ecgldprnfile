package nl.ebay.creditlimittracker.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateConverter {
    private DateConverter() {

    }

    public static String convertDate(String dateStr, String sourceFormat, String targetFormat) throws ParseException {
        SimpleDateFormat sourceFormatter = new SimpleDateFormat(sourceFormat);
        Date date = sourceFormatter.parse(dateStr);

        SimpleDateFormat targetFormatter = new SimpleDateFormat(targetFormat);
        return targetFormatter.format(date);
    }
}
