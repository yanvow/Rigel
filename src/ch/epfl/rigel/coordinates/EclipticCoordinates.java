package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.Preconditions.checkInInterval;
import static ch.epfl.rigel.math.Angle.TAU;
import static java.lang.Math.PI;

import java.util.Locale;

import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * Coordonnées écliptiques
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public final class EclipticCoordinates extends SphericalCoordinates {

    /**
     * Intervalles couvert par la longitude écliptique et la latitude écliptique
     */
    private static final RightOpenInterval INTERVAL_LON = RightOpenInterval
            .of(0, TAU);
    private static final ClosedInterval INTERVAL_LAT = ClosedInterval
            .symmetric(PI);

    /**
     * Constructeur
     *
     * @param lon
     *            longitude écliptique
     * @param lat
     *            latitude écliptique
     */
    private EclipticCoordinates(double lon, double lat) {
        super(lon, lat);
    }

    /**
     * Méthode de construction de coordonnées écliptique en radian Vérifie si la
     * longitude écliptique et la latitude écliptique sont valides, dans leur
     * intervalle
     *
     * @param lon
     *            longitude écliptique
     * @param lat
     *            latitude écliptique
     * @return contruit les coordonnées en radian
     */
    public static EclipticCoordinates of(double lon, double lat) {
        return new EclipticCoordinates(checkInInterval(INTERVAL_LON, lon),
                checkInInterval(INTERVAL_LAT, lat));
    }

    /**
     *
     * @return la longitude en radians
     */
    public double lon() {
        return super.lon();
    }

    /**
     *
     * @return la longitude en degres
     */
    public double lonDeg() {
        return super.lonDeg();
    }

    /**
     *
     * @return la latitude en radians
     */
    public double lat() {
        return super.lat();
    }

    /**
     *
     * @return la latitude en degres
     */
    public double latDeg() {
        return super.latDeg();
    }

    /**
     * @return la longitude écliptique et la latitude écliptique sous forme de
     *         string
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(λ=%.4f°, β=%.4f°)", this.lonDeg(),
                this.latDeg());
    }
}