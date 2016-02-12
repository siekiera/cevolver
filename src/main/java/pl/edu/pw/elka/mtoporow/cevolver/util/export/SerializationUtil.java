package pl.edu.pw.elka.mtoporow.cevolver.util.export;

import java.io.*;
import java.util.function.Consumer;

/**
 * Metody narzędziowe do serializacji obiektów
 * Data utworzenia: 11.02.16, 13:57
 *
 * @author Michał Toporowski
 */
public final class SerializationUtil {

    private SerializationUtil() {

    }

    public static void serialize(final File file, final Serializable serializable) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(serializable);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(final File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (T) ois.readObject();
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new IOException(e);
        }
    }

    /**
     * Deserializuje plik, modyfikuje obiekt i serializuje z powrotem
     *
     * @param file     plik
     * @param modifier funkcja modyfikująca
     * @param <T>      typ obiektu
     * @throws IOException
     */
    public static <T extends Serializable> void modify(final File file, final Consumer<T> modifier) throws IOException {
        T object = deserialize(file);
        modifier.accept(object);
        serialize(file, object);
    }

}
