package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;

/**
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public final class Preconditions {

    private Preconditions() {
    }

    /**
     * lève l'exception IllegalArgumentException si son argument est faux, et ne
     * fait rien sinon
     * 
     * @param isTrue
     *            estVrai
     * @throws IllegalArgumentException()
     *             si l'argumenent isTrue est faux
     */
    public static void checkArgument(boolean isTrue) {
        if (!isTrue)
            throw new IllegalArgumentException();
    }

    /**
     * qui lève l'exception IllegalArgumentException si value n'appartient pas à
     * interval, et retourne value sinon.
     * 
     * @param interval
     *            l'intervalle
     * @param value
     *            la valeur
     * @throws IllegalArgumentException
     *             si value n'appartient pas à interval
     * @return la valeur si elle appartient à l'intervalle
     */
    public static double checkInInterval(Interval interval, double value) {
        checkArgument(interval.contains(value));
        return value;
    }

}