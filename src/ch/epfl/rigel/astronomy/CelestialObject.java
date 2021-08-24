package ch.epfl.rigel.astronomy;

import static ch.epfl.rigel.Preconditions.checkArgument;

import java.util.Objects;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * Objet celeste
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public abstract class CelestialObject {

    private String name;
    private EquatorialCoordinates equatorialPos;
    private float angularSize;
    private float magnitude;

    /**
     * Construit un objet céleste
     *
     * @param name
     *            nom
     * @param equatorialPos
     *            coordonnées équatoriales
     * @param angularSize
     *            taille angulaire
     * @param magnitude
     *            magnitude
     * @throws IllegalArgumentException
     *             si la taille angulaire est négative
     * @throws NullPointerException
     *             si le nom ou la position équatoriale sont nuls
     */
    CelestialObject(String name, EquatorialCoordinates equatorialPos,
            float angularSize, float magnitude) {
        checkArgument(angularSize >= 0);
        this.name = Objects.requireNonNull(name);
        this.equatorialPos = Objects.requireNonNull(equatorialPos);
        this.angularSize = angularSize;
        this.magnitude = magnitude;
    }

    /**
     * @return le nom
     */
    public String name() {
        return name;
    }

    /**
     * @return la taille angulaire
     */
    public double angularSize() {
        return angularSize;
    }

    /**
     * @return la magnitude
     */
    public double magnitude() {
        return magnitude;
    }

    /**
     * @return les coordonnées équatoriales
     */
    public EquatorialCoordinates equatorialPos() {
        return equatorialPos;
    }

    /**
     * @return texte informatif au sujet de l'objet
     */
    public String info() {
        return name();
    }

    /**
     * retourne un (court) texte informatif au sujet de l'objet, destiné à être
     * montré à l'utilisateur
     */
    @Override
    public String toString() {
        return info();
    }
}