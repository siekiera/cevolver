package pl.edu.pw.elka.mtoporow.cevolver.ext.chart;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Zestaw danych, który może być wyświetlony na wykresie za pomocą klasy ChartViewer
 * Data utworzenia: 11.02.16, 11:26
 *
 * @author Michał Toporowski
 */
public class ChartData implements Serializable {
    private final String name;
    private final Series xValues;
    private final List<Series> yValues = new LinkedList<>();

    /**
     * Konstruktor
     *
     * @param name    nazwa zestawu danych
     * @param xName   nazwa osi x
     * @param xValues wartości osi x
     */
    public ChartData(String name, String xName, double[] xValues) {
        this.name = name;
        this.xValues = new Series(xName, xValues);
    }

    /**
     * Dodaje serię danych
     *
     * @param name    nazwa
     * @param yValues wartości
     */
    public void addSeries(final String name, final double[] yValues) {
        this.yValues.add(new Series(name, yValues));
    }


    /**
     * Seria danych
     */
    public class Series implements Serializable {
        private final String name;
        private final double[] values;

        private Series(String name, double[] values) {
            this.name = name;
            this.values = values;
        }

        public String getName() {
            return name;
        }

        public double[] getValues() {
            return values;
        }
    }

    public Series getxValues() {
        return xValues;
    }

    public List<Series> getyValues() {
        return yValues;
    }

    public String getName() {
        return name;
    }
}
