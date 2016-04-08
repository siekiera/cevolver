package pl.edu.pw.elka.mtoporow.cevolver.cli.export;

import com.sun.deploy.util.StringUtils;
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps;

import java.io.*;
import java.util.Collection;

/**
 * Klasa eksportująca kolekcje do plików
 * Data utworzenia: 07.02.16, 20:07
 *
 * @author Michał Toporowski
 */
public class Exporter implements Closeable {
    private static final String SEPARATOR = ";";
    private final Writer writer;

    private Exporter(final Writer writer) {
        this.writer = writer;
    }

    public static Exporter forFile(final File file) throws IOException {
        return new Exporter(new FileWriter(file));
    }

    public static Exporter systemOut() {
        return new Exporter(new OutputStreamWriter(System.out));
    }

    /**
     * Dodaje linię do pliku
     *
     * @param line linię
     * @throws IOException
     */
    private Exporter appendLine(final String line) throws IOException {
        writer.append(line).append("\n");
        return this;
    }

    public Exporter append(Collection<?> c) throws IOException {
        appendLine(StringUtils.join(c, SEPARATOR));
        return this;
    }

    public Exporter appendMatrix(final double[][] matrix) throws IOException {
        for (double[] row : matrix) {
            appendLine(MatrixOps.mkString(row, SEPARATOR));
        }
        return this;
    }

    public Exporter appendMatrix(final Object[][] matrix) throws IOException {
        for (Object[] row : matrix) {
            appendLine(MatrixOps.mkString(row));
        }
        return this;
    }

    @Override
    public void close() throws IOException {
        writer.flush();
        writer.close();
    }

    /**
     * Eksportuje kolekcję do pliku wykorzystując metodę toString
     *
     * @param file plik
     * @param c    kolekcja
     * @throws IOException
     */
    public static void serialize(File file, Collection<?> c) throws IOException {
        try (Exporter exporter = Exporter.forFile(file)) {
            exporter.append(c);
        }
    }

    /**
     * Eksportuje macierz do pliku
     *
     * @param file   plik
     * @param matrix macierz
     * @throws IOException
     */
    public static void serialize(File file, double[][] matrix) throws IOException {
        try (Exporter exporter = Exporter.forFile(file)) {
            exporter.appendMatrix(matrix);
        }
    }

}
