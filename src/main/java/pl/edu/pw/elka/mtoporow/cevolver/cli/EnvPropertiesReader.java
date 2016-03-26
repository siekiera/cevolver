package pl.edu.pw.elka.mtoporow.cevolver.cli;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.MeasurementParams;
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.ModelType;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.Distances;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.LWDists;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.MicrostripParams;
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.maths.Units;

import java.io.IOException;
import java.io.InputStream;

/**
 * Klasa odczytująca plik konfiguracji parametrów środowiska (np. mikropaska)
 * <p>
 * Data utworzenia: 15.09.15 11:27
 * <p>
 * Założenie: odległości w MM
 *
 * @author Michał Toporowski
 */
public class EnvPropertiesReader extends PropertiesReader {

    private static final Units.BaseUnit UNIT = Units.MILLI();

    /**
     * Odległości do rzeczywistych miejsc nieciągłości (opcjonalne)
     */
    private Distances expectedDistances;

    /**
     * Parametry pomiaru
     */
    private MeasurementParams measurementParams;

    public EnvPropertiesReader(InputStream is) throws IOException {
        super(is);
    }

    @Override
    protected void read() throws IOException {
        measurementParams = new MeasurementParams();
        measurementParams.setMicrostripParams(new MicrostripParams(
                readDoubleInUnit("microstrip.w"),
                readDoubleInUnit("microstrip.t"),
                readDoubleInUnit("microstrip.h"),
                readDouble("microstrip.epsr"),
                readDoubleInUnit("microstrip.biggerW")
        ));
        measurementParams.setImpedance(Complex.valueOf(readDouble("impedance")));
        measurementParams.setTotalLength(readDoubleInUnit("totalLength"));
        measurementParams.setModelType(readEnumWithDefault(ModelType.class, "model", ModelType.LONGBREAK));
        RealVector expectedDistances = readOptionalDoubles("discontinuities");
        expectedDistances.mapMultiplyToSelf(UNIT.valueInSI());
        if (measurementParams.getModelType() == ModelType.SHORTBREAK) {
            RealVector expectedWidths = readOptionalDoubles("widths");
            expectedWidths.mapMultiplyToSelf(UNIT.valueInSI());
            this.expectedDistances = new LWDists(expectedDistances, expectedWidths);
        } else {
            // Longbreak - dodajemy biggerW jako odległości
            RealVector expectedWidths = new ArrayRealVector(expectedDistances.getDimension() - 1, measurementParams.getMicrostripParams().biggerW());
            this.expectedDistances = new LWDists(expectedDistances, expectedWidths);
        }

    }

    private double readDoubleInUnit(final String name) throws IOException {
        return new Units.U(readDouble(name)).toSI(UNIT);
    }

    public Distances getExpectedDistances() {
        return expectedDistances;
    }

    public MeasurementParams getMeasurementParams() {
        return measurementParams;
    }
}
