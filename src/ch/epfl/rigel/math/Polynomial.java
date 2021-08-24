package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

/**
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public final class Polynomial {

    private double[] coefficients;

    private Polynomial(double[] coefficients) {
        this.coefficients = coefficients;
    }

    /**
     * retourne la fonction polynomiale avec les coefficients donnés, ordonnés
     * par degré en ordre décroissant
     * 
     * 
     * @param coefficientN
     *            coefficient de plus haut degré
     * @param coefficients
     *            liste de coefficients
     * @return retourne la fonction polynomiale avec les coefficients donnés,
     *         ordonnés par degré en ordre décroissant
     * @throws IllegalArgumentException
     *             si le coefficient de plus haut degré (coefficientN) vaut 0
     */
    public static Polynomial of(double coefficientN, double... coefficients) {
        Preconditions.checkArgument(coefficientN != 0);

        double newArray[] = new double[coefficients.length + 1];
        newArray[0] = coefficientN;
        System.arraycopy(coefficients, 0, newArray, 1, coefficients.length);

        return new Polynomial(newArray);
    }

    /**
     * retourne la valeur de la fonction polynomiale pour l'argument donné
     * 
     * @param x
     *            la valeur
     * @return etourne la valeur de la fonction polynomiale pour l'argument
     *         donné
     */
    public double at(double x) {
        double value = coefficients[0];

        for (int i = 1; i < coefficients.length; i++) {
            value = value * x + coefficients[i];
        }
        return value;
    }

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

    /**
     * retourne la représentation textuelle de la fonction polynomiale
     */
    @Override
    public String toString() {

        StringBuilder str = new StringBuilder();
        int power = coefficients.length;

        for (int i = 0; i < coefficients.length; ++i) {
            power -= 1;
            if (!(coefficients[i] == 0)) {

                if (coefficients[i] > 0 && i != 0) {
                    str.append("+");
                }

                if (Math.abs(coefficients[i]) != 1) {
                    str.append(coefficients[i]);
                } else if (coefficients[i] == -1) {
                    str.append("-");
                }

                if (power != 0 && power != 1) {
                    str.append("x^" + power);
                }
                if (power == 1) {
                    str.append("x");
                }
            }
        }
        return str.toString();
    }
}