package pl.edu.pw.elka.mtoporow.cevolver.algorithm.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Metody narzędziowe związane z czasem
 * Data utworzenia: 08.02.16, 19:31
 *
 * @author Michał Toporowski
 */
public final class TimeUtil {
    private static final DateFormat NO_SEP_FORMAT = new SimpleDateFormat("YYYYMMDDHHmmss");

    private TimeUtil() {

    }

    public static String nowAsNoSepString() {
        return NO_SEP_FORMAT.format(new Date());
    }
}
