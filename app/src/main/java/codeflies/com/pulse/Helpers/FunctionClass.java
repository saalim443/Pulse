package codeflies.com.pulse.Helpers;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

    public static String getRole(String role){
        String finalRole="";

        if(role.equalsIgnoreCase("admin")){
            finalRole="Admin";
        }else if(role.equalsIgnoreCase("hr_manager")){
            finalRole="HR Manager";
        }else if(role.equalsIgnoreCase("eme")){
            finalRole="Email Marketing Executive";
        }else if(role.equalsIgnoreCase("bde")){
            finalRole="Business Development Exicutive";
        }else if(role.equalsIgnoreCase("employee")){
            finalRole="Employee";
        }else if(role.equalsIgnoreCase("executive")){
            finalRole="Executive";
        }else if(role.equalsIgnoreCase("publisher")){
            finalRole="Publisher";
        }else if(role.equalsIgnoreCase("contributor")){
            finalRole="Contributor";
        }else if(role.equalsIgnoreCase("viewer")){
            finalRole="Viewer";
        }else if(role.equalsIgnoreCase("accounts")){
            finalRole="Accounts";
        }else if(role.equalsIgnoreCase("recruiter")){
            finalRole="Recruiter";
        }

        return finalRole;
    }


    public static SpannableStringBuilder makeColoredText(String coloredWord, String originalText,int color){
        SpannableStringBuilder spannableBuilder = new SpannableStringBuilder(originalText);

        int startIndex = originalText.indexOf(coloredWord);
        int endIndex = startIndex + coloredWord.length();

        if (startIndex != -1) {
            spannableBuilder.setSpan(
                    new ForegroundColorSpan(color),
                    startIndex,
                    endIndex,
                    SpannableStringBuilder.SPAN_INCLUSIVE_INCLUSIVE
            );
        }

        return spannableBuilder;
    }


    public static List<String> getDatesBetween(String startDateString, String endDateString) {
        List<String> datesList = new ArrayList<>();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = dateFormat.parse(startDateString);
            Date endDate = dateFormat.parse(endDateString);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            while (calendar.getTime().before(endDate) || calendar.getTime().equals(endDate)) {
                datesList.add(new SimpleDateFormat("EEE, dd-MMM-yy").format(calendar.getTime()));
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datesList;
    }


}
