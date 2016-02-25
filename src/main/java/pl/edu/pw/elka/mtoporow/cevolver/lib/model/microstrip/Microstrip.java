package pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip;

import org.apache.commons.math3.complex.Complex;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.matrix.TMatrix;
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.maths.PhysicalConstants;

/**
 * Alternatywny model linii mikropaskowej
 * Data utworzenia: 29.01.16, 15:57
 *
 * @author Michał Toporowski
 */
public class Microstrip {
    private final double w;
    private final double t;
    private final double l;
    private final double h;
    private final double epsr;
    private double z0;
    private double eef;

    /**
     * @param w    wysokość paska (m)
     * @param l    długość paska (m)
     * @param t    wysokość paska (m)
     * @param h    wysokość dielektrycznego podłoża (m)
     * @param epsr względna przenikalność elektryczna podłoża
     */
    public Microstrip(double w, double t, double l, double h, double epsr) {
        this.w = w;
        this.t = t;
        this.l = l;
        this.h = h;
        this.epsr = epsr;
        this.calculate();
    }

    /**
     * Liczy eef oraz Z0
     */
    private void calculate() {
        double wph = w / h;
        if (wph < 1) {
            eef = (epsr + 1.) / 2. + ((epsr - 1.) / 2.) * (1. / Math.sqrt(1. + 12. / wph) + 0.041 * (1 - wph) * (1 - wph));
            z0 = (60. / Math.sqrt(eef)) * Math.log(8. / wph + 0.25 * wph);
        } else {
            eef = (epsr + 1.) / 2. + ((epsr - 1.) / 2.) * (1. / Math.sqrt(1. + 12. / wph));
            z0 = 120. * Math.PI / (Math.sqrt(eef) * (wph + 1.393 + (2. / 3.) * Math.log(wph + 1.444)));
        }
    }

    /**
     * Liczy długość elektryczną
     *
     * @param f częstotliwość (Hz)
     * @return długość elektryczna
     */
    public double getELen(double f) {
        double vphi = PhysicalConstants.LIGHT_SPEED_MPS() / Math.sqrt(eef);
        return 2. * Math.PI * l * f / vphi;
    }

    /**
     * Liczy macierz T dla tego mikropaska
     *
     * @param z01  impedancja (om)
     * @param freq częstotliwość (Hz)
     * @return Macierz T
     */
    public TMatrix getTMatrix(Complex z01, double freq) {
        double theta = getELen(freq);
        Complex r = Complex.valueOf(z0).divide(z01);
        Complex rInv = Complex.ONE.divide(r);
        Complex commonSubstrate = Complex.I.multiply(0.5).multiply(r.add(rInv)).multiply(Math.sin(theta));
        Complex t11 = Complex.valueOf(Math.cos(theta)).subtract(commonSubstrate);
        Complex t22 = Complex.valueOf(Math.cos(theta)).add(commonSubstrate);
        Complex t12 = Complex.I.multiply(0.5).multiply(r.subtract(rInv)).multiply(Math.sin(theta));
        Complex t21 = t12.negate();
        return new TMatrix(t11, t12, t21, t22);
    }


    /**
     * Zwraca impedancję charakterystyczną tego mikropaska
     *
     * @return impedancja
     */
    public double getZ0() {
        return z0;
    }


}
