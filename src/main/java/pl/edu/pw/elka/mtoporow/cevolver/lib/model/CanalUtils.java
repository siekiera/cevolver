package pl.edu.pw.elka.mtoporow.cevolver.lib.model;

import org.apache.commons.math3.complex.Complex;

/**
 * Klasa narzędziowa zawierająca obliczenia parametrów kanałów
 * Data utworzenia: 21.03.16, 15:33
 *
 * @author Michał Toporowski
 */
public final class CanalUtils {
    private CanalUtils() {

    }

    /**
     * Zwraca S11 dla impedancji obciążającej wrota wejściowej
     *
     * @param z  wartość impedancji obciążającej
     * @param z0 wartość impedancji na wrotach
     * @return wartość S11
     */
    public static Complex s11ForImpedance(final Complex z, final Complex z0) {
        final Complex y = z0.divide(z);
        return Complex.ONE.subtract(y)
                .divide(Complex.ONE.add(y));
    }

}
