package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts;

import org.apache.commons.math3.linear.RealVector;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.AbstractCanalModel;
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Operator inwersji
 * Data utworzenia: 09.01.16, 11:37
 *
 * @author Michał Toporowski
 */
public class Inversion implements EvolutionaryOperator<AbstractCanalModel> {
    private final Probability probability;

    public Inversion(Probability probability) {
        this.probability = probability;
    }

    @Override
    public List<AbstractCanalModel> apply(List<AbstractCanalModel> selectedCandidates, Random rng) {
        return selectedCandidates.parallelStream()
                .map(c -> c.createNew(process(c.distances().distances(), rng)))
                .collect(Collectors.toList());
    }

    private RealVector process(RealVector input, Random rng) {
        if (probability.nextEvent(rng)) {
            return MatrixOps.invert(input);
        } else {
            return input;
        }
    }
}
