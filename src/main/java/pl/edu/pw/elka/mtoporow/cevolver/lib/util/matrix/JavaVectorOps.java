package pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Zawiera operacje na macierzach i wektorach
 * Data utworzenia: 09.02.16, 18:46
 *
 * @author Michał Toporowski
 */
public final class JavaVectorOps {
    private JavaVectorOps() {

    }

    /**
     * Zwraca iterowalną kolekcję elementów wektora
     *
     * @param v wektor
     * @return kolekcja implementująca Iterable
     */
    public static Iterable<Double> asIterable(RealVector v) {
        return () -> asStream(v).iterator();
    }

    /**
     * Zwraca strumień liczb wektora
     *
     * @param v wektor
     * @return strumień prymitywów double
     */
    public static DoubleStream asStream(RealVector v) {
        return IntStream.range(0, v.getDimension()).mapToDouble(v::getEntry);
    }

    /**
     * Zwraca strumień liczb wektora
     *
     * @param v wektor
     * @return strumień obiektów typu Double
     */
    public static Stream<Double> asObjectStream(RealVector v) {
        return IntStream.range(0, v.getDimension()).mapToObj(v::getEntry);
    }

    /**
     * Tworzy macierz na podstawie tablicy przycinając wiersze do prostokąta, jeśli potrzeba
     *
     * @param array tablica 2W
     * @return macierz
     */
    public static RealMatrix createMatrix(final double[][] array) {
        int columnDimension = Arrays.stream(array).mapToInt(row -> row.length).min().getAsInt();
        RealMatrix result = MatrixUtils.createRealMatrix(array.length, columnDimension);
        for (int y = 0; y < array.length; y++) {
            result.setRow(y, Arrays.copyOf(array[y], columnDimension));
        }
        return result;
    }

    /**
     * Liczy średnią po kolumnach macierzy
     *
     * @param matrix macierz
     * @return lista zawierająca wartość średnią dla każdej kolumny
     */
    public static List<Double> avg(final RealMatrix matrix) {
        List<Double> result = new ArrayList<>(matrix.getColumnDimension());
        for (int x = 0; x < matrix.getColumnDimension(); x++) {
            result.add(Arrays.stream(matrix.getColumn(x)).average().getAsDouble());
        }
        return result;
    }

    /**
     * Konwertuje listę obiektów Double do tablicy double'i
     *
     * @param list lista
     * @return tablica
     */
    public static double[] toDoubleArray(final List<Double> list) {
        return list.stream().mapToDouble(x -> x).toArray();
    }

    /**
     * Tworzy losowy wektor liczb rzeczywistych z przedziału [min, bound)
     *
     * @param rng    generator
     * @param length długość wektora
     * @param min    wartość minimalna
     * @param bound  granica górna
     * @return wektor
     */
    public static RealVector randomRealVector(final Random rng, final int length, final double min, final double bound) {
        return MatrixUtils.createRealVector(rng.doubles(length, min, bound).toArray());
    }
}
