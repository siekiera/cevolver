package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts;

import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.operators.DoubleArrayCrossover;

/**
 * Operator krzyżowania
 * Data utworzenia: 13.11.15, 13:51
 *
 * @author Michał Toporowski
 */
public class DistArrayCrossover extends BaseCrossover {

    public DistArrayCrossover(Probability probability) {
        super(new DoubleArrayCrossover(1, probability), true);
    }
}
