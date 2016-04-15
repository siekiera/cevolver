package pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.RealVector;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.AbstractCanalModel;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.Distances;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.LWDists;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.matrix.TMatrix;

/**
 * Model z reprezentacją nieciągłości jako bardzo krótkich mikropasków (dla N nieciągłości 2N+1 elementów)
 * Szerokości miejsc nieciągłości są również uzmiennione
 * <p>
 * Data utworzenia: 25.02.16, 13:51
 *
 * @author Michał Toporowski
 */
public class ShortbreakLineModel extends MicrostripLineModel {
    private final LWDists dists;

    ShortbreakLineModel(final LWDists distances, final MicrostripParams pars) {
        super(pars);
        this.dists = distances;
    }

    public ShortbreakLineModel(RealVector lengths, final RealVector widths, final MicrostripParams pars) {
        super(pars);
        this.dists = new LWDists(lengths, widths);
    }


    @Override
    protected Complex calcResponse(double freq, Complex z01) {
        TMatrix tMatrix = TMatrix.identity();
        for (int i = 0; i < dists.breakCount(); i++) {
            double elementL = dists.lEntry(i);
            double breakW = dists.wEntry(i);
            // Pasek reprezentujący element
            TMatrix elementMat = calcTMatrix(pars.w(), elementL, z01, freq);
            // Pasek reprezentujący przerwę
            // TODO:: tak naprawdę to nie biggerW, tylko lesserL - zmienić, jeśli działać będzie
            TMatrix breakMat = calcTMatrix(breakW, pars.biggerW(), z01, freq);
            tMatrix = tMatrix.multiply(elementMat).multiply(breakMat);
        }
        // Ostatni element - zakładamy taki sam, jak pierwszy
        // jeśli impedancja dopasowana (z0 ~ z01), dł. nie wpływa na wynik
        TMatrix lastMat = calcTMatrix(pars.w(), dists.lEntry(0), z01, freq);
        tMatrix = tMatrix.multiply(lastMat);
        return tMatrix.getS11();
    }

    @Override
    public RealVector lengths() {
        return dists.lengths();
    }

    @Override
    public Distances distances() {
        return dists;
    }

    @Override
    public AbstractCanalModel createNew(Distances distances) {
        return new ShortbreakLineModel(LWDists.shortbreak(distances), pars).withTempVector(algorithmTempVector());
    }
}
