package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.operators.DoubleArrayCrossover;
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.MeasurementParams;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.AbstractCanalModel;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.Distances;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.MicrostripLineModel;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.MicrostripParams;
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Operator krzyżowania
 * Data utworzenia: 13.11.15, 13:51
 *
 * @author Michał Toporowski
 */
public class DistArrayCrossover implements EvolutionaryOperator<AbstractCanalModel> {

    private final Probability probability;

    public DistArrayCrossover(Probability probability) {
        this.probability = probability;
    }

    @Override
    public List<AbstractCanalModel> apply(List<AbstractCanalModel> selectedCandidates, Random rng) {
        // FIXME:: te paramsy chyba nie powinny być w modelu, to bez sensu
        MicrostripParams params = MeasurementParams.getMicrostripParams();
        List<double[]> distValues = selectedCandidates.parallelStream()
                .map(c -> c.distances().distances())
                .map(MatrixOps::asSums)
                .map(RealVector::toArray)
                .collect(Collectors.toList());
        List<double[]> crossed = new DoubleArrayCrossover(1, probability).apply(distValues, rng);
        return crossed.parallelStream()
//                .map(this::sort)
                .map(ArrayRealVector::new)
                .map(MatrixOps::fromSums)
                .map(v -> new MicrostripLineModel(new Distances(v), params))
                .collect(Collectors.toList());
    }
}
