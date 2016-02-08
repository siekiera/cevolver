package pl.edu.pw.elka.mtoporow.cevolver.algorithm.util;

import java.io.*;
import java.util.Properties;

/**
 * Klasa narzędziowa do operacji na plikach properties
 * Data utworzenia: 08.02.16, 19:13
 *
 * @author Michał Toporowski
 */
public final class PropertiesUtil {
    private PropertiesUtil() {

    }

    /**
     * Zapisuje properties to Stringa
     *
     * @param properties properties
     * @return łańcuch znaków
     * @throws IOException
     */
    public static String storeToString(final Properties properties) throws IOException {
        try (Writer sw = new StringWriter()) {
            properties.store(sw, null);
            return sw.toString();
        }
    }

    /**
     * Zapisuje properties to pliku
     *
     * @param properties properties
     * @param file       plik
     * @param comments   komentarze
     * @throws IOException
     */
    public static void storeToFile(final Properties properties, final File file, final String comments) throws IOException {
        try (Writer w = new FileWriter(file)) {
            properties.store(w, comments);
        }
    }
}
