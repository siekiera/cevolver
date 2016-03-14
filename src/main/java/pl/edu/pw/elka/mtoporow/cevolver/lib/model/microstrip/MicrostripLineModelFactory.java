package pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip;

import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder;
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.MeasurementParams;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.Distances;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.LWDists;

/**
 * Fabryka modeli linii mikropaskowej
 * Data utworzenia: 04.03.16, 11:13
 *
 * @author Michał Toporowski
 */
public class MicrostripLineModelFactory {

    /**
     * Tworzy nowy model typu kompatybilnego z aktualnym zestawem danych
     *
     * @param distances wektor odległości
     * @return nowy obiekt modelu
     */
    public static MicrostripLineModel newModel(final Distances distances) {
        final MeasurementParams mp = DataHolder.getCurrent().measurementParams();
        final MicrostripParams params = mp.getMicrostripParams();
        switch (mp.getModelType()) {
            case SHORTBREAK:
                return new ShortbreakLineModel(LWDists.shortbreak(distances), params);
            case LONGBREAK:
                return new FixedWidthLineModel(distances, params);
            default:
                throw new IllegalArgumentException("Nieznany model");
        }
    }
}
