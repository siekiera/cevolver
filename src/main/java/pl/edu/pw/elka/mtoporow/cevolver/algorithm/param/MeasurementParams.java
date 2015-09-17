package pl.edu.pw.elka.mtoporow.cevolver.algorithm.param;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.RealVector;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.MicrostripParams;

import java.util.Objects;

/**
 * Parametry pomiaru
 * Data utworzenia: 21.06.15, 10:01
 *
 * @author Michał Toporowski
 */
public class MeasurementParams {
    // TODO:: może przenieść do Scali
    // TODO:: może połączyć to jakoś z Distances w jedną klasę

    /**
     * Częstotliwości, z jakimi wykonywany był pomiar
     */
    private static RealVector frequencies;

    /**
     * Parametry linii mikropaskowej
     */
    private static MicrostripParams microstripParams;

    /**
     * Całk. długość linii
     */
    private static double totalLength;

    /**
     * Impedancja na wejściu
     */
    private static Complex impedance;
    /**
     * Liczba nieciągłości
     */
    private static int discontinuitiesCount;

    public static RealVector getFrequencies() {
        Objects.requireNonNull(frequencies, "Frequencies not set");
        return frequencies;
    }

    public static void setFrequencies(RealVector frequencies) {
        MeasurementParams.frequencies = frequencies;
    }

    public static MicrostripParams getMicrostripParams() {
        Objects.requireNonNull(frequencies, "Microstrip params not set");
        return microstripParams;
    }

    public static void setMicrostripParams(MicrostripParams microstripParams) {
        MeasurementParams.microstripParams = microstripParams;
    }

    public static double getTotalLength() {
        return totalLength;
    }

    public static void setTotalLength(double totalLength) {
        MeasurementParams.totalLength = totalLength;
    }

    public static int getDiscontinuitiesCount() {
        return discontinuitiesCount;
    }

    public static void setDiscontinuitiesCount(int discontinuitiesCount) {
        MeasurementParams.discontinuitiesCount = discontinuitiesCount;
    }

    public static Complex getImpedance() {
        return impedance;
    }

    public static void setImpedance(Complex impedance) {
        MeasurementParams.impedance = impedance;
    }
}
