package utl;

import java.text.DecimalFormat;

/**
 * Created by nairuz on 0002, July, 2, 2015.
 */
public class Utils {

    public static String formatNumber (int value) {
        DecimalFormat formatter = new DecimalFormat("#,####,###");
        String formatted = formatter.format(value);

        return formatted ;
    }
}
