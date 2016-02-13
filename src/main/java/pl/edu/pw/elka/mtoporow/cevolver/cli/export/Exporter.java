package pl.edu.pw.elka.mtoporow.cevolver.cli.export;

import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

/**
 * Klasa eksportująca kolekcje do plików
 * Data utworzenia: 07.02.16, 20:07
 *
 * @author Michał Toporowski
 */
public class Exporter implements Closeable {
    private final FileWriter writer;

    private Exporter(final File file) throws IOException {
        this.writer = new FileWriter(file);
    }

    /**
     * Dodaje linię do pliku
     *
     * @param line linię
     * @throws IOException
     */
    private void appendLine(final String line) throws IOException {
        writer.append(line).append("\n");
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
        try (Exporter exporter = new Exporter(file)) {
            for (Object obj : c) {
                exporter.appendLine(obj.toString());
            }
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
        try (Exporter exporter = new Exporter(file)) {
            for (double[] row : matrix) {
                exporter.appendLine(MatrixOps.mkString(row, ";"));
            }
        }
    }
}
