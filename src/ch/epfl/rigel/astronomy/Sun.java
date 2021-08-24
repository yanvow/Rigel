package ch.epfl.rigel.astronomy;

import java.util.Objects;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * Représente le Soleil à un instant donné.
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public final class Sun extends CelestialObject {

    private final EclipticCoordinates eclipticPos;
    private final float meanAnomaly;
    private final static float MAGNITUDE = -26.7f;
    private final static String NAME = "Soleil";

    /**
     * construit (un objet représentant) le Soleil avec la position écliptique,
     * la position équatoriale, la taille angulaire et l'anomalie moyenne
     * données
     *
     * @param eclipticPos
     *            position écliptique
     * @param equatorialPos
     *            position équatoriale
     * @param angularSize
     *            a taille angulaire
     * @param meanAnomaly
     *            l'anomalie moyenne
     * @throws NullPointerException
     *             si la position écliptique est nulle.
     */
    public Sun(EclipticCoordinates eclipticPos,
            EquatorialCoordinates equatorialPos, float angularSize,
            float meanAnomaly) {
        super(NAME, equatorialPos, angularSize, MAGNITUDE);

        this.eclipticPos = Objects.requireNonNull(eclipticPos);
        this.meanAnomaly = meanAnomaly;
    }

    /**
     * @return la position écliptique
     */
    public EclipticCoordinates eclipticPos() {
        return eclipticPos;
    }

    /**
     * @return l'anomalie moyenne donnée
     */
    public double meanAnomaly() {
        return meanAnomaly;
    }
}