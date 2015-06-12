package pl.edu.pw.elka.mtoporow.cevolver.lib.matrix;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.linear.FieldVector;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * Klasa X
 * Data utworzenia: 20.02.15
 *
 * @author Michał Toporowski
 */
@Deprecated
public class ComplexArrayMatrix extends Array2DRowFieldMatrix<Complex> implements ComplexMatrix {


    public ComplexArrayMatrix(Field<Complex> field) {
        super(field);
    }

    public ComplexArrayMatrix(Field<Complex> field, int rowDimension, int columnDimension) throws NotStrictlyPositiveException {
        super(field, rowDimension, columnDimension);
    }

    public ComplexArrayMatrix(Complex[][] d) throws DimensionMismatchException, NullArgumentException, NoDataException {
        super(d);
    }

    public ComplexArrayMatrix(Field<Complex> field, Complex[][] d) throws DimensionMismatchException, NullArgumentException, NoDataException {
        super(field, d);
    }

    public ComplexArrayMatrix(Complex[][] d, boolean copyArray) throws DimensionMismatchException, NoDataException, NullArgumentException {
        super(d, copyArray);
    }

    public ComplexArrayMatrix(Field<Complex> field, Complex[][] d, boolean copyArray) throws DimensionMismatchException, NoDataException, NullArgumentException {
        super(field, d, copyArray);
    }

    public ComplexArrayMatrix(Complex[] v) throws NoDataException {
        super(v);
    }

    public ComplexArrayMatrix(Field<Complex> field, Complex[] v) {
        super(field, v);
    }
}
