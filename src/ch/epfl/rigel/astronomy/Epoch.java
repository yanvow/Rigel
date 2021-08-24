package ch.epfl.rigel.astronomy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Epoque astronomique
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public enum Epoch {

    J2000(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1),
            LocalTime.of(12, 0), ZoneOffset.UTC)), 

    J2010(ZonedDateTime.of(LocalDate.of(2010, Month.JANUARY, 1).minusDays(1),
            LocalTime.of(0, 0), ZoneOffset.UTC));

    private static final double DAY_PER_MILSEC = 1d / 8.64E7;
    private static final double DAY_PER_JUL_CENT = 36525;

    private final ZonedDateTime zonedDateTime;

    /**
     * Constructeur
     *
     * @param zonedDateTime
     *            date précise de l'epoque astronomique
     */
    private Epoch(ZonedDateTime zonedDateTime) {
        this.zonedDateTime = zonedDateTime;
    }

    /**
     * Calcule le nombre de jours entre la date donnée et l'époque
     *
     * @param when
     *            date
     * @return la différence en jours
     */
    public double daysUntil(ZonedDateTime when) {
        return DAY_PER_MILSEC * zonedDateTime.until(when, ChronoUnit.MILLIS);
    }

    /**
     * Calcule le nombre de siècles juliens entre la date donnée et l'époque
     *
     * @param when
     *            date
     * @return la différence en siècles juliens
     */
    public double julianCenturiesUntil(ZonedDateTime when) {
        return daysUntil(when) / DAY_PER_JUL_CENT;
    }
}