package pl.edu.pw.elka.mtoporow.cevolver.ext.chart;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import pl.edu.pw.elka.mtoporow.cevolver.util.export.SerializationUtil;

import java.io.File;
import java.util.List;

/**
 * Aplikacja JavaFX wyświetlająca wykresy
 * Dane pobierane są z pliku zawierającego zserializowany obiekt ChartData
 * <p>
 * Data utworzenia: 11.02.16, 12:00
 *
 * @author Michał Toporowski
 */
public class ChartViewer extends Application {
    private ChartData chartData;


    @Override
    public void init() throws Exception {
        List<String> parameters = getParameters().getRaw();
        if (parameters.isEmpty()) {
            throw new IllegalArgumentException("Nazwa pliku powinna być pierwszym parametrem!");
        }
        File file = new File(parameters.get(0));
        this.chartData = SerializationUtil.deserialize(file);
    }

    @Override
    public void start(Stage stage) throws Exception {
        createChart(stage);
    }

    /**
     * Tworzy wykres na podstawie danych z chartData
     *
     * @param stage stage
     */
    private void createChart(Stage stage) {
        stage.setTitle("Wykres");
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(chartData.getxValues().getName());
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

        lineChart.setTitle(chartData.getName());

        double[] xValues = chartData.getxValues().getValues();
        for (ChartData.Series ySeries : chartData.getyValues()) {
            double[] yValues = ySeries.getValues();
            XYChart.Series<Number, Number> jfxSeries = new XYChart.Series<>();
            jfxSeries.setName(ySeries.getName());
            for (int i = 0; i < xValues.length; i++) {
                jfxSeries.getData().add(new XYChart.Data<>(xValues[i], yValues[i]));
            }
            lineChart.getData().add(jfxSeries);
        }

        Scene scene = new Scene(lineChart, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Uruchamia narzędzie
     *
     * @param args argumenty linii poleceń, pierwszym ma być nazwa pliku zawierającego zserializowany obiekt ChartData
     */
    public static void main(String args[]) {
        launch(args);
    }

}
