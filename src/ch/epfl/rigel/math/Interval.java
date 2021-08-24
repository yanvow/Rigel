package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

/**
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public abstract class Interval {

    private final double borneInf;
    private final double borneSup;

    /**
     * Représente un intervalle.
     * 
     * @param borneInf
     *            la borne inférieure de l'intervalle
     * @param borneSup
     *            la borne supérieure de l'intervalle
     */
    public Interval(double borneInf, double borneSup) {
        Preconditions.checkArgument(borneInf < borneSup);
        this.borneInf = borneInf;
        this.borneSup = borneSup;
    }

    /**
     * @return retourne la borne inférieure de l'intervalle,
     */
    public double low() {
        return borneInf;
    }

    /**
     * @return retourne la borne supérieure de l'intervalle
     */
    public double high() {
        return borneSup;
    }

    /**
     * @return retourne la taille de l'intervalle
     */
    public double size() {
        return borneSup - borneInf;
    }

    /**
     * @param v
     *            la valeur
     * @return qui retourne vrai si et seulement si la valeur v appartient à
     *         l'intervalle.
     */
    public abstract boolean contains(double v);

    /**
     * @throws UnsupportedOperationException
     */
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    /**
     * @throws UnsupportedOperationException
     */
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}