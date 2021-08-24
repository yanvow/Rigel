package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

/**
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public final class Star extends CelestialObject {

    private static final ClosedInterval INTERVAL_COLORS = ClosedInterval.of(-.5,
            5.5);

    private int hipparcosId;
    private float colorIndex;

    /**
     * Construit une étoile
     * 
     * @param hipparcosId
     *            le numéro Hipparcos
     * @param name
     *            le nom
     * @param equatorialPos
     *            la position équatoriale
     * @param magnitude
     *            la magnitude
     * @param colorIndex
     *            l'indice de couleur
     * @throws IllegalArgumentException
     *             si le numéro Hipparcos est négatif, ou si l'indice de couleur
     *             n'est pas compris dans l'intervalle [-0.5, 5.5].
     * 
     */
    public Star(int hipparcosId, String name,
            EquatorialCoordinates equatorialPos, float magnitude,
            float colorIndex) {
        super(name, equatorialPos, 0, magnitude);

        Preconditions.checkArgument(hipparcosId >= 0);
        Preconditions.checkInInterval(INTERVAL_COLORS, colorIndex);

        this.hipparcosId = hipparcosId;
        this.colorIndex = colorIndex;
    }

    /**
     * retourne le numéro Hipparcos de l'étoile
     * 
     * @return le numéro Hipparcos de l'étoile
     */
    public int hipparcosId() {
        return hipparcosId;
    }

    /**
     * retourne la température de couleur de l'étoile, en degrés Kelvin
     * 
     * @return retourne la température de couleur de l'étoile, en degrés Kelvin,
     *         arrondie par défaut (c-à-d à l'entier inférieur le plus proche).
     */
    public int colorTemperature() {
        double T = 4600 * (1 / (0.92 * colorIndex + 1.7)
                + 1 / (.92 * colorIndex + .62));
        return (int) Math.floor(T);
    }

}