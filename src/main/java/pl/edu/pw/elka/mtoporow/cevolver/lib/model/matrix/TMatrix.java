package pl.edu.pw.elka.mtoporow.cevolver.lib.model.matrix;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.linear.FieldMatrix;

/**
 * Implementacja macierzy T
 * Data utworzenia: 29.01.16, 16:38
 *
 * @author Michał Toporowski
 */
public class TMatrix {
    private final FieldMatrix<Complex> matrix;

    public TMatrix(Complex t11, Complex t12, Complex t21, Complex t22) {
        this(new Array2DRowFieldMatrix<>(new Complex[][]{{t11, t12}, {t21, t22}}));
    }

    private TMatrix(FieldMatrix<Complex> matrix) {
        this.matrix = matrix;
    }

    /**
     * Mnoży tę macierz przez inną
     *
     * @param other inna macierz T
     * @return macierz wynikowa tj. macierz reprezentująca połączenie kaskadowe czwórników
     */
    public TMatrix multiply(TMatrix other) {
        return new TMatrix(this.matrix.multiply(other.matrix));
    }

    /**
     * Zwraca element S11 macierzy reprezentującej połączenie kaskadowe tej macierzy i macierzy reprezentowanej
     * jedynie przez element s11'
     *
     * @param s11 s11'
     * @return element s11 wynikowej macierzy
     */
    public Complex getS11WithCascadeS11(final Complex s11) {
        // TODO:: czy to dobrze?
        // s11m = (t11 * s11' + t12) / (t21 + s11' + t22)
        return matrix.getEntry(0, 0).multiply(s11).add(matrix.getEntry(0, 1))
                .divide(matrix.getEntry(1, 0).multiply(s11).add(matrix.getEntry(1, 1)));
    }

    /**
     * Zwraca element S11 z macierzy S przekształconej z tej macierzy T
     *
     * @return S11 = T12 / T22 jako liczba zespolona
     */
    public Complex getS11() {
        return matrix.getEntry(0, 1).divide(matrix.getEntry(1, 1));
    }

    /**
     * Zwraca macierz jednostkową
     *
     * @return jednostkowa macierz T
     */
    public static TMatrix identity() {
        return new TMatrix(Complex.ONE, Complex.ZERO, Complex.ZERO, Complex.ONE);
    }
}
