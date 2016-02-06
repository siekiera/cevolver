package pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.RealVector;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.MicrostripParams;
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps;

import java.util.Objects;

/**
 * Parametry pomiaru
 * Data utworzenia: 21.06.15, 10:01
 *
 * @author Michał Toporowski
 */
public class MeasurementParams {

    /**
     * Częstotliwości, z jakimi wykonywany był pomiar
     */
    private RealVector frequencies;

    /**
     * Parametry linii mikropaskowej
     */
    private MicrostripParams microstripParams;

    /**
     * Całk. długość linii
     */
    private double totalLength;

    /**
     * Impedancja na wejściu
     */
    private Complex impedance;
    /**
     * Liczba nieciągłości
     */
    private int discontinuitiesCount;

    public RealVector getFrequencies() {
        Objects.requireNonNull(frequencies, "Frequencies not set");
        return frequencies;
    }

    public void setFrequencies(RealVector frequencies) {
        this.frequencies = frequencies;
    }

    public MicrostripParams getMicrostripParams() {
        Objects.requireNonNull(frequencies, "Microstrip params not set");
        return microstripParams;
    }

    public void setMicrostripParams(MicrostripParams microstripParams) {
        this.microstripParams = microstripParams;
    }

    public double getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(double totalLength) {
        this.totalLength = totalLength;
    }

    public int getDiscontinuitiesCount() {
        return discontinuitiesCount;
    }

    public void setDiscontinuitiesCount(int discontinuitiesCount) {
        this.discontinuitiesCount = discontinuitiesCount;
    }

    public Complex getImpedance() {
        return impedance;
    }

    public void setImpedance(Complex impedance) {
        this.impedance = impedance;
    }

    public double getMinMicrostripLength() {
        return 0;
    }

    @Override
    public String toString() {
        return "Parametry pomiaru: {" +
//                "\n częstotliwości=" + frequencies +
                "\n parametry mikropaska=" + microstripParams +
                "\n długość całkowita=" + totalLength +
                "\n impedancja=" + impedance +
                "\n liczba punktów nieciągłości=" + discontinuitiesCount +
                '}';
    }
}
