package ch.epfl.rigel.coordinates;

import static java.lang.Math.PI;

import java.util.Locale;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * Coordonnées géographiques
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public final class GeographicCoordinates extends SphericalCoordinates {

    /**
     * Intervalles couvert par la longitude et la latitude
     */
    private static final RightOpenInterval INTERVAL_LON = RightOpenInterval
            .of(-PI, PI);
    private static final ClosedInterval INTERVAL_LAT = ClosedInterval
            .symmetric(PI);

    /**
     * Constructeur Privé
     *
     * @param longitude
     *            longitude
     * @param latitude
     *            latitude
     */
    private GeographicCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }

    /**
     * Méthode de construction de coordonnées geographiques en degrés
     *
     * @param lonDeg
     *            longitude en degrés
     * @param latDeg
     *            latitude en degrés
     * @return contruit les coordonnées en radian
     */
    public static GeographicCoordinates ofDeg(double lonDeg, double latDeg) {
        Preconditions
                .checkArgument(isValidLatDeg(latDeg) && isValidLonDeg(lonDeg));
        double lon = Angle.ofDeg(lonDeg);
        double lat = Angle.ofDeg(latDeg);
        return new GeographicCoordinates(lon, lat);
    }

    /**
     * Vérifie si la longitude utilisée est valide, dans l'intervalle
     *
     * @param lonDeg
     *            longitude en degrés
     * @return validité
     */
    public static boolean isValidLonDeg(double lonDeg) {
        return INTERVAL_LON.contains(Angle.ofDeg(lonDeg));
    }

    /**
     * Vérifie si la latitude utilisée est valide, dans l'intervalle
     *
     * @param latDeg
     *            latitude en degrés
     * @return validité
     */
    public static boolean isValidLatDeg(double latDeg) {
        return INTERVAL_LAT.contains(Angle.ofDeg(latDeg));
    }

    /**
     * @return la longitude en radian
     */
    @Override
    public double lon() {
        return super.lon();
    }

    /**
     * @return la longitude en degrés
     */
    @Override
    public double lonDeg() {
        return super.lonDeg();
    }

    /**
     * @return la latidude en radian
     */
    @Override
    public double lat() {
        return super.lat();
    }

    /**
     * @return la latidude en degrés
     */
    @Override
    public double latDeg() {
        return super.latDeg();
    }

    /**
     * @return la longitude et la latitude sous forme de string
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(lon=%.4f°, lat=%.4f°)",
                this.lonDeg(), this.latDeg());
    }
}