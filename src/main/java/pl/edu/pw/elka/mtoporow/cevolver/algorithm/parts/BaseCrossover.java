package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.operators.AbstractCrossover;
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.MeasurementParams;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.AbstractCanalModel;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.Distances;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.MicrostripLineModel;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.MicrostripParams;
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps;

import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Klasa bazowa dla operatora krzyżowania
 * Data utworzenia: 09.01.16, 16:48
 *
 * @author Michał Toporowski
 */
class BaseCrossover implements EvolutionaryOperator<AbstractCanalModel> {
    private final AbstractCrossover<double[]> doubleCrossover;
    private final Function<RealVector, RealVector> asSums;
    private final Function<RealVector, RealVector> fromSums;

    public BaseCrossover(AbstractCrossover<double[]> doubleCrossover, boolean useSums) {
        this.doubleCrossover = doubleCrossover;
        if (useSums) {
            this.asSums = MatrixOps::asSums;
            this.fromSums = MatrixOps::fromSums;
        } else {
            this.asSums = Function.identity();
            this.fromSums = Function.identity();
        }
    }

    @Override
    public List<AbstractCanalModel> apply(List<AbstractCanalModel> selectedCandidates, Random rng) {
        // FIXME:: te paramsy chyba nie powinny być w modelu, to bez sensu
        MicrostripParams params = MeasurementParams.getMicrostripParams();
        List<double[]> distValues = selectedCandidates.parallelStream()
                .map(c -> c.distances().distances())
                .map(asSums)
                .map(RealVector::toArray)
                .collect(Collectors.toList());
        List<double[]> crossed = doubleCrossover.apply(distValues, rng);
        return crossed.parallelStream()
//                .map(this::sort)
                .map(ArrayRealVector::new)
                .map(fromSums)
                .map(v -> new MicrostripLineModel(new Distances(v), params))
                .collect(Collectors.toList());
    }
}
