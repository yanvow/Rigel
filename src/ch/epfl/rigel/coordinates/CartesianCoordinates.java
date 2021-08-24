package ch.epfl.rigel.coordinates;

import java.util.Locale;

/**
 * Coordonnées cartésiennes
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public final class CartesianCoordinates {

    private final double x;
    private final double y;

    /**
     * Contructeur privé
     */
    private CartesianCoordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Méthode de construction de coordonnées cartésiennes
     * 
     * @param x
     *            abcisse
     * @param y
     *            ordonnée
     * @return coordonnées cartésiennes d'abscisse x et d'ordonnée y
     */
    public static CartesianCoordinates of(double x, double y) {
        return new CartesianCoordinates(x, y);
    }

    /**
     * @return l'abcisse de la coordonnée cartésienne
     */
    public double x() {
        return this.x;
    }

    /**
     * @return l'ordonnée de la coordonnée cartésienne
     */
    public double y() {
        return this.y;
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
     * retourne une représentation textuelle des coordonnées
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(abs=%.4f°, ord=%.4f°)", x(), y());
    }
}