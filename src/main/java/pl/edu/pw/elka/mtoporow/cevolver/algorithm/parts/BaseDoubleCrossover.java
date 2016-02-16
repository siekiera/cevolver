package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts;

import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.operators.AbstractCrossover;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Klasa bazowa dla krzyżowań tablic liczb rzeczywistych
 * Data utworzenia: 16.02.16, 16:58
 *
 * @author Michał Toporowski
 */
abstract class BaseDoubleCrossover extends AbstractCrossover<double[]> {
    protected BaseDoubleCrossover(Probability crossoverProbability) {
        super(1, crossoverProbability);
    }

    @Override
    protected List<double[]> mate(double[] parent1, double[] parent2, int numberOfCrossoverPoints, Random rng) {
        double[] child1 = mateOne(parent1, parent2, numberOfCrossoverPoints, rng);
        double[] child2 = mateOne(parent1, parent2, numberOfCrossoverPoints, rng);
        return Arrays.asList(child1, child2);
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
    protected abstract double[] mateOne(double[] parent1, double[] parent2, int numberOfCrossoverPoints, Random rng);
}
