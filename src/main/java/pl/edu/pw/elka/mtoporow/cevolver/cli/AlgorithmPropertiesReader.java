package pl.edu.pw.elka.mtoporow.cevolver.cli;

import pl.edu.pw.elka.mtoporow.cevolver.algorithm.AlgorithmParameters;
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.*;
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.util.Conversions;

import java.io.IOException;
import java.io.InputStream;

/**
 * Klasa odczytująca plik konfiguracji parametrów algorytmu
 * <p>
 * Data utworzenia: 15.09.15 11:27
 *
 * @author Michał Toporowski
 */
public class AlgorithmPropertiesReader extends PropertiesReader {

    private AlgorithmParameters parameters;

    public AlgorithmPropertiesReader(InputStream is) throws IOException {
        super(is);
    }

    @Override
    protected void init() {
        parameters = new AlgorithmParameters();
    }

    @Override
    protected void read() throws IOException {
        parameters.candidateFactory_$eq(readOne(CFType.class, "cf"));
//        AlgorithmPartParams<EOType> operator = readOne(EOType.class, "eo");
//        parameters.operators_$eq(Conversions.objectToScalaList(operator)); //FIXME:: chcemy obsługiwać > 1
        parameters.operators_$eq(Conversions.javaToScalaList(readMulti(EOType.class, "eo")));
        parameters.fitnessEvaluator_$eq(readOne(FEType.class, "fe"));
        parameters.selectionStrategy_$eq(readOne(SSType.class, "ss"));
        parameters.terminationCondition_$eq(readOne(TCType.class, "tc"));
        parameters.populationSize_$eq(readInt("populationSize"));
        parameters.eliteCount_$eq(readInt("eliteCount"));
    }

    public AlgorithmParameters getParameters() {
        return parameters;
    }

}
