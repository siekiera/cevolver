package pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets;

import pl.edu.pw.elka.mtoporow.cevolver.cli.EnvPropertiesReader;
import pl.edu.pw.elka.mtoporow.cevolver.data.TouchstoneDataProvider;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.CanalResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Klasa przechowująca dane
 * Data utworzenia: 05.02.16, 13:02
 *
 * @author Michał Toporowski
 */
public final class DataHolder {
    private static List<String> availableDataSets;
    private static DataSet current;
    private static final File dataDir;
    private static String currentId;

    private DataHolder() {
    }

    /**
     * Zwraca listę dostępnych zestawów danych
     *
     * @return lista zestawów danych
     */
    public static List<String> getAvailableDataSets() {
        if (availableDataSets == null) {
            // Zbieramy wszystkie pliki .properties z katalogu data z jara
            File[] propertyFiles = dataDir.listFiles((d, name) -> name.endsWith(".properties"));
            // i zapisujemy do listy ich nazwy bez rozszerzeń
            availableDataSets = Arrays.stream(propertyFiles).map(f -> f.getName().replaceFirst("[.][^.]+$", "")).collect(Collectors.toList());
        }
        return availableDataSets;
    }

    /**
     * Ładuje zestaw danych
     * Nowy zestaw będzie mógł być pobrany za pomocą metody getCurrent()
     *
     * @param dataSetId identyfikator (nazwa pliku bez rozszerzenia)
     */
    public static void load(final String dataSetId) {
        File propertiesFile = new File(dataDir, dataSetId + ".properties");
        File dataFile = new File(dataDir, dataSetId + ".s2p");
        try {
            // Odczytanie danych
            TouchstoneDataProvider dataProvider = new TouchstoneDataProvider(dataFile);
            CanalResponse canalResponse = dataProvider.provide();
            // Odczytanie właściwości
            try (FileInputStream fis = new FileInputStream(propertiesFile)) {
                EnvPropertiesReader reader = new EnvPropertiesReader(fis);
                current = new DataSet(reader.getMeasurementParams(), canalResponse, reader.getExpectedDistances());
                // Dodajemy jeszcze częstotliwości pobrane z pliku z danymi
                current.measurementParams().setFrequencies(dataProvider.frequencies());
                currentId = dataSetId;
            }
        } catch (IOException e) {
            throw new DataLoadingException(e);
        }
    }

    /**
     * Zwraca aktywny zestaw danych
     *
     * @return aktywny zestaw danych
     */
    public static DataSet getCurrent() {
        return current;
    }

    /**
     * Zwraca identyfikator aktywnego zestawu danych
     *
     * @return identyfikator aktywnego zestawu danych
     */
    public static String getCurrentId() {
        return currentId;
    }

    /**
     * Sprawdza, czy dane zostały załadowane
     *
     * @return true, jeśli tak
     */
    public static boolean isLoaded() {
        return current != null;
    }

    static {
        try {
            URL dataUrl = DataHolder.class.getClassLoader().getResource("data");
            if (dataUrl == null) {
                throw new DataLoadingException("No data to read!");
            }
            dataDir = new File(dataUrl.toURI());
        } catch (URISyntaxException e) {
            throw new DataLoadingException(e);
        }
    }
}
