package ch.epfl.rigel.coordinates;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.time.ZonedDateTime;
import java.util.function.Function;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;

/**
 * classe représentant un changement de systèmes de coordonnées depuis les
 * coordonnées équatoriales vers les coordonnées écliptiques, à un instant et
 * pour un lieu donnés.
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public final class EquatorialToHorizontalConversion
        implements Function<EquatorialCoordinates, HorizontalCoordinates> {

    private final double cosϕ;
    private final double sinϕ;
    private final double localSideralTime;

    /**
     * construit un changement de systèmes de coordonnées entre les coordonnées
     * équatoriales et les coordonnées horizontales
     *
     * @param when
     *            date/heure
     * @param where
     *            lieu
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when,
            GeographicCoordinates where) {

        this.localSideralTime = SiderealTime.local(when, where);

        this.cosϕ = cos(where.lat());
        this.sinϕ = sin(where.lat());

    }

    /**
     * retourne les coordonnées horizontales correspondant aux coordonnées
     * équatoriales
     *
     * @param equ coordonnées
     *            équatoriales equ
     * @return retourne les coordonnées horizontales correspondant aux
     *         coordonnées équatoriales
     */
    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equ) {
        double sinδ = sin(equ.dec());
        double cosδ = cos(equ.dec());

        double hourAngle = localSideralTime - equ.ra();
        double cosHourAngle = cos(hourAngle);
        double sinHourAngle = sin(hourAngle);

        double h = Math.asin(sinδ * sinϕ + cosδ * cosϕ * cosHourAngle);
        double A = Angle.normalizePositive(
                Math.atan2(-cosδ * cosϕ * sinHourAngle, sinδ - sinϕ * sin(h)));

        return HorizontalCoordinates.of(A, h);
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