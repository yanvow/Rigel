package ch.epfl.rigel.math;

import java.util.Locale;

import ch.epfl.rigel.Preconditions;

/**
 * The Class ClosedInterval.
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */

public final class ClosedInterval extends Interval {

    private ClosedInterval(double a, double b) {

        super(a, b);
    }

    /**
     * Retourne un intervalle fermé allant de low à high.
     *
     * @param low
     *            la borne inférieure
     * @param high
     *            la borne supérieure
     * @return Retourne un intervalle fermé allant de low à high
     * @throws IllegalArgumentException
     *             si la borne inférieure low n'est pas (strictement) plus
     *             petite que la borne supérieure high
     */
    public static ClosedInterval of(double low, double high) {
        Preconditions.checkArgument((low < high));
        return new ClosedInterval(low, high);
    }

    /**
     * retourne un intervalle fermé centré en 0 et de taille size.
     * 
     * @param size
     *            la taille
     * @return retourne un intervalle fermé centré en 0 et de taille size.
     * @throws IllegalArgumentException
     *             si la taille n'est pas (strictement) positive
     */
    public static ClosedInterval symmetric(double size) {
        Preconditions.checkArgument(size > 0);
        return new ClosedInterval(-size / 2, +size / 2);
    }

    /**
     * retourne vrai si et seulement si la valeur v appartient à l'intervalle.
     *
     * @param v
     *            la valeur
     * @return vrai si et seulement si la valeur v appartient à l'intervalle.
     */
    @Override
    public boolean contains(double v) {
        return (v >= this.low() && v <= this.high());
    }

    /**
     * écrête son argument à l'intervalle.
     *
     * @param v
     *            la valeur v
     * @return the double
     */
    public double clip(double v) {
        if (v <= this.low()) {
            return this.low();
        } else if (v >= this.high()) {
            return this.high();
        } else {
            return v;
        }
    }

    /**
     * représentation textuelle de l'intervalle
     *
     * @return la représentation textuelle de l'intervalle
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "[%s,%s]", this.low(), this.high());
    }
}