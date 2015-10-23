package pl.edu.pw.elka.mtoporow.cevolver.cli;

import org.apache.commons.math3.linear.RealVector;
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.MeasurementParams;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.Distances;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.MicrostripParams;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.util.Units;

import java.io.IOException;
import java.io.InputStream;

/**
 * Klasa odczytująca plik konfiguracji parametrów środowiska (np. mikropaska)
 * <p>
 * Data utworzenia: 15.09.15 11:27
 * <p>
 * Założenie: odległości w MILach (1/1000 cala) TODO:: może warto zmienić na MM
 *
 * @author Michał Toporowski
 */
public class EnvPropertiesReader extends PropertiesReader {

    /**
     * Odległości do rzeczywistych miejsc nieciągłości (opcjonalne)
     */
    private Distances expectedDistances;

    public EnvPropertiesReader(InputStream is) throws IOException {
        super(is);
    }

    @Override
    protected void read() throws IOException {
        MeasurementParams.setMicrostripParams(new MicrostripParams(
                readMilDouble("microstrip.w"),
                readMilDouble("microstrip.t"),
                readMilDouble("microstrip.h"),
                readDouble("microstrip.epsr"),
                readMilDouble("microstrip.discW"),
                readMilDouble("microstrip.discL")
        ));
        MeasurementParams.setTotalLength(readMilDouble("totalLength"));
        MeasurementParams.setDiscontinuitiesCount(readInt("discontinuitiesCount"));
        RealVector expectedDistances = readOptionalDoubles("discontinuities");
        if (expectedDistances != null) {
//            expectedDistances = expectedDistances.append(MeasurementParams.getTotalLength());
            expectedDistances.mapMultiplyToSelf(Units.MIL().valueInSI());
            this.expectedDistances = new Distances(expectedDistances);
        }
    }

    private double readMilDouble(final String name) throws IOException {
//        return UnitConversions.milToM(readDouble(name));
        return new Units.U(readDouble(name)).toSI(Units.MIL());
    }

    public Distances getExpectedDistances() {
        return expectedDistances;
    }
}
