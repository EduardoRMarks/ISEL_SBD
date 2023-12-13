package TP2;
import java.util.Locale;

public class NationalityUtils {

    public static String convertToNationalityCode(String nationalityName) {
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            if (nationalityName.equalsIgnoreCase(locale.getDisplayCountry())) {
                return locale.getCountry();
            }
        }
        return null; // Nationality code not found
    }
}
