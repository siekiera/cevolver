package pl.edu.pw.elka.mtoporow.cevolver.lib.model.alt;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.ArrayFieldVector;
import org.apache.commons.math3.linear.RealVector;
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.MeasurementParams;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.AbstractCanalModel;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.CanalResponse;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.Distances;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.MicrostripParams;

/**
 * Klasa X
 * Data utworzenia: 29.01.16, 15:56
 *
 * @author Michał Toporowski
 */
public class MicrostripLineModelAlt extends AbstractCanalModel {

    private final Distances dists;
    private final MicrostripParams pars = MeasurementParams.getMicrostripParams();
    private final RealVector frequencies = MeasurementParams.getFrequencies();

    public MicrostripLineModelAlt(Distances distances) {
        this.dists = distances;
    }

    @Override
    public Distances distances() {
        return dists;
    }

    @Override
    public CanalResponse response() {
        Complex[] respValues = new Complex[frequencies.getDimension()];
        for (int i = 0; i < frequencies.getDimension(); i++) {
            respValues[i] = calcResponse(frequencies.getEntry(i));
        }

        return new CanalResponse(new ArrayFieldVector<>(respValues));
    }

    private Complex calcResponse(double freq) {
        TMatrixAlt tMatrixAlt = TMatrixAlt.identity();
        boolean thick = false;
        for (double dist : dists.distances().toArray()) {
            MicrostripAlt microstripAlt = new MicrostripAlt(thick ? pars.biggerW() : pars.w(), pars.t(), dist, pars.h(), pars.epsr());
            Complex z01 = MeasurementParams.getImpedance();
//            Complex z01 = Complex.valueOf(microstripAlt.getZ0());
            TMatrixAlt matrixAlt = microstripAlt.getTMatrix(z01, freq);
            tMatrixAlt = tMatrixAlt.multiply(matrixAlt);
            thick = !thick;
        }
        MicrostripAlt lastM = new MicrostripAlt(thick ? pars.biggerW() : pars.w(), pars.t(), dists.last(), pars.h(), pars.epsr());
        TMatrixAlt matrixAlt = lastM.getTMatrix(MeasurementParams.getImpedance(), freq);
        tMatrixAlt = tMatrixAlt.multiply(matrixAlt);
        return tMatrixAlt.getS11();
    }

    @Override
    public AbstractCanalModel createNew(Distances distances) {
        return new MicrostripLineModelAlt(dists);
    }
}
