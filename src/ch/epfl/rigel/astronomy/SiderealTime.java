package ch.epfl.rigel.astronomy;

import java.math.MathContext;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

/**
 * Temps sidéral
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public final class SiderealTime {

    private static final double HR_PER_MILSEC = 1d / 3.6E6;
    
    private SiderealTime() {};

    /**
     * Calcule le temps sidéral de Greenwich pour une date/heure donnée
     *
     * @param when
     *            date/heure
     * @return le temps sidéral en radians
     */
    public static double greenwich(ZonedDateTime when) {

        ZonedDateTime whenZoned = when.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime whenTrunc = whenZoned.truncatedTo(ChronoUnit.DAYS);

        Polynomial S0 = Polynomial.of(2.5862e-5, 2400.051336, 6.697374558);
        Polynomial S1 = Polynomial.of(1.002737909, 0);

        double T = Epoch.J2000.julianCenturiesUntil(whenTrunc);
        double t = HR_PER_MILSEC
                * whenTrunc.until(whenZoned, ChronoUnit.MILLIS);

        double Sg = S0.at(T) + S1.at(t);
        Sg = Angle.ofHr(Sg);
        Sg = Angle.normalizePositive(Sg);
        return Sg;
    }

    /**
     * Calcule le temps sidéral pour une date/heure et une position
     *
     * @param when
     *            date/heure
     * @param where
     *            position
     * @return le temps sidéral en radians
     */
    public static double local(ZonedDateTime when,
            GeographicCoordinates where) {
        return Angle.normalizePositive(greenwich(when) + where.lon());
    }
}