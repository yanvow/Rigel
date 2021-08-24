package ch.epfl.rigel.astronomy;

import java.util.Collections;
import java.util.List;

import ch.epfl.rigel.Preconditions;

/**
 * Astérisme
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public final class Asterism {

    private List<Star> stars;

    /**
     * Construit un astérisme composé de la liste d'étoiles données
     *
     * @param stars
     *            liste d'étoile
     * @throws IllegalArgumentException
     *             si la liste d'étoile est vide
     */
    public Asterism(List<Star> stars) {
        Preconditions.checkArgument(!stars.isEmpty());
        this.stars = Collections.unmodifiableList(List.copyOf(stars));
    }

    /**
     *
     * @return liste des étoiles formant l'astérisme
     */
    public List<Star> stars() {
        return stars;
    }
}