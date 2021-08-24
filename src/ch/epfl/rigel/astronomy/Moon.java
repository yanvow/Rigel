package ch.epfl.rigel.astronomy;

import java.util.Locale;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

/**
 * représente la Lune à un instant donné
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public final class Moon extends CelestialObject {

    private final static ClosedInterval INTERVAL_PHASE = ClosedInterval.of(0,1);

    private final float phase;

    /**
     * construit (un objet représentant) la Lune avec la position, la taille
     * angulaire, la magnitude et la phase données
     *
     * @param equatorialPos
     *            la position équatoriale
     * @param angularSize
     *            la taille angulaire
     * @param magnitude
     *            la magnitude
     * @param phase
     *            la phase
     */
    public Moon(EquatorialCoordinates equatorialPos, float angularSize,
            float magnitude, float phase) {
        super("Lune", equatorialPos, angularSize, magnitude);
        this.phase = (float) Preconditions.checkInInterval(INTERVAL_PHASE,
                phase);

    }

    /**
     * Getter
     * @return la phase de la lune
     */
    public float getPhase() {
        return phase;
    }

    /**
     * renvoi le nom suivi de la phase en en pourcent, avec une décimale
     */
    @Override
    public String info() {
        return String.format(Locale.ROOT, "%s (%.1f%%)", this.name(),
                this.phase * 100);
    }
}