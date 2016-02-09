package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts;

import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.AbstractCanalModel;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Klasa bazowa dla operatorów mutacji
 * Data utworzenia: 09.02.16, 20:03
 *
 * @author Michał Toporowski
 */
public abstract class BaseMutation implements EvolutionaryOperator<AbstractCanalModel> {
    private final Probability probability;

    protected BaseMutation(Probability probability) {
        this.probability = probability;
    }


    @Override
    public List<AbstractCanalModel> apply(List<AbstractCanalModel> selectedCandidates, Random rng) {
        return selectedCandidates.parallelStream().map(c -> mutateWithProbability(c, rng)).collect(Collectors.toList());
    }

    /**
     * Mutuje pojedynczego osobnika z prawdopodobieństwem probability
     *
     * @param candidate osobnik
     * @param rng       generator liczb pseudolosowych
     * @return zmutowany lub oryginalny osobnik
     */
    private AbstractCanalModel mutateWithProbability(AbstractCanalModel candidate, Random rng) {
        if (probability.doubleValue() > rng.nextDouble()) {
            return mutate(candidate, rng);
        } else {
            return candidate;
        }
    }

    /**
     * Mutuje pojedyńczy osobnik za pomocą funkcji Gaussa
     *
     * @param candidate osobnik
     * @param rng       generator liczb pseudolosowych
     * @return zmutowany osobnik
     */
    protected abstract AbstractCanalModel mutate(AbstractCanalModel candidate, Random rng);
}
