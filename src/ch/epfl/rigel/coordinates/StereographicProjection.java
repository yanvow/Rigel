package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.math.Angle.normalizePositive;
import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;

import java.util.Locale;
import java.util.function.Function;

/**
 * Projection stéréographique
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public final class StereographicProjection
        implements Function<HorizontalCoordinates, CartesianCoordinates> {

    private double azCenter;
    private double altCenter;
    private double cosAltCenter;
    private double sinAltCenter;

    /**
     * Constructeur
     *
     * @param center
     *            coordonnées horizontales du centre
     */
    public StereographicProjection(HorizontalCoordinates center) {
        azCenter = center.az();
        altCenter = center.lat();
        cosAltCenter = cos(altCenter);
        sinAltCenter = sin(altCenter);
    }

    /**
     * Calcule les coordonnées du centre du cercle correspondant à la projection
     * du parallèle passant par le point hor donné
     *
     * @param hor
     *            cordonnées horizontales
     * @return les coordonnées cartésiennes du centre du cercle
     */
    public CartesianCoordinates circleCenterForParallel(
            HorizontalCoordinates hor) {
        double lat = hor.lat();
        double y = cosAltCenter / (sin(lat) + sinAltCenter);
        return CartesianCoordinates.of(0, y);
    }

    public CartesianCoordinates circleCenterForMeridian(
            HorizontalCoordinates hor) {
        double lon = hor.lon();
        double x = cos(azCenter) / (sin(lon) + sin(azCenter));
        return CartesianCoordinates.of(x, 0);
    }

    /**
     * Calcule le rayon du cercle correspondant à la projection du parallèle
     * passant par le point de coordonnées hor donné
     *
     * @param parallel
     *            cordonnées horizontales
     * @return le rayon du cercle
     */
    public double circleRadiusForParallel(HorizontalCoordinates parallel) {
        double lat = parallel.lat();
        return cos(lat) / (sin(lat) + sinAltCenter);
    }

    public double circleRadiusForMeridian(HorizontalCoordinates meridian) {
        double lon = meridian.lon();
        return cos(lon) / (sin(lon) + sin(azCenter));
    }

    /**
     * Calucle le diamètre projeté d'une sphère de taille angulaire donné
     * centrée au centre de projection
     *
     * @param rad
     *            taille angulaire
     * @return le diamètre
     */
    public double applyToAngle(double rad) {
        return 2 * tan(rad / 4);
    }

    /**
     * Calule les coordonnées horizontales du point dont la projection est le
     * point de coordonnées cartésiennes donné
     *
     * @param xy
     *            coordonnées cartésiennes
     * @return les coordonnées horizontales
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy) {

        double x = xy.x();
        double y = xy.y();

        double r = sqrt(pow(x, 2) + pow(y, 2));

        double powR2 = pow(r, 2);

        double sinc = (2 * r) / (powR2 + 1);
        double cosc = (1 - powR2) / (powR2 + 1);

        double lambda, phi;

        if (x == 0 && y == 0) {
            lambda = azCenter;
            phi = altCenter;

        }
        else {

            lambda = atan2(x * sinc,
                    r * cosAltCenter * cosc - y * sinAltCenter * sinc)
                    + azCenter;
            phi = asin(cosc * sinAltCenter + (y * sinc * cosAltCenter) / r);
        }

        return HorizontalCoordinates.of(normalizePositive(lambda), phi);
    }

    /**
     * Calcule les coordonnées cartésiennes de la project donné
     *
     * @param azAlt
     *            coordonnée horizontales
     * @return les coordonnées cartésiennes
     */
    @Override
    public CartesianCoordinates apply(HorizontalCoordinates azAlt) {

        double alt = azAlt.alt();
        double az = azAlt.az();
        double cosAlt = cos(alt);
        double sinAlt = sin(alt);

        double lambdaDelta = az - azCenter;
        double d = 1 / (1 + (sinAlt * sinAltCenter
                + cosAlt * cosAltCenter * cos(lambdaDelta)));
        double x = d * cosAlt * sin(lambdaDelta);
        double y = d * (sinAlt * cosAltCenter
                - cosAlt * sinAltCenter * cos(lambdaDelta));

        return CartesianCoordinates.of(x, y);
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "StereographicProjection :\n az : %s, alt : %s", azCenter,
                altCenter);
    }
}