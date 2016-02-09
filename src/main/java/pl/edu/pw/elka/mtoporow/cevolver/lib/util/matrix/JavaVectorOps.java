package pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix;

import org.apache.commons.math3.linear.RealVector;

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

}
