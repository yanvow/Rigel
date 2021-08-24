package ch.epfl.rigel.math;

import java.util.Locale;

import ch.epfl.rigel.Preconditions;

/**
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public final class RightOpenInterval extends Interval {

    private RightOpenInterval(double a, double b) {
        super(a, b);
    }

    /**
     * retourne un intervalle semi-ouvert à droite
     * 
     * @param low
     *            la borne inférieure de l'intervalle
     * @param high
     *            la borne supérieure de l'intervalle
     * @return retourne un intervalle semi-ouvert à droite
     */
    public static RightOpenInterval of(double low, double high) {
        Preconditions.checkArgument(low < high);
        return new RightOpenInterval(low, high);
    }

    /**
     * retourne un intervalle semi-ouvert centré en 0 et de taille size.
     * 
     * @param size
     *            la taille
     * @return retourne un intervalle fermé centré en 0 et de taille size.
     * @throws IllegalArgumentException
     *             si la taille n'est pas (strictement) positive
     */
    public static RightOpenInterval symmetric(double size) {
        Preconditions.checkArgument(size > 0);
        return new RightOpenInterval(-size / 2, +size / 2);
    }

    /**
     * réduit son argument à l'intervalle
     * 
     * @param v
     *            la valeur
     * @return réduit son argument à l'intervalle
     */
    public double reduce(double v) {
        return this.low() + floorMod(v - this.low(), this.high() - this.low());
    }

    /**
     * retourne vrai si et seulement si la valeur v appartient à l'intervalle.
     */
    @Override
    public boolean contains(double v) {
        return (v >= this.low() && v < this.high());
    }

    /**
     * représentation textuelle de l'intervalle
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "[%s,%s[", this.low(), this.high());
    }

    private double floorMod(double x, double y) {
        return x - y * java.lang.Math.floor(x / y);
    }
}
