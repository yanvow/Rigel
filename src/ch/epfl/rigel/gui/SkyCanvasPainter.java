package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;

import java.util.List;
import java.util.Set;

/**
 * Peintre de canevas du ciel
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public class SkyCanvasPainter {

    public Canvas canvas;
    public GraphicsContext ctx;

    private double i = 0;
    private boolean sunset;
    private boolean animation;
    private HorizontalCoordinates savedSunHor;
    private boolean first = true;
    private LinearGradient linear;

    private static final int PAS_PARALLEL_DEGREE = 15;

    /**
     * Constructeur
     *
     * @param canvas Canvas
     */
    public SkyCanvasPainter(Canvas canvas) {
        this.canvas = canvas;
        this.ctx = canvas.getGraphicsContext2D();

        ctx.setFill(Color.BLACK);
        ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Nettoye le canvas
     */
    public void clear(ObservedSky sky, StereographicProjection projection) {
        drawSky(sky, projection);
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawSky(ObservedSky sky, StereographicProjection projection) {

        if (first) {
            savedSunHor = projection.inverseApply(sky.sunPosition());
            animation = true;
            first = false;
            sunset = true;
        } else {

            if (Math.abs(savedSunHor.alt() - projection
                    .inverseApply(sky.sunPosition()).alt()) <= 0.000001) {
                animation = false;

            } else if (Math.abs(savedSunHor.alt() - projection
                    .inverseApply(sky.sunPosition()).alt()) >= 0.001) {
                sunset = savedSunHor.alt() - projection.inverseApply(sky.sunPosition()).alt() > 0;

                animation = true;
            } else {
                sunset = savedSunHor.alt() - projection.inverseApply(sky.sunPosition()).alt() > 0;

                animation = false;
            }
        }
        HorizontalCoordinates hor = projection.inverseApply(sky.sunPosition());

        // Sunset
        Stop[] stopsSunset = new Stop[]{new Stop(0, Color.RED),
                new Stop(0.4, Color.ORANGE), new Stop(1, Color.MIDNIGHTBLUE)};

        // Sunrise
        Stop[] stopsSunrise = new Stop[]{new Stop(0, Color.ROYALBLUE),
                new Stop(0.6, Color.ORANGE), new Stop(1, Color.INDIANRED)};

        if (hor.altDeg() < 0 && hor.altDeg() > -6 && sunset && animation) {
            savedSunHor = projection.inverseApply(sky.sunPosition());

            linear = new LinearGradient(0, 1, 0, i, true,
                    CycleMethod.NO_CYCLE, stopsSunset);
            if (i <= 0.6) {
                i += 0.008;
            }

        } else if (hor.altDeg() < 0 && hor.altDeg() > -6 && !sunset && animation) {
            savedSunHor = projection.inverseApply(sky.sunPosition());
            linear = new LinearGradient(0, 1, 0, i, true,
                    CycleMethod.NO_CYCLE, stopsSunrise);
            if (i >= -0.5) {
                i -= 0.08;
            }
        }

        if (hor.altDeg() >= 0) {
            i = 0;
            ctx.setFill(Color.ROYALBLUE);

        } else if (hor.altDeg() < 0 && hor.altDeg() > -6) {
            ctx.setFill(linear);
        } else {
            i = 0;
            ctx.setFill(Color.BLACK);
        }
    }

    private double diametre(double magnitude, StereographicProjection projection) {

        double magnCliped;
        if (magnitude <= -2) {
            magnCliped = -2;
        } else if (magnitude >= 5) {
            magnCliped = 5;
        } else {
            magnCliped = magnitude;
        }

        double sizeFactor = (99 - 17 * magnCliped) / 140;

        double diametre = sizeFactor * projection.applyToAngle(Angle.ofDeg(.5));

        return diametre;
    }

    /**
     * Dessine les étoiles  et les asterismes sur le canvas
     *
     * @param sky        ciel observé
     * @param projection projection stéreographique
     * @param transform  transform
     */
    public void drawStars(ObservedSky sky, StereographicProjection projection, Transform transform) {

        Bounds bds = canvas.getBoundsInLocal();

        List<Star> starList = sky.stars();
        Set<Asterism> asterismSet = sky.asterismSet();
        double[] starPostions = sky.starPosition();

        for (Asterism asterism : asterismSet) {
            List<Integer> list = sky.asterismIndices(asterism);
            for (int j = 0; j < list.size() - 1; j++) {
                Point2D ptn = transform.
                        transform(starPostions[2 * list.get(j)], starPostions[2 * list.get(j) + 1]);
                Point2D ptn2 = transform.
                        transform(starPostions[2 * list.get(j + 1)], starPostions[2 * list.get(j + 1) + 1]);
                if (bds.contains(ptn.getX(), ptn.getY()) || bds.contains(ptn2.getX(), ptn2.getY())) {
                    ctx.beginPath();
                    ctx.moveTo(ptn.getX(), ptn.getY());
                    ctx.lineTo(ptn2.getX(), ptn2.getY());
                    ctx.setStroke(Color.BLUE);
                    ctx.setLineWidth(1);
                    ctx.stroke();
                }
            }
        }

        int i = 0;
        for (Star star : starList) {
            ctx.setFill(BlackBodyColor.colorForTemperature(star.colorTemperature()));
            double diametre = diametre(star.magnitude(), projection);
            double r = transform.deltaTransform(0, diametre).magnitude();
            Point2D ptn = transform.transform(starPostions[i], starPostions[i + 1]);
            ctx.fillOval(ptn.getX() - r / 2, ptn.getY() - r / 2, r, r);
            if (star.magnitude() < 1.5) {
                String name = star.name();
                ctx.setFill(Color.WHITE);
                ctx.setTextBaseline(VPos.TOP);
                ctx.setTextAlign(TextAlignment.CENTER);
                ctx.fillText(name, ptn.getX(),
                        ptn.getY());
            }
            i += 2;
        }
    }

    /**
     * Dessine les planettes
     *
     * @param sky        ciel observé
     * @param projection projection stéreographique
     * @param transform  transform
     */
    public void drawPlanets(ObservedSky sky, StereographicProjection projection, Transform transform) {
        int i = 0;
        List<Planet> planetList = sky.planets();
        double[] planetPostions = sky.planetPosition();

        for (Planet planet : planetList) {
            ctx.setFill(Color.LIGHTGRAY);
            double diametre = diametre(planet.magnitude(), projection);
            double r = transform.deltaTransform(0, diametre).magnitude();
            Point2D ptn = transform.transform(planetPostions[i], planetPostions[i + 1]);
            ctx.fillOval(ptn.getX() - r / 2, ptn.getY() - r / 2, r, r);
            i += 2;
        }
    }

    /**
     * Dessine le soleil
     *
     * @param sky        ciel observé
     * @param projection projection stéreographique
     * @param transform  transform
     */
    public void drawSun(ObservedSky sky, StereographicProjection projection, Transform transform) {

        Sun sun = sky.sun();
        double diametre = diametre(sun.magnitude(), projection);
        double r = transform.deltaTransform(0, diametre).magnitude();
        Point2D ptn = transform.transform(sky.sunPosition().x(), sky.sunPosition().y());

        ctx.setFill(Color.YELLOW.deriveColor(1, 1, 1, 0.25));
        double d = r * 2.2;
        ctx.fillOval(ptn.getX() - d / 2, ptn.getY() - d / 2, d, d);

        ctx.setFill(Color.YELLOW);
        double di = r + 2;
        ctx.fillOval(ptn.getX() - di / 2, ptn.getY() - di / 2, di, di);

        ctx.setFill(Color.WHITE);
        ctx.fillOval(ptn.getX() - r / 2, ptn.getY() - r / 2, r, r);
    }

    /**
     * Dessine la trace du soleil
     *
     * @param sky        ciel observé
     * @param projection projection stéreographique
     * @param transform  transform
     */
    public void drawSunTrace(ObservedSky sky, StereographicProjection projection, Transform transform) {
        Bounds bds = canvas.getBoundsInLocal();

        CartesianCoordinates[] sunTracePositions = sky.sunTracePosition();

        for (int j = 0; j < sunTracePositions.length - 2; j += 2) {
            Point2D ptn = transform.
                    transform(sunTracePositions[j].x(), sunTracePositions[j].y());
            Point2D ptn2 = transform.
                    transform(sunTracePositions[j + 1].x(), sunTracePositions[j + 1].y());
            if (bds.contains(ptn.getX(), ptn.getY()) || bds.contains(ptn2.getX(), ptn2.getY())) {
                ctx.beginPath();
                ctx.moveTo(ptn.getX(), ptn.getY());
                ctx.lineTo(ptn2.getX(), ptn2.getY());
                ctx.setStroke(Color.ORANGE);
                ctx.setLineWidth(1);
                ctx.stroke();
            }
        }
    }

    /**
     * Dessine la lune
     *
     * @param sky        ciel observé
     * @param projection projection stéreographique
     * @param transform  transform
     */
    public void drawMoon(ObservedSky sky, StereographicProjection projection, Transform transform) {

        Moon moon = sky.moon();
        float moonPhase = moon.getPhase();
        double diametre = diametre(moon.magnitude(), projection);
        double r = transform.deltaTransform(0, diametre).magnitude() * 50;
        Point2D ptn = transform.transform(sky.moonPosition().x(), sky.moonPosition().y());


        if (moonPhase == 0) {
            ctx.setFill(Color.BLACK);
            ctx.fillOval(ptn.getX() - r / 2, ptn.getY() - r / 2, r, r);
        } else if (moonPhase > .5) {
            ctx.setFill(Color.BLACK);
            ctx.fillOval(ptn.getX() - r / 2, ptn.getY() - r / 2, r, r);
            ctx.setFill(Color.WHITE);
            ctx.fillArc(ptn.getX() - r / 2, ptn.getY() - r / 2, r, r, 90, 180, ArcType.ROUND);
            double ri = r * (moonPhase - .5) * 2;
            ctx.fillArc(ptn.getX() - ri / 2, ptn.getY() - r / 2, ri, r, -90, 180, ArcType.ROUND);

        } else if (moonPhase <= .5) {
            ctx.setFill(Color.WHITE);
            ctx.fillOval(ptn.getX() - r / 2, ptn.getY() - r / 2, r, r);
            ctx.setFill(Color.BLACK);
            ctx.fillArc(ptn.getX() - r / 2, ptn.getY() - r / 2, r, r, -90, 180, ArcType.ROUND);
            double ri = r * (.5 - moonPhase) * 2;
            ctx.fillArc(ptn.getX() - ri / 2, ptn.getY() - r / 2, ri, r, 90, 180, ArcType.ROUND);
        }
    }

    /**
     * Dessine la trace de la lune
     *
     * @param sky        ciel observé
     * @param projection projection stéreographique
     * @param transform  transform
     */
    public void drawMoonTrace(ObservedSky sky, StereographicProjection projection, Transform transform) {
        Bounds bds = canvas.getBoundsInLocal();

        CartesianCoordinates[] moonTracePositions = sky.moonTracePosition();

        for (int j = 0; j < moonTracePositions.length - 2; j += 2) {
            Point2D ptn = transform.
                    transform(moonTracePositions[j].x(), moonTracePositions[j].y());
            Point2D ptn2 = transform.
                    transform(moonTracePositions[j + 1].x(), moonTracePositions[j + 1].y());
            if (bds.contains(ptn.getX(), ptn.getY()) || bds.contains(ptn2.getX(), ptn2.getY())) {
                ctx.beginPath();
                ctx.moveTo(ptn.getX(), ptn.getY());
                ctx.lineTo(ptn2.getX(), ptn2.getY());
                ctx.setStroke(Color.WHITE);
                ctx.setLineWidth(1);
                ctx.stroke();
            }
        }
    }

    /**
     * Dessine l'horizon
     *
     * @param sky        ciel observé
     * @param projection projection stéreographique
     * @param transform  transform
     */
    public void drawHorizon(ObservedSky sky, StereographicProjection projection, Transform transform) {

        HorizontalCoordinates horizontalCoordinates = HorizontalCoordinates.ofDeg(0, 0);

        CartesianCoordinates proj = projection.circleCenterForParallel(horizontalCoordinates);

        Point2D center = transform.transform(proj.x(), proj.y());

        double rad = projection.circleRadiusForParallel(horizontalCoordinates);

        double radius = transform.deltaTransform(0, rad).magnitude() * 2;

        ctx.setStroke(Color.RED);
        ctx.setLineWidth(2);
        ctx.strokeOval(center.getX() - radius / 2, center.getY() - radius / 2, radius, radius);

        for (int i = 0; i < 360; i += 45) {
            HorizontalCoordinates coordinates = HorizontalCoordinates.ofDeg(i, -0.5);
            CartesianCoordinates coordinatesPrime = projection.apply(coordinates);
            Point2D coordiantesSecond = transform.transform(coordinatesPrime.x(), coordinatesPrime.y());
            String name = coordinates.azOctantName("N", "E", "S", "O");
            ctx.setFill(Color.RED);
            ctx.setTextBaseline(VPos.TOP);
            ctx.setTextAlign(TextAlignment.CENTER);
            ctx.fillText(name, coordiantesSecond.getX(), coordiantesSecond.getY());
        }
    }

    /**
     * Dessine les parrallèles
     *
     * @param sky        ciel observé
     * @param projection projection stéreographique
     * @param transform  transform
     */
    public void drawParrallels(ObservedSky sky, StereographicProjection projection, Transform transform) {

        for (int i = -30; i < 90; i += PAS_PARALLEL_DEGREE) {
            if (i != 0) {

                HorizontalCoordinates horizontalCoordinates = HorizontalCoordinates.ofDeg(0, i);

                CartesianCoordinates proj = projection.circleCenterForParallel(horizontalCoordinates);

                Point2D centre = transform.transform(proj.x(), proj.y());

                double rad = projection.circleRadiusForParallel(horizontalCoordinates);

                double radius = transform.deltaTransform(0, rad).magnitude() * 2;

                ctx.setStroke(Color.LIGHTGRAY);
                ctx.setLineWidth(1);
                ctx.strokeOval(centre.getX() - radius / 2, centre.getY() - radius / 2, radius, radius);
            }
        }
    }

    /**
     * Dessine les limites des constellations
     *
     * @param sky        ciel observé
     * @param projection projection stéreographique
     * @param transform  transform
     */
    public void drawBoundaries(ObservedSky sky, StereographicProjection projection, Transform transform) {

        Bounds bds = canvas.getBoundsInLocal();

        Set<Boundaries> boundariesSet = sky.boundariesSet();

        for (Boundaries boundaries : boundariesSet) {

            CartesianCoordinates[] boundPostions = sky.bundariesPosition(boundaries);

            for (int j = 0; j < boundPostions.length - 2; j += 1) {
                Point2D ptn = transform.
                        transform(boundPostions[j].x(), boundPostions[j].y());
                Point2D ptn2 = transform.
                        transform(boundPostions[j + 1].x(), boundPostions[j + 1].y());
                if (bds.contains(ptn.getX(), ptn.getY()) || bds.contains(ptn2.getX(), ptn2.getY())) {
                    ctx.beginPath();
                    ctx.moveTo(ptn.getX(), ptn.getY());
                    ctx.lineTo(ptn2.getX(), ptn2.getY());
                    ctx.setStroke(Color.GRAY);
                    ctx.setLineWidth(.5);
                    ctx.stroke();
                }
            }
        }
    }
}