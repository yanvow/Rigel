package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.math.Angle.ofArcsec;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.tan;

import java.time.ZonedDateTime;
import java.util.function.Function;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

/**
 * représentant un changement de système de coordonnées depuis les coordonnées
 * écliptiques vers les coordonnées équatoriales, à un instant donné.
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public final class EclipticToEquatorialConversion
        implements Function<EclipticCoordinates, EquatorialCoordinates> {

    private final double cosObli;
    private final double sinObli;
    private static final Polynomial epsilon = Polynomial.of(ofArcsec(0.00181),
            ofArcsec(-0.0006), ofArcsec(-46.815), Angle.ofDMS(23, 26, 21.45));

    /**
     * construit un changement de système de coordonnées entre les coordonnées
     * écliptiques et les coordonnées équatoriales
     * 
     * @param when
     *            date/heure
     */
    public EclipticToEquatorialConversion(ZonedDateTime when) {
        double T = Epoch.J2000.julianCenturiesUntil(when);
        double obliquite = epsilon.at(T);
        this.cosObli = cos(obliquite);
        this.sinObli = sin(obliquite);
    }

    /**
     * retourne les coordonnées équatoriales correspondant aux coordonnées
     * écliptiques
     * 
     * @param ecl
     *            coordonnées écliptiques
     * @return des coordonnées équatoriales en radian
     */
    @Override
    public EquatorialCoordinates apply(EclipticCoordinates ecl) {
        double λ = ecl.lon();
        double β = ecl.lat();
        double sinλ = sin(λ);

        double α = Angle.normalizePositive(
                Math.atan2(sinλ * cosObli - tan(β) * sinObli, cos(λ)));
        double δ = Math.asin(sin(β) * cosObli + cos(β) * sinObli * sinλ);

        return EquatorialCoordinates.of(α, δ);
    }

    /**
     * @throws UnsupportedOperationException
     */
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    /**
     * @throws UnsupportedOperationException
     */
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}