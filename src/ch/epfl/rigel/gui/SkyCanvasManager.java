package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;


/**
 * Gestionnaire de canevas du ciel
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public class SkyCanvasManager {

    StarCatalogue starCatalogue;

    private final static double STEP_AZ = Angle.ofDeg(10);
    private final static ClosedInterval INTERVAL_FOV = ClosedInterval.of(30, 150);

    private final static double STEP_ALT = Angle.ofDeg(5);
    private final static ClosedInterval INTERVAL_ALT = ClosedInterval.of(Angle.ofDeg(5), Angle.TAU / 4);

    private final static double DIST_MAX = 10;

    private Canvas canvas;

    private ObjectProperty<DateTimeBean> dateTimeBean = new SimpleObjectProperty<>();
    private ObjectProperty<ObserverLocationBean> obsLocBean = new SimpleObjectProperty<>();
    private ObjectProperty<ViewingParametersBean> viewParBean = new SimpleObjectProperty<>();

    private ObjectProperty<CartesianCoordinates> mousePosition =
            new SimpleObjectProperty<>(CartesianCoordinates.of(0, 0));

    public final ObjectBinding<Double> mouseAzDeg;
    public final ObjectBinding<Double> mouseAltDeg;
    public final ObjectBinding<CelestialObject> objectUnderMouse;

    private final ObjectBinding<SkyCanvasPainter> skyCanvasPainter;

    private final ObjectBinding<StereographicProjection> projection;
    private final ObjectBinding<Transform> planeToCanvas;
    private final ObjectBinding<ObservedSky> observedSky;
    private final ObjectBinding<HorizontalCoordinates> mouseHorizontalPosition;

    /**
     * Constructeur
     *
     * @param starCatalogue catalogue d'étoiles
     * @param dateTimeBean  Bean de la Date et du Temps
     * @param obsLocBean    Bean de la position d'observation
     * @param viewParBean   Bean des paramètres de vue
     */
    public SkyCanvasManager(StarCatalogue starCatalogue, DateTimeBean dateTimeBean,
                            ObserverLocationBean obsLocBean, ViewingParametersBean viewParBean) {
        this.starCatalogue = starCatalogue;
        setDateTimeBean(dateTimeBean);
        setObsLocBean(obsLocBean);
        setViewParBean(viewParBean);

        canvas = new Canvas(800, 600);

        skyCanvasPainter = Bindings.createObjectBinding(()
                -> new SkyCanvasPainter(canvas), canvas.heightProperty(), canvas.widthProperty());

        projection = Bindings.createObjectBinding(()
                -> new StereographicProjection(viewParBean.getCenter()), viewParBean.centerProperty());

        observedSky = Bindings.createObjectBinding(()
                        -> new ObservedSky(dateTimeBean.getZonedDateTime(), obsLocBean.getCoordinates(),
                        projection.get(), starCatalogue),
                dateTimeBean.dateProperty(), dateTimeBean.zoneProperty(), dateTimeBean.timeProperty(),
                obsLocBean.coordinatesProperty(), projection);

        planeToCanvas = Bindings.createObjectBinding(()
                        -> Transform.affine(expansionFactor(), 0, 0, -expansionFactor(),
                canvas.getWidth() / 2, canvas.getHeight() / 2), canvas.widthProperty(), canvas.heightProperty(),
                viewParBean.fieldOfViewDegProperty());


        //listener sur obs et plane -> draw
        observedSky.addListener((p, o, n) -> draw());
        planeToCanvas.addListener((p, o, n) -> draw());
        projection.addListener((p, o, n) -> draw());


        //pression des touches du clavier
        canvas.setOnKeyPressed(e -> {
            canvas.requestFocus();

            HorizontalCoordinates currentCoord = getViewParBean().getCenter();
            HorizontalCoordinates nextCoord = currentCoord;

            switch (e.getCode()) {
                case RIGHT:
                    nextCoord = HorizontalCoordinates.
                            of(Angle.normalizePositive(currentCoord.az() + STEP_AZ), currentCoord.alt());
                    break;
                case LEFT:
                    nextCoord = HorizontalCoordinates.
                            of(Angle.normalizePositive(currentCoord.az() - STEP_AZ), currentCoord.alt());
                    break;
                case DOWN:
                    nextCoord = HorizontalCoordinates.of(currentCoord.az(),
                            INTERVAL_ALT.clip(currentCoord.alt() - STEP_ALT));
                    break;
                case UP:
                    nextCoord = HorizontalCoordinates.of(currentCoord.az(),
                            INTERVAL_ALT.clip(currentCoord.alt() + STEP_ALT));
                    break;
            }
            getViewParBean().setCenter(nextCoord);
            e.consume();
        });

        //molette de la souris
        canvas.setOnScroll(e -> {

            double deltaFOV = 0;

            double currentFOV = getViewParBean().getFieldOfViewDeg();

            if (Math.abs(e.getDeltaX()) > Math.abs(e.getDeltaY())) {
                deltaFOV = e.getDeltaX();
            } else if (Math.abs(e.getDeltaY()) > Math.abs(e.getDeltaX())) {
                deltaFOV = e.getDeltaY();
            } else {
                deltaFOV = e.getDeltaX();
            }

            getViewParBean().setFieldOfViewDeg(INTERVAL_FOV.clip(currentFOV + deltaFOV / 8));
        });

        //clics de la souris sur le canevas
        canvas.setOnMousePressed(e -> {
            if (e.isPrimaryButtonDown()) {
                canvas.requestFocus();
            }
        });

        //mouvements du curseur de la souris
        canvas.setOnMouseMoved(e -> {
            try {
                Point2D pnt = planeToCanvas.get().inverseTransform(e.getX(), e.getY());
                setMousePosition(CartesianCoordinates.of(pnt.getX(), pnt.getY()));
            } catch (NonInvertibleTransformException ex) {
                ex.printStackTrace();
            }
        });

        ObjectBinding<Double> distanceMax = Bindings.createObjectBinding(() -> {
            double value;
            try {
                value = planeToCanvas.get().inverseDeltaTransform(DIST_MAX, 0).magnitude();
            } catch (NonInvertibleTransformException e) {
                value = 0;
            }
            return value;
        }, planeToCanvas);


        objectUnderMouse = Bindings.createObjectBinding(() -> {
            if (observedSky.get().objectClosestTo(getMousePosition(), distanceMax.get()).isPresent()) {
                return observedSky.get().objectClosestTo(getMousePosition(), distanceMax.get()).get();
            } else {
                return null;
            }
        }, mousePositionProperty(), observedSky, planeToCanvas, distanceMax);

        mouseHorizontalPosition = Bindings.createObjectBinding(() ->
                        projection.get().inverseApply(getMousePosition()),
                mousePositionProperty(), planeToCanvas);

        mouseAzDeg = Bindings.createObjectBinding(() ->
                        mouseHorizontalPosition.get().azDeg(),
                mouseHorizontalPosition);

        mouseAltDeg = Bindings.createObjectBinding(() ->
                        mouseHorizontalPosition.get().altDeg(),
                mouseHorizontalPosition);

    }

    private void draw() {
        skyCanvasPainter.get().clear(observedSky.get(), projection.get());
        skyCanvasPainter.get().drawBoundaries(observedSky.get(), projection.get(), planeToCanvas.get());
        skyCanvasPainter.get().drawSunTrace(observedSky.get(), projection.get(), planeToCanvas.get());
        skyCanvasPainter.get().drawMoonTrace(observedSky.get(), projection.get(), planeToCanvas.get());
        skyCanvasPainter.get().drawStars(observedSky.get(), projection.get(), planeToCanvas.get());
        skyCanvasPainter.get().drawPlanets(observedSky.get(), projection.get(), planeToCanvas.get());
        skyCanvasPainter.get().drawSun(observedSky.get(), projection.get(), planeToCanvas.get());
        skyCanvasPainter.get().drawMoon(observedSky.get(), projection.get(), planeToCanvas.get());
        skyCanvasPainter.get().drawHorizon(observedSky.get(), projection.get(), planeToCanvas.get());
        skyCanvasPainter.get().drawParrallels(observedSky.get(), projection.get(), planeToCanvas.get());
    }

    private double expansionFactor() {
        return canvas.getWidth() / projection.get().applyToAngle(Angle.ofDeg(getViewParBean().getFieldOfViewDeg()));
    }

    /**
     * @return
     */
    public DateTimeBean getDateTimeBean() {
        return dateTimeBean.get();
    }

    /**
     * @return
     */
    public ObjectProperty<DateTimeBean> dateTimeBeanProperty() {
        return dateTimeBean;
    }

    /**
     * @param dateTime
     */
    public void setDateTimeBean(DateTimeBean dateTime) {
        this.dateTimeBean.set(dateTime);
    }

    /**
     * @return
     */
    public ObserverLocationBean getObsLocBean() {
        return obsLocBean.get();
    }

    /**
     * @return
     */
    public ObjectProperty<ObserverLocationBean> obsLocBeanProperty() {
        return obsLocBean;
    }

    /**
     * @param obsLocBean
     */
    public void setObsLocBean(ObserverLocationBean obsLocBean) {
        this.obsLocBean.set(obsLocBean);
    }

    /**
     * @return
     */
    public ViewingParametersBean getViewParBean() {
        return viewParBean.get();
    }

    /**
     * @return
     */
    public ObjectProperty<ViewingParametersBean> viewParBeanProperty() {
        return viewParBean;
    }

    /**
     * @param viewParBean
     */
    public void setViewParBean(ViewingParametersBean viewParBean) {
        this.viewParBean.set(viewParBean);
    }

    /**
     * @return
     */
    public CartesianCoordinates getMousePosition() {
        return mousePosition.get();
    }

    /**
     * @return
     */
    public ObjectProperty<CartesianCoordinates> mousePositionProperty() {
        return mousePosition;
    }

    /**
     * @param mousePosition
     */
    public void setMousePosition(CartesianCoordinates mousePosition) {
        this.mousePosition.set(mousePosition);
    }

    /**
     * @return
     */
    public Binding<Double> mouseAzDegProperty() {
        return mouseAzDeg;
    }

    /**
     * @return
     */
    public Binding<Double> mouseAltDegProperty() {
        return mouseAltDeg;
    }

    /**
     * @return
     */
    public Binding<CelestialObject> objectUnderMouseProperty() {
        return objectUnderMouse;
    }

    /**
     * @return
     */
    public ObservedSky getObservedSky() {
        return observedSky.get();
    }

    /**
     * @return
     */
    public ObjectBinding<ObservedSky> observedSkyProperty() {
        return observedSky;
    }

    /**
     * @return
     */
    public Canvas canvas() {
        return canvas;
    }
}