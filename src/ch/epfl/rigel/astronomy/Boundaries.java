package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.List;

/**
 * Limite de Constellation
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public final class Boundaries {

    private final String name;
    private final List<EquatorialCoordinates> points;

    /**
     * Constructeur
     *
     * @param name   le nom
     * @param points les points des limites
     */
    public Boundaries(String name, List<EquatorialCoordinates> points) {
        Preconditions.checkArgument(!points.isEmpty());
        this.name = name;
        this.points = List.copyOf(points);
    }

    /**
     * retourne une liste des points limites de la constellation
     *
     * @return la liste des points
     */
    public List<EquatorialCoordinates> getPoints() {
        return points;
    }

    /**
     * retourne le nom de la constellation
     *
     * @return le nom
     */
    public String getName() {
        return name;
    }
}