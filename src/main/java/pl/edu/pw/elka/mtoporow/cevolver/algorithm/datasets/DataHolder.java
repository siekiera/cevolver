package pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets;

import pl.edu.pw.elka.mtoporow.cevolver.cli.EnvPropertiesReader;
import pl.edu.pw.elka.mtoporow.cevolver.data.TouchstoneDataProvider;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.CanalResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
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
    private static final Path dataDir;
    private static String currentId;

    private DataHolder() {
    }

    /**
     * Zwraca listę dostępnych zestawów danych
     *
     * @return lista nazw zestawów danych
     */
    public static List<String> getAvailableDataSets() {
        if (availableDataSets == null) {
            try {
                // Zbieramy wszystkie pliki .properties z katalogu data z jara
                // i zapisujemy do listy ich nazwy bez rozszerzeń
                availableDataSets = Files.walk(dataDir).map(p -> p.getFileName().toString())
                        .filter(name -> name.endsWith(".properties"))
                        .map(f -> f.replaceFirst("[.][^.]+$", ""))
                        .collect(Collectors.toList());
            } catch (IOException e) {
                throw new DataLoadingException(e);
            }
        }
        return availableDataSets;
    }

    /**
     * Zwraca listę zestawów danych pasujących do wyrażenia regularnego
     *
     * @param regex wyrażenie regularne
     * @return lista nazw zestawów danych
     */
    public static Iterator<String> getMatchingDataSets(final String regex) {
        return getAvailableDataSets().stream().filter(s -> s.matches(regex)).iterator();
    }

    /**
     * Ładuje zestaw danych
     * Nowy zestaw będzie mógł być pobrany za pomocą metody getCurrent()
     *
     * @param dataSetId identyfikator (nazwa pliku bez rozszerzenia)
     */
    public static void load(final String dataSetId) {
        Path propertiesFile = dataDir.resolve(dataSetId + ".properties");
        Path dataFile = dataDir.resolve(dataSetId + ".s2p");
        try {
            // Odczytanie danych
            TouchstoneDataProvider dataProvider = new TouchstoneDataProvider(Files.newInputStream(dataFile));
            CanalResponse canalResponse = dataProvider.provide();
            // Odczytanie właściwości
            try (InputStream fis = Files.newInputStream(propertiesFile)) {
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
     * Ładuje zestaw danych, o ile nie jest już załadowany
     * @see DataHolder#load(java.lang.String)
     *
     * @param dataSetId identyfikator (nazwa pliku bez rozszerzenia)
     */
    public static void lazyLoad(final String dataSetId) {
        if (!Objects.equals(dataSetId, getCurrentId())) {
            load(dataSetId);
        }
    }

    /**
     * Zwraca aktywny zestaw danych
     *
     * @return aktywny zestaw danych
     */
    public static DataSet getCurrent() {
        if (current == null) {
            throw new IllegalArgumentException("Nie załadowano danych!");
        }
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
            URI dataUri = dataUrl.toURI();
            if (dataUri.getScheme().equals("jar")) {
                // Jeśli ścieżka jest wewnątrz jara, trzeba się do niej dostać przez system plików ZIP
                FileSystem fileSystem = FileSystems.newFileSystem(dataUri, Collections.<String, Object>emptyMap());
                dataDir = fileSystem.getPath("/data");
            } else {
                dataDir = Paths.get(dataUri);
            }
        } catch (URISyntaxException | IOException e) {
            throw new DataLoadingException(e);
        }
    }
}
