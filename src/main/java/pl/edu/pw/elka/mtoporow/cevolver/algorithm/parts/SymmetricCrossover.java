package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts;

import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Pair;
import org.uncommons.maths.random.Probability;

import java.util.Random;

/**
 * Klasa bazowa dla krzyżowań produkujących oboje dzieci w ten sam sposób
 * Data utworzenia: 16.02.16, 16:58
 *
 * @author Michał Toporowski
 */
abstract class SymmetricCrossover extends BaseCrossover {


    public SymmetricCrossover(Probability crossoverProbability) {
        super(crossoverProbability);
    }

    @Override
    protected Pair<RealVector, RealVector> mate(RealVector parent1, RealVector parent2, int numberOfCrossoverPoints, Random rng) {
        RealVector child1 = mateOne(parent1, parent2, numberOfCrossoverPoints, rng);
        RealVector child2 = mateOne(parent1, parent2, numberOfCrossoverPoints, rng);
        return new Pair<>(child1, child2);
    }

    /**
     * Krzyżuje ze sobą dwoje rodziców
     *
     * @param parent1                 rodzic 1
     * @param parent2                 rodzic 2
     * @param numberOfCrossoverPoints liczba p. krzyżowania
     * @param rng                     generator liczb losowych
     * @return osobnik dziecka
     */
    protected abstract RealVector mateOne(RealVector parent1, RealVector parent2, int numberOfCrossoverPoints, Random rng);
}
