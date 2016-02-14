package pl.edu.pw.elka.mtoporow.cevolver.lib.model.alt;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.ArrayFieldVector;
import org.apache.commons.math3.linear.RealVector;
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.MeasurementParams;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.AbstractCanalModel;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.CanalResponse;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.Distances;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.MicrostripParams;
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.JavaVectorOps;

/**
 * Model linii mikropaskowej - alternatywna implementacja
 * Data utworzenia: 29.01.16, 15:56
 *
 * @author Michał Toporowski
 */
public class MicrostripLineModelAlt extends AbstractCanalModel {

    private final Distances dists;
    private final MicrostripParams pars;

    public MicrostripLineModelAlt(Distances distances, MicrostripParams pars) {
        this.dists = distances;
        this.pars = pars;
    }

    @Override
    public Distances distances() {
        return dists;
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
    private Complex calcResponse(double freq, Complex z01) {
        TMatrixAlt tMatrixAlt = TMatrixAlt.identity();
        boolean thick = false;
        for (double dist : JavaVectorOps.asIterable(dists.distances())) {
            MicrostripAlt microstripAlt = new MicrostripAlt(thick ? pars.biggerW() : pars.w(), pars.t(), dist, pars.h(), pars.epsr());
            TMatrixAlt matrixAlt = microstripAlt.getTMatrix(z01, freq);
            tMatrixAlt = tMatrixAlt.multiply(matrixAlt);
            thick = !thick;
        }
        MicrostripAlt lastM = new MicrostripAlt(thick ? pars.biggerW() : pars.w(), pars.t(), dists.last(), pars.h(), pars.epsr());
        TMatrixAlt matrixAlt = lastM.getTMatrix(z01, freq);
        tMatrixAlt = tMatrixAlt.multiply(matrixAlt);
        return tMatrixAlt.getS11();
    }

    @Override
    public AbstractCanalModel createNew(Distances distances) {
        return new MicrostripLineModelAlt(distances, pars);
    }
}
