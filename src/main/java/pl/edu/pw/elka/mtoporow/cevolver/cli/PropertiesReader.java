package pl.edu.pw.elka.mtoporow.cevolver.cli;

import pl.edu.pw.elka.mtoporow.cevolver.algorithm.AlgorithmParameters;
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.AlgorithmPartParams;
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.*;
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.util.Conversions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Klasa odczytująca plik konfiguracji parametrów algorytmu
 * Data utworzenia: 12.06.15, 11:10
 *
 * @author Michał Toporowski
 */
public class PropertiesReader {

    private final Properties properties = new Properties();
    private final AlgorithmParameters parameters = new AlgorithmParameters();
    private final InputStream is;

    public PropertiesReader(final InputStream is) throws IOException {
        this.is = is;
        read();
    }

    /**
     * Wczytuje plik
     */
    private void read() throws IOException {
        properties.load(is);
        parameters.candidateFactory_$eq(readOne(CFType.class, "cf"));
        AlgorithmPartParams<EOType> operator = readOne(EOType.class, "eo");
        parameters.operators_$eq(Conversions.objectToScalaList(operator)); //FIXME:: chcemy obsługiwać > 1
        parameters.fitnessEvaluator_$eq(readOne(FEType.class, "fe"));
        parameters.selectionStrategy_$eq(readOne(SSType.class, "ss"));
        parameters.terminationCondition_$eq(readOne(TCType.class, "tc"));
    }

    private <T extends Enum<T> & AlgorithmPartType> AlgorithmPartParams<T> readOne(final Class<T> partTypeClass, final String propName) throws IOException {
        String propVal = getRequiredProperty(propName);
        AlgorithmPartParams<T> partParams = new AlgorithmPartParams<>();
        // Odczytujemy typ części algorytmu
        partParams.partType_$eq(Enum.valueOf(partTypeClass, propVal.toUpperCase()));
        // Odczytujemy informację nt. wszystkich parametrów danej części
        RegisteredParams.ParamDef[] partParamDefs = RegisteredParams.partsParamDefs().get(partParams.partType());
        if (partParamDefs != null) {
            for (RegisteredParams.ParamDef partParamDef : partParamDefs) {
                String parVal = getRequiredProperty(propName + "." + partParamDef.name());
                partParams.setParamValAsString(partParamDef, parVal);
            }
        }
        return partParams;
    }

    private String getRequiredProperty(String propName) throws IOException {
        String propVal = properties.getProperty(propName);
        if (propVal == null) {
            throw new IOException("Missing property: " + propName);
        }
        return propVal;
    }

    public AlgorithmParameters getParameters() {
        return parameters;
    }
}
