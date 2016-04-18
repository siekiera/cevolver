package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts;


import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Pair;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.operators.DoubleArrayCrossover;

import java.util.List;
import java.util.Random;

/**
 * Operator krzyżowania
 * Data utworzenia: 13.11.15, 13:51
 *
 * @author Michał Toporowski
 */
public class DistArrayCrossover extends BaseCrossover {
    private static final DoubleCrossover DOUBLE_CROSSOVER = new DoubleCrossover();

    public DistArrayCrossover(final Probability probability) {
        super(probability);
    }

    @Override
    protected Pair<RealVector, RealVector> mate(RealVector parent1, RealVector parent2, int numberOfCrossoverPoints, Random rng) {
        return DOUBLE_CROSSOVER.mateVectors(parent1, parent2, numberOfCrossoverPoints, rng);
    }

    /**
     * Operator krzyżowania tablic liczb rzeczywistych - wykorzystanie implementacji DoubleArrayCrossover
     */
    private static class DoubleCrossover extends DoubleArrayCrossover {
        Pair<RealVector, RealVector> mateVectors(RealVector parent1, RealVector parent2, int numberOfCrossoverPoints, Random rng) {
            List<double[]> crossed = mate(parent1.toArray(), parent2.toArray(), numberOfCrossoverPoints, rng);
            RealVector child1 = MatrixUtils.createRealVector(crossed.get(0));
            RealVector child2 = MatrixUtils.createRealVector(crossed.get(1));
            return new Pair<>(child1, child2);
        }
    }


}
