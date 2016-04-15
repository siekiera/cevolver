package pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.RealVector;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.AbstractCanalModel;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.Distances;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.matrix.TMatrix;
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.JavaVectorOps;

/**
 * Model linii mikropaskowej z podanymi z góry szerokościami
 * Data utworzenia: 25.02.16, 13:24
 *
 * @author Michał Toporowski
 */
public class FixedWidthLineModel extends MicrostripLineModel {
    private Distances dists;

    public FixedWidthLineModel(Distances distances, MicrostripParams pars) {
        super(pars);
        this.dists = distances;
    }

    @Override
    public RealVector lengths() {
        // Jedyne odległości w tym modelu to długości
        return dists.distances();
    }

    @Override
    public Distances distances() {
        return dists;
    }

    /**
     * Oblicza odpowiedź dla danej częstotliwości
     *
     * @param freq częstotliwość (Hz)
     * @param z01  impedancja
     * @return odpowiedź (S11) jako liczba zespolona
     */
    @Override
    protected Complex calcResponse(double freq, Complex z01) {
        TMatrix tMatrix = TMatrix.identity();
        boolean thick = false;
        for (double dist : JavaVectorOps.asIterable(dists.distances())) {
            Microstrip microstrip = new Microstrip(thick ? pars.biggerW() : pars.w(), pars.t(), dist, pars.h(), pars.epsr());
            TMatrix matrixAlt = microstrip.getTMatrix(z01, freq);
            tMatrix = tMatrix.multiply(matrixAlt);
            thick = !thick;
        }
        // Ostatni element - zakładamy taki sam, jak pierwszy
        // jeśli impedancja dopasowana (z0 ~ z01), dł. nie wpływa na wynik
        Microstrip lastM = new Microstrip(thick ? pars.biggerW() : pars.w(), pars.t(), dists.distances().getEntry(0), pars.h(), pars.epsr());
        TMatrix matrixAlt = lastM.getTMatrix(z01, freq);
        tMatrix = tMatrix.multiply(matrixAlt);
        return tMatrix.getS11();
    }

    @Override
    public AbstractCanalModel createNew(Distances distances) {
        return new FixedWidthLineModel(distances, pars).withTempVector(algorithmTempVector());
    }
}
