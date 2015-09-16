package pl.edu.pw.elka.mtoporow.cevolver.cli;

import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.MeasurementParams;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.MicrostripParams;

import java.io.IOException;
import java.io.InputStream;

/**
 * Klasa odczytująca plik konfiguracji parametrów środowiska (np. mikropaska)
 * <p>
 * Data utworzenia: 15.09.15 11:27
 *
 * @author Michał Toporowski
 */
public class EnvPropertiesReader extends PropertiesReader {

    public EnvPropertiesReader(InputStream is) throws IOException {
        super(is);
    }

    @Override
    protected void read() throws IOException {
        MeasurementParams.setMicrostripParams(new MicrostripParams(
                readDouble("microstrip.w"),
                readDouble("microstrip.t"),
                readDouble("microstrip.h"),
                readDouble("microstrip.epsr"),
                readDouble("microstrip.discW"),
                readDouble("microstrip.discL")
        ));
        MeasurementParams.setTotalLength(readDouble("totalLength"));
        MeasurementParams.setDiscontinuitiesCount(readInt("discontinuitiesCount"));
    }
}
