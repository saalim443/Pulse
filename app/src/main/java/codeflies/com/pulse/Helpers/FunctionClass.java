package codeflies.com.pulse.Helpers;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FunctionClass {


    public static String getTwoDecial(String number) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
        return decimalFormat.format(Double.parseDouble(number));
    }


    public static String changeDate(String inputDate){

        try {
            // Parse the input date
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = inputFormat.parse(inputDate);

            // Format the date into the desired format
            SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, dd-MMM-yy", Locale.US);
            outputFormat.setTimeZone(TimeZone.getDefault()); // Adjust to the desired time zone

            String formattedDate = outputFormat.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
