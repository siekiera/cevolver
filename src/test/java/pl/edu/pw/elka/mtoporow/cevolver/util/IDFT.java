package pl.edu.pw.elka.mtoporow.cevolver.util;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexField;
import org.apache.commons.math3.linear.ArrayFieldVector;
import org.apache.commons.math3.linear.FieldVector;

/**
 * Odwrotna dyskretna transformata Fouriera
 * Data utworzenia: 20.05.16, 11:18
 *
 * @author Michał Toporowski
 */
public class IDFT {

    /**
     * Transformuje wektor odwrotną dyskretną transformatą Fouriera
     *
     * @param input wektor liczb zespolonych
     * @return wynik transformacji
     */
    public FieldVector<Complex> transform(FieldVector<Complex> input) {
        int len = input.getDimension();
        FieldVector<Complex> output = new ArrayFieldVector<>(ComplexField.getInstance(), len);
        for (int n = 0; n < len; n++) {
            Complex xn = Complex.ZERO;
            for (int k = 0; k < len; k++) {
                double arg = 2 * Math.PI * k * n / len;
                Complex multiplier = Complex.valueOf(Math.cos(arg)).add(Complex.I.multiply(Math.sin(arg)));
                xn = xn.add(input.getEntry(k).multiply(multiplier));
            }
            output.setEntry(n, xn);
        }
        return output;
    }
}
