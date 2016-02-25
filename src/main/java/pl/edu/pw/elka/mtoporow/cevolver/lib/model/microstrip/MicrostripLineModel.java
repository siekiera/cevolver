package pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.ArrayFieldVector;
import org.apache.commons.math3.linear.RealVector;
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.MeasurementParams;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.AbstractCanalModel;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.CanalResponse;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.Distances;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.matrix.TMatrix;
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.JavaVectorOps;

/**
 * Model linii mikropaskowej
 * Data utworzenia: 29.01.16, 15:56
 *
 * @author Michał Toporowski
 */
public class MicrostripLineModel extends AbstractCanalModel {

    private final Distances dists;
    private final MicrostripParams pars;

    public MicrostripLineModel(Distances distances, MicrostripParams pars) {
        this.dists = distances;
        this.pars = pars;
    }

    @Override
    public Distances distances() {
        return dists;
    }

    @Override
    public RealVector lengths() {
        // Jedyne odległości w tym modelu to długości
        return dists.distances();
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
        TMatrix tMatrix = TMatrix.identity();
        boolean thick = false;
        for (double dist : JavaVectorOps.asIterable(dists.distances())) {
            Microstrip microstrip = new Microstrip(thick ? pars.biggerW() : pars.w(), pars.t(), dist, pars.h(), pars.epsr());
            TMatrix matrixAlt = microstrip.getTMatrix(z01, freq);
            tMatrix = tMatrix.multiply(matrixAlt);
            thick = !thick;
        }
        Microstrip lastM = new Microstrip(thick ? pars.biggerW() : pars.w(), pars.t(), dists.last(), pars.h(), pars.epsr());
        TMatrix matrixAlt = lastM.getTMatrix(z01, freq);
        tMatrix = tMatrix.multiply(matrixAlt);
        return tMatrix.getS11();
    }

    @Override
    public AbstractCanalModel createNew(Distances distances) {
        return new MicrostripLineModel(distances, pars);
    }
}
