package pl.edu.pw.elka.mtoporow.cevolver.lib.model.alt;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Arrays;

/**
 * Klasa X
 * Data utworzenia: 29.01.16, 16:38
 *
 * @author Michał Toporowski
 */
public class TMatrixAlt {
    private final FieldMatrix<Complex> matrix;

    public TMatrixAlt(Complex t11, Complex t12, Complex t21, Complex t22) {
        this(new Array2DRowFieldMatrix<>(new Complex[][]{{t11, t12}, {t21, t22}}));
    }

    private TMatrixAlt(FieldMatrix<Complex> matrix) {
        this.matrix = matrix;
    }

    public TMatrixAlt multiply(TMatrixAlt other) {
        return new TMatrixAlt(this.matrix.multiply(other.matrix));
    }

    public Complex getS11() {
        return matrix.getEntry(0, 0).divide(matrix.getEntry(1, 1));
    }

    public static TMatrixAlt identity() {
        return new TMatrixAlt(Complex.ONE, Complex.ZERO, Complex.ZERO, Complex.ONE);
    }
}
