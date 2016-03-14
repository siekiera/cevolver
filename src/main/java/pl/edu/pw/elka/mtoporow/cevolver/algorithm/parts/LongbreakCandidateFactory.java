package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts;

import org.apache.commons.math3.linear.RealVector;
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder;
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.MeasurementParams;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.AbstractCanalModel;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.LongbreakLineModel;
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.JavaVectorOps;

import java.util.Random;

/**
 * Fabryka osobników będących modelami typu longbreak ze zmienną szerokością
 * Data utworzenia: 11.03.16, 14:48
 *
 * @author Michał Toporowski
 */
public class LongbreakCandidateFactory extends BaseCandidateFactory {
    private final MeasurementParams measurementParams = DataHolder.getCurrent().measurementParams();
    private final int breakCount;

    public LongbreakCandidateFactory(int breakCount) {
        this.breakCount = breakCount;
    }

    @Override
    public int traitCount() {
        // N długości + N-1 szerokości
        return 2 * breakCount - 1;
    }

    @Override
    public AbstractCanalModel generateRandomCandidate(Random rng) {
        // Losujemy odległości z [0, totalLength]
        // TODO:: może powinniśmy to jakoś uzmiennić? Jako parametr algorytmu?
        RealVector lengths = JavaVectorOps.randomRealVector(rng, breakCount, 0, measurementParams.getTotalLength());
        /*
        Założenie: szerokości pasków w zakresie (0, 3w]
         */
        RealVector widths = JavaVectorOps.randomRealVector(rng, breakCount - 1, 0, 3 * measurementParams.getMicrostripParams().w());
        return new LongbreakLineModel(lengths, widths, measurementParams.getMicrostripParams());
    }
}
