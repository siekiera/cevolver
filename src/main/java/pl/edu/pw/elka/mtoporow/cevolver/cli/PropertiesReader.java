package pl.edu.pw.elka.mtoporow.cevolver.cli;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.AlgorithmPartParams;
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.AlgorithmPartType;
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.RegisteredParams;
import scala.Option;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Klasa odczytująca plik properties
 * Data utworzenia: 12.06.15, 11:10
 *
 * @author Michał Toporowski
 */
public abstract class PropertiesReader {

    private final Properties properties;

    public PropertiesReader(final InputStream is) throws IOException {
        this.properties = new Properties();
        properties.load(is);
        init();
        read();
    }

    public PropertiesReader(final Properties properties) throws IOException {
        this.properties = properties;
        init();
        read();
    }

    /**
     * Wczytuje plik
     */
    protected abstract void read() throws IOException;

    /**
     * Czynności inicjalne do wykonania przed read()
     */
    protected void init() {

    }


    protected <T extends Enum<T> & AlgorithmPartType> AlgorithmPartParams<T> readOne(final Class<T> partTypeClass, final String propName) throws IOException {
        String propVal = getRequiredProperty(propName);
        // Odczytujemy typ części algorytmu
        T partType = Enum.valueOf(partTypeClass, propVal.toUpperCase());
        AlgorithmPartParams<T> partParams = new AlgorithmPartParams<>(partType);
        // Odczytujemy informację nt. wszystkich parametrów danej części
        Option<RegisteredParams.ParamDef[]> partParamDefs = RegisteredParams.partsParamDefs().get(partParams.partType());
        if (partParamDefs.isDefined()) {
            for (RegisteredParams.ParamDef partParamDef : partParamDefs.get()) {
                String parVal = getRequiredProperty(propName + "." + partParamDef.name());
                partParams.setStringAsParamVal(partParamDef, parVal);
            }
        }
        return partParams;
    }

    protected <T extends Enum<T> & AlgorithmPartType> List<AlgorithmPartParams<T>> readMulti(final Class<T> partTypeClass, final String propPrefix) throws IOException {
        int counter = 0;
        List<AlgorithmPartParams<T>> result = new LinkedList<>();
        String key;
        while (properties.containsKey(key = propPrefix + "." + counter)) {
            result.add(readOne(partTypeClass, key));
            counter++;
        }
        return result;
    }

    protected String getRequiredProperty(String propName) throws IOException {
        String propVal = properties.getProperty(propName);
        if (propVal == null) {
            throw new IOException("Missing property: " + propName);
        }
        return propVal;
    }

    protected int readInt(String propName) throws IOException, NumberFormatException {
        String propVal = getRequiredProperty(propName);
        return Integer.parseInt(propVal);
    }

    protected double readDouble(String propName) throws IOException, NumberFormatException {
        String propVal = getRequiredProperty(propName);
        return Double.parseDouble(propVal);
    }

    protected int readIntWithDefault(final String propName, final int defaultValue) {
        String propVal = properties.getProperty(propName);
        if (propVal != null) {
            return Integer.parseInt(propVal);
        } else {
            return defaultValue;
        }
    }

    protected <T extends Enum<T>> T readEnumWithDefault(final Class<T> enumClass, final String propName, final T defaultValue) {
        String propVal = properties.getProperty(propName);
        if (propVal != null) {
            try {
                return Enum.valueOf(enumClass, propVal.toUpperCase());
            } catch (Exception ignored) {

            }
        }
        return defaultValue;
    }

    protected RealVector readOptionalDoubles(String propName) {
        String propVal = properties.getProperty(propName);
        if (propVal != null) {
            String[] values = propVal.split(",");
            double[] doubleVals = Arrays.stream(values).mapToDouble(Double::valueOf).toArray();
            return new ArrayRealVector(doubleVals);
        }
        return new ArrayRealVector();
    }

}
