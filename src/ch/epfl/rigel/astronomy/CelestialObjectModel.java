package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

/**
 * Modèle d'objet céleste
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public interface CelestialObjectModel<O> {

    /**
     * Représente un modèle d'objet céleste.
     * 
     * @param daysSinceJ2010
     *            nombre de jours après l'époque J2010
     * @param eclipticToEquatorialConversion
     *            conversion de coordonnées écliptiques vers des coordonnées
     *            équatoriales
     * @return l'objet modélisé par le modèle
     */
    public abstract O at(double daysSinceJ2010,
            EclipticToEquatorialConversion eclipticToEquatorialConversion);
}