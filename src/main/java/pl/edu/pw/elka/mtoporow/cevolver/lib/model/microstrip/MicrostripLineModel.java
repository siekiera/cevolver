package pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.ArrayFieldVector;
import org.apache.commons.math3.linear.RealVector;
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.MeasurementParams;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.AbstractCanalModel;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.CanalResponse;

/**
 * Model linii mikropaskowej
 * Data utworzenia: 29.01.16, 15:56
 *
 * @author Michał Toporowski
 */
public abstract class MicrostripLineModel extends AbstractCanalModel {

    protected final MicrostripParams pars;

    public MicrostripLineModel(MicrostripParams pars) {
        this.pars = pars;
    }

    @Override
    public CanalResponse calculateResponse(MeasurementParams measurementParams) {
        RealVector frequencies = measurementParams.getFrequencies();
        Complex impedance = measurementParams.getImpedance();
        Complex[] respValues = new Complex[frequencies.getDimension()];
        for (int i = 0; i < frequencies.getDimension(); i++) {
            respValues[i] = calcResponse(frequencies.getEntry(i), impedance);
        }

        return new CanalResponse(new ArrayFieldVector<>(respValues));
    }

    /**
     * Oblicza odpowiedź dla danej częstotliwości
     *
     * @param freq częstotliwość (Hz)
     * @param z01  impedancja
     * @return odpowiedź (S11) jako liczba zespolona
     */
    protected abstract Complex calcResponse(double freq, Complex z01);
}
