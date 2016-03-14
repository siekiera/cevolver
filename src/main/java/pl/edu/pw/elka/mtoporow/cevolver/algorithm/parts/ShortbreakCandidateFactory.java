package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts;

import org.apache.commons.math3.linear.RealVector;
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder;
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.MeasurementParams;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.AbstractCanalModel;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.ShortbreakLineModel;
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.JavaVectorOps;

import java.util.Random;

/**
 * Fabryka osobników typu Shortbreak
 * Data utworzenia: 26.02.16, 10:18
 *
 * @author Michał Toporowski
 */
public class ShortbreakCandidateFactory extends BaseCandidateFactory {
    private final MeasurementParams measurementParams = DataHolder.getCurrent().measurementParams();
    private final int breakCount;

    public ShortbreakCandidateFactory(int breakCount) {
        this.breakCount = breakCount;
    }


    @Override
    public AbstractCanalModel generateRandomCandidate(Random rng) {
        // Losujemy odległości z [0, totalLength]
        // TODO:: może powinniśmy to jakoś uzmiennić? Jako parametr algorytmu?
        RealVector lengths = JavaVectorOps.randomRealVector(rng, breakCount, 0, measurementParams.getTotalLength());
        /*
        Założenie: szerokości miejsc nieciągłości są mniejsze od szerokości właściwego przewodu
        losujemy z przedziału [0, w)
         */
        RealVector widths = JavaVectorOps.randomRealVector(rng, breakCount, 0, measurementParams.getMicrostripParams().w());
        return new ShortbreakLineModel(lengths, widths, measurementParams.getMicrostripParams());
    }

    @Override
    public int traitCount() {
        // N długości + N szerokości
        return breakCount * 2;
    }
}
