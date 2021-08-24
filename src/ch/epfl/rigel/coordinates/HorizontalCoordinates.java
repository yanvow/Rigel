package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.Preconditions.checkInInterval;
import static ch.epfl.rigel.math.Angle.TAU;
import static java.lang.Math.PI;

import java.util.Locale;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * Coordonnées horizontales
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public final class HorizontalCoordinates extends SphericalCoordinates {

    /**
     * Intervalles couvert par l'azimut et la hauteur
     */
    private static final RightOpenInterval INTERVAL_AZ = RightOpenInterval.of(0,
            TAU);
    private static final ClosedInterval INTERVAL_ALT = ClosedInterval
            .symmetric(PI);

    /**
     * Intervalles cardinaux
     */
    private static final RightOpenInterval INTERVAL_N_1 = RightOpenInterval
            .of(13 * PI / 8, TAU);
    private static final RightOpenInterval INTERVAL_N_2 = RightOpenInterval
            .of(0, 3 * PI / 8);
    private static final RightOpenInterval INTERVAL_S = RightOpenInterval
            .of(5 * PI / 8, 11 * PI / 8);
    private static final RightOpenInterval INTERVAL_E = RightOpenInterval
            .of(PI / 8, 7 * PI / 8);
    private static final RightOpenInterval INTERVAL_W = RightOpenInterval
            .of(9 * PI / 8, 15 * PI / 8);

    private HorizontalCoordinates(double lon, double lat) {
        super(lon, lat);
    }

    /**
     * Méthode de construction de coordonnées horizontalles en radian
     *
     * @param az
     *            azimut
     * @param alt
     *            hauteur
     * @return contruit les coordonnés en radian
     */
    public static HorizontalCoordinates of(double az, double alt) {
        return new HorizontalCoordinates(checkInInterval(INTERVAL_AZ, az),
                checkInInterval(INTERVAL_ALT, alt));
    }

    /**
     * Méthode de construction de coordonnées horizontal en degrès
     *
     * @param azDeg
     * @param altDeg
     * @return contruit les coordonnés en radian
     */
    public static HorizontalCoordinates ofDeg(double azDeg, double altDeg) {
        double az = Angle.ofDeg(azDeg);
        double alt = Angle.ofDeg(altDeg);
        return new HorizontalCoordinates(checkInInterval(INTERVAL_AZ, az),
                checkInInterval(INTERVAL_ALT, alt));
    }

    /**
     * @return l'azimut en radian
     */
    public double az() {
        return lon();
    }

    /**
     * @return l'azimut en degrès
     */
    public double azDeg() {
        return lonDeg();
    }

    /**
     * Détermine le point cardinal de l'azimut des coordonnés horizontalles en
     * vérifiant s'il est contenu dans les 5 intervalles cardinaux
     *
     * @param n
     *            Nord
     * @param e
     *            Est
     * @param s
     *            Sud
     * @param w
     *            Ouest
     * @return le point cordinal
     */
    public String azOctantName(String n, String e, String s, String w) {

        double az = this.az();

        String OctanName = "";

        if (INTERVAL_N_1.contains(az) || INTERVAL_N_2.contains(az))
            OctanName += n;
        if (INTERVAL_S.contains(az))
            OctanName += s;
        if (INTERVAL_E.contains(az))
            OctanName += e;
        if (INTERVAL_W.contains(az))
            OctanName += w;
        return OctanName;
    }

    /**
     * @return la hauteur en radian
     */
    public double alt() {
        return lat();
    }

    /**
     * @return la hauteur en degrès
     */
    public double altDeg() {
        return latDeg();
    }

    /**
     * Calcule la distance angulaire entre le récepteur et le point donné en
     * argument
     *
     * @param that
     *            point avec lequel en veut meusurer la distance angulaire
     * @return la distance angulaire
     */
    public double angularDistanceTo(HorizontalCoordinates that) {
        double az1 = this.az();
        double az2 = that.az();
        double alt1 = this.alt();
        double alt2 = that.alt();

        return Math.acos(Math.sin(alt1) * Math.sin(alt2)
                + Math.cos(alt1) * Math.cos(alt2) * Math.cos(az1 - az2));
    }

    /**
     * @return l'azimut et la hauteur sous forme de string
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(az=%.4f°, alt=%.4f°)", azDeg(),
                altDeg());
    }
}