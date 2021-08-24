package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.BoundariesLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

import static javafx.beans.binding.Bindings.select;
import static javafx.beans.binding.Bindings.when;

/**
 * Classe principale
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public class Main extends Application {

    private final static String HYG_STRING = "/hygdata_v3.csv";
    private final static String ASTERISM_STRING = "/asterisms.txt";
    private final static String BOUNDARIES_STRING = "/bound_20.dat";

    private final static double OBSERVER_DEGREES_LONGITUDE = 6.57;
    private final static double OBSERVER_DEGREES_LATITUDE = 46.52;

    private final static double OBSERVATION_DEGREES_AZ = 180.000000000001;
    private final static double OBSERVATION_DEGREES_ALT = 15;

    private final static double FIELD_OF_VIEW = 100;

    private final static double WINDOW_MIN_WIDTH = 800;
    private final static double WINDOW_MIN_HEIGHT = 600;

    private final static String WINDOW_TITLE = "Rigel";

    private final static String RESET_STRING = "\uf0e2";
    private final static String PLAY_STRING = "\uf04b";
    private final static String PAUSE_STRING = "\uf04c";

    private TimeAnimator timeAnimator;


    /**
     * Méthode Main
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    private InputStream resourceStream(String resourceName) {
        return getClass().getResourceAsStream(resourceName);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        StarCatalogue.Builder builder = new StarCatalogue.Builder();
        try (InputStream as = resourceStream(BOUNDARIES_STRING)) {
            builder.loadFrom(as, BoundariesLoader.INSTANCE);
        }
        try (InputStream hs = resourceStream(HYG_STRING)) {
            builder.loadFrom(hs, HygDatabaseLoader.INSTANCE);
        }
        try (InputStream as = resourceStream(ASTERISM_STRING)) {
            builder = builder.loadFrom(as, AsterismLoader.INSTANCE);
            StarCatalogue catalogue = builder.build();

            DateTimeBean dateTimeBean = new DateTimeBean();
            dateTimeBean.setZonedDateTime(ZonedDateTime.now());

            ObserverLocationBean observerLocationBean =
                    new ObserverLocationBean();
            observerLocationBean.setCoordinates(
                    GeographicCoordinates.ofDeg(OBSERVER_DEGREES_LONGITUDE, OBSERVER_DEGREES_LATITUDE));

            ViewingParametersBean viewingParametersBean =
                    new ViewingParametersBean();
            viewingParametersBean.setCenter(
                    HorizontalCoordinates.ofDeg(OBSERVATION_DEGREES_AZ, OBSERVATION_DEGREES_ALT));
            viewingParametersBean.setFieldOfViewDeg(FIELD_OF_VIEW);

            SkyCanvasManager canvasManager = new SkyCanvasManager(
                    catalogue,
                    dateTimeBean,
                    observerLocationBean,
                    viewingParametersBean);


            Canvas sky = canvasManager.canvas();
            Pane skyPane = new Pane();

            sky.widthProperty().bind(skyPane.widthProperty());
            sky.heightProperty().bind(skyPane.heightProperty());
            skyPane.getChildren().add(sky);

            BorderPane borderPane = new BorderPane();
            timeAnimator = new TimeAnimator(canvasManager.getDateTimeBean());

            borderPane.setCenter(skyPane);
            borderPane.setTop(controlBar(canvasManager));
            borderPane.setBottom(infoBar(canvasManager));


            primaryStage.setMinWidth(WINDOW_MIN_WIDTH);
            primaryStage.setMinHeight(WINDOW_MIN_HEIGHT);

            primaryStage.setY(100);
            primaryStage.setTitle(WINDOW_TITLE);

            primaryStage.setScene(new Scene(borderPane));
            primaryStage.show();

            sky.requestFocus();
        }
    }


    private HBox obsPos(SkyCanvasManager skyCanvasManager) {

        HBox obsPos = new HBox();
        obsPos.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        Label lonLabel = new Label("Longitude (°) :");
        Label latLabel = new Label("Latitude (°) :");

        NumberStringConverter numberStringConverter =
                new NumberStringConverter("#0.00");

        TextFormatter<Number> lonTextFormatter =
                new TextFormatter<>(numberStringConverter, 0, formattor(numberStringConverter));
        lonTextFormatter.setValue(skyCanvasManager.getObsLocBean().getLonDeg());

        TextField lonTextField =
                new TextField();
        lonTextField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        lonTextField.setTextFormatter(lonTextFormatter);

        TextFormatter<Number> latTextFormatter =
                new TextFormatter<>(numberStringConverter, 0, formattor(numberStringConverter));
        latTextFormatter.setValue(skyCanvasManager.getObsLocBean().getLatDeg());

        TextField latTextField =
                new TextField();
        latTextField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        latTextField.setTextFormatter(latTextFormatter);

        skyCanvasManager.getObsLocBean().lonDegProprety().bindBidirectional(lonTextFormatter.valueProperty());
        skyCanvasManager.getObsLocBean().latDegProprety().bindBidirectional(latTextFormatter.valueProperty());

        obsPos.getChildren().addAll(lonLabel, lonTextField, latLabel, latTextField);

        return obsPos;
    }

    private HBox obsTime(SkyCanvasManager skyCanvasManager) {

        HBox obsTime = new HBox();
        obsTime.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
        obsTime.disableProperty().bind(timeAnimator.runningProperty());

        Label dateLabel = new Label("Date :");

        DatePicker datePicker = new DatePicker();
        datePicker.setStyle("-fx-pref-width: 120;");
        datePicker.setValue(skyCanvasManager.getDateTimeBean().getDate());

        skyCanvasManager.getDateTimeBean().dateProperty().bindBidirectional(datePicker.valueProperty());

        Label hourLabel = new Label("Heure :");

        TextField hourTextField = new TextField();
        hourTextField.setStyle("-fx-pref-width: 75; -fx-alignment: baseline-right;");

        DateTimeFormatter hmsFormatter =
                DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTimeStringConverter timeStringConverter =
                new LocalTimeStringConverter(hmsFormatter, hmsFormatter);
        TextFormatter<LocalTime> timeFormatter =
                new TextFormatter<>(timeStringConverter);

        hourTextField.setTextFormatter(timeFormatter);

        hourTextField.setText(LocalTime.of(skyCanvasManager.getDateTimeBean().getTime().getHour(),
                skyCanvasManager.getDateTimeBean().getTime().getMinute(),
                skyCanvasManager.getDateTimeBean().getTime().getSecond()).toString());

        skyCanvasManager.getDateTimeBean().timeProperty().bindBidirectional(timeFormatter.valueProperty());

        ComboBox zoneComboBox = new ComboBox();
        zoneComboBox.setStyle("-fx-pref-width: 180;");
        zoneComboBox.setVisibleRowCount(10);
        List<String> zoneList = new ArrayList<>(ZoneId.getAvailableZoneIds());
        Collections.sort(zoneList);
        List<String> zoneIdname = FXCollections.observableArrayList(zoneList);
        ObservableList<ZoneId> ZoneIdLIst = FXCollections.observableArrayList();
        for (String s : zoneIdname) {
            ZoneIdLIst.add(ZoneId.of(s));
        }
        zoneComboBox.setItems(ZoneIdLIst);

        zoneComboBox.setValue(skyCanvasManager.getDateTimeBean().getZone());
        skyCanvasManager.getDateTimeBean().zoneProperty().bindBidirectional(zoneComboBox.valueProperty());

        obsTime.getChildren().addAll(dateLabel, datePicker, hourLabel, hourTextField, zoneComboBox);

        return obsTime;
    }

    private HBox timeAcc(SkyCanvasManager skyCanvasManager) {

        HBox timeAcc = new HBox();
        timeAcc.setStyle("-fx-spacing: inherit;");

        InputStream fontStream = getClass()
                .getResourceAsStream("/Font Awesome 5 Free-Solid-900.otf");
        Font fontAwesome = Font.loadFont(fontStream, 15);

        ChoiceBox<NamedTimeAccelerator> accChoiceBox = new ChoiceBox<>();
        accChoiceBox.setItems(FXCollections.observableArrayList(NamedTimeAccelerator.values()));
        accChoiceBox.setValue(NamedTimeAccelerator.TIMES_300);
        accChoiceBox.disableProperty().bind(timeAnimator.runningProperty());

        timeAnimator.acceleratorProperty().bind(select(accChoiceBox.valueProperty(), "accelerator"));

        Button resetButton = new Button(RESET_STRING);
        resetButton.setFont(fontAwesome);
        resetButton.setOnAction(e -> {
            skyCanvasManager.getDateTimeBean().setZonedDateTime(ZonedDateTime.now());

        });
        resetButton.disableProperty().bind(timeAnimator.runningProperty());

        Button pauseplayButton = new Button(PLAY_STRING);
        pauseplayButton.setFont(fontAwesome);
        pauseplayButton.textProperty().bind(when(timeAnimator.runningProperty()).then(PAUSE_STRING).otherwise(PLAY_STRING));
        pauseplayButton.setOnAction(e -> {
            if (timeAnimator.getRunning().get()) {
                timeAnimator.stop();
            } else {
                timeAnimator.start();
            }
        });

        timeAcc.getChildren().addAll(accChoiceBox, resetButton, pauseplayButton);

        return timeAcc;
    }

    private HBox controlBar(SkyCanvasManager skyCanvasManager) {
        HBox controlBar = new HBox();
        controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");

        Separator scdSeprator = new Separator(Orientation.VERTICAL);
        Separator trdSeprator = new Separator(Orientation.VERTICAL);

        controlBar.getChildren().addAll(obsPos(skyCanvasManager), scdSeprator,
                obsTime(skyCanvasManager), trdSeprator,
                timeAcc(skyCanvasManager));

        return controlBar;
    }

    private BorderPane infoBar(SkyCanvasManager skyCanvasManager) {

        BorderPane infoBorderBar = new BorderPane();
        infoBorderBar.setStyle("-fx-padding: 4; -fx-background-color: white;");

        Text povText = new Text("Champ de vue : <fov>°");
        povText.textProperty().bind(Bindings.format("Champ de vue : %.1f°", skyCanvasManager.getViewParBean().fieldOfViewDegProperty()));

        Text objectUnderMouseText = new Text();
        objectUnderMouseText.textProperty().bind(Bindings.createStringBinding(() -> {
            if (skyCanvasManager.objectUnderMouse.get() != null) {
                return skyCanvasManager.objectUnderMouse.get().info();
            } else {
                return "";
            }
        }, skyCanvasManager.objectUnderMouse));

        Text azAltText = new Text("Azimut : <az>°, hauteur : <alt>°");
        azAltText.textProperty().bind(Bindings.format("Azimut : %.2f°, hauteur : %.2f°",
                skyCanvasManager.mouseAzDegProperty(), skyCanvasManager.mouseAltDegProperty()));

        infoBorderBar.setLeft(povText);
        infoBorderBar.setCenter(objectUnderMouseText);
        infoBorderBar.setRight(azAltText);

        return infoBorderBar;
    }

    private BorderPane furtherInfo(SkyCanvasManager skyCanvasManager) {

        BorderPane furtherInfoBorderBar = new BorderPane();
        furtherInfoBorderBar.setStyle("-fx-padding: 4; -fx-background-color: white;");
        furtherInfoBorderBar.setPrefSize(100, 200);

        return furtherInfoBorderBar;
    }

    private UnaryOperator<TextFormatter.Change> formattor(NumberStringConverter stringConverter) {

        UnaryOperator<TextFormatter.Change> filter = (change -> {
            try {
                String newText =
                        change.getControlNewText();
                double newLonDeg =
                        stringConverter.fromString(newText).doubleValue();
                return GeographicCoordinates.isValidLonDeg(newLonDeg)
                        ? change
                        : null;
            } catch (Exception e) {
                return null;
            }
        });
        return filter;
    }
}