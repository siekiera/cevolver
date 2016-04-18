package pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.RealVector;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.AbstractCanalModel;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.CanalUtils;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.Distances;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.LWDists;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.matrix.TMatrix;

/**
 * Model z reprezentacją nieciągłości jako zmian szerokości mikropaska (dla N nieciągłości N+1 elementów)
 * Szerokości pasków o innej szerokości są uzmiennione
 * Data utworzenia: 11.03.16, 11:15
 *
 * @author Michał Toporowski
 */
public class LongbreakLineModel extends MicrostripLineModel {
    private final LWDists dists;

    LongbreakLineModel(final LWDists dists, MicrostripParams pars) {
        super(pars);
        this.dists = dists;
    }

    public LongbreakLineModel(final RealVector lengths, final RealVector widths, final MicrostripParams pars) {
        super(pars);
        this.dists = new LWDists(lengths, widths);
    }

    @Override
    protected Complex calcResponse(double freq, Complex z01) {
        // Pierwszy element - znana szerokość:
        TMatrix tMatrix = calcTMatrix(pars.w(), dists.lEntry(0), z01, freq);
        for (int i = 1; i < dists.breakCount(); i++) {
            // Pasek reprezentujący i-ty element - zmienna szerokość i długość
            TMatrix elementMat = calcTMatrix(dists.wEntry(i - 1), dists.lEntry(i), z01, freq);
            tMatrix = tMatrix.multiply(elementMat);
        }
        if ((dists.breakCount() & 1) == 0) {
            // Parzysta liczba przerwań
            // Ostatni element - zakładamy taki sam, jak pierwszy
            // jeśli impedancja dopasowana (z0 ~ z01), dł. nie wpływa na wynik
            TMatrix lastMat = calcTMatrix(pars.w(), dists.lEntry(0), z01, freq);
            tMatrix = tMatrix.multiply(lastMat);
            return tMatrix.getS11();
        } else {
            // Nieparzysta liczba przerwań - należy dodać impedancję na końcu
            // TODO:: jaka powinna być wartość tej impedancji???
            int beforeLast = dists.breakCount() - 1;
            double lastImpedance = new Microstrip(dists.wEntry(beforeLast - 1), pars.t(), dists.lEntry(beforeLast), pars.h(), freq).getZ0();
            Complex z = z01.subtract(lastImpedance);
            final Complex impS11 = CanalUtils.s11ForImpedance(z, z01);
            return tMatrix.getS11WithCascadeS11(impS11);
        }
    }

    @Override
    public Distances distances() {
        return dists;
    }

    @Override
    public RealVector lengths() {
        return dists.lengths();
    }

    @Override
    public AbstractCanalModel createNew(Distances distances) {
        return new LongbreakLineModel(LWDists.longbreak(distances), pars).withTempVector(algorithmTempVector());
    }
}
