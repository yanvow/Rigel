package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Locale;

/**
 * représente une planète
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public final class Planet extends CelestialObject {

    /**
     * constructeur public prenant exactement les mêmes arguments que celui de
     * sa classe mère
     *
     * @param name
     *            le nom
     * @param equatorialPos
     *            les coordonnées équatoriales
     * @param angularSize
     *            la taille angulaire
     * @param magnitude
     *            la magnitude
     */
    public Planet(String name, EquatorialCoordinates equatorialPos,
            float angularSize, float magnitude) {
        super(name, equatorialPos, angularSize, magnitude);
    }

    public void khra(){


        System.out.println("nik babak");

    }
}