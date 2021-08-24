package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.Preconditions.checkInInterval;
import static ch.epfl.rigel.math.Angle.TAU;
import static java.lang.Math.PI;

import java.util.Locale;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * Coordonnées équatoriales
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public final class EquatorialCoordinates extends SphericalCoordinates {

    /**
     * Intervalles couvert par l'ascension droite et la déclinaison
     */
    private static final RightOpenInterval INTERVAL_RA = RightOpenInterval.of(0,
            TAU);
    private static final ClosedInterval INTERVAL_DEC = ClosedInterval
            .symmetric(PI);

    /**
     * Constructeur Privé
     *
     * @param ra
     *            l'ascension droite
     * @param dec
     *            la déclinaison
     */
    private EquatorialCoordinates(double ra, double dec) {
        super(ra, dec);
    }

    /**
     * Méthode de construction de coordonnées equatorialle en radian Vérifie si
     * l'ascension droite et la déclinaison utilisées sont valides, dans leur
     * intervalle
     *
     * @param ra
     *            ascension droite en radian
     * @param dec
     *            déclinaison en radian
     * @return contruit les coordonnées en radian
     */
    public static EquatorialCoordinates of(double ra, double dec) {
        return new EquatorialCoordinates(checkInInterval(INTERVAL_RA, ra),
                checkInInterval(INTERVAL_DEC, dec));
    }

    /**
     * @return l'ascension droite en radians
     */
    public double ra() {
        return super.lon();
    }

    /**
     * @return l'ascension droite en degrès
     */
    public double raDeg() {
        return super.lonDeg();
    }

    /**
     * @return l'ascension droite en heure
     */
    public double raHr() {
        return Angle.toHr(ra());
    }

    /**
     * @return la déclinaison en radians
     */
    public double dec() {
        return super.lat();
    }

    /**
     * @return la déclinaison en degrés
     */
    public double decDeg() {
        return super.latDeg();
    }

    /**
     * @return l'ascension droite et la déclinaison sous forme de string
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(ra=%.4fh, dec=%.4f°)", this.raHr(),
                this.decDeg());
    }
}