package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts;

import org.apache.commons.math3.linear.RealVector;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder;
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.MeasurementParams;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.AbstractCanalModel;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.ShortbreakLineModel;
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps;

import java.util.Random;

/**
 * Fabryka osobników typu Shortbreak
 * Data utworzenia: 26.02.16, 10:18
 *
 * @author Michał Toporowski
 */
public class ShortbreakCandidateFactory extends AbstractCandidateFactory<AbstractCanalModel> {
    private final MeasurementParams measurementParams = DataHolder.getCurrent().measurementParams();
    private final int breakCount;

    public ShortbreakCandidateFactory(int breakCount) {
        this.breakCount = breakCount;
    }


    @Override
    public AbstractCanalModel generateRandomCandidate(Random rng) {
        RealVector lengths = MatrixOps.randomRealVector(rng, breakCount);
        RealVector widths = MatrixOps.randomRealVector(rng, breakCount);
        // TODO:: przeskalować to
        return new ShortbreakLineModel(lengths, widths, measurementParams.getMicrostripParams());
    }
}
