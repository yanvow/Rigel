package ch.epfl.rigel.astronomy;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Représente un catalogue d'étoiles et d'astérismes
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */

public final class StarCatalogue {

    private final List<Star> stars;
    private final List<Asterism> asterisms;
    private final Map<Asterism, List<Integer>> asterismToIndex = new HashMap<>();
    private final List<Boundaries> boundaries;

    /**
     * Construit un catalogue constitué des étoiles stars et des astérismes
     * asterisms.
     *
     * @param stars     étoiles
     * @param asterisms asterismes
     * @throws IllegalArgumentException si l'un des astérismes contient une étoile qui ne fait pas
     *                                  partie de la liste d'étoiles
     */
    public StarCatalogue(List<Star> stars, List<Asterism> asterisms, List<Boundaries> boundaries) {

        this.stars = List.copyOf(stars);
        this.asterisms = List.copyOf(asterisms);
        this.boundaries = List.copyOf(boundaries);

        for (Asterism as : asterisms) {
            for (Star st : as.stars()) {
                if (!stars.contains(st))
                    throw new IllegalArgumentException();
            }
        }

        for (Asterism as : asterisms) {
            List<Integer> indexList = new ArrayList<Integer>();
            for (Star etoile : as.stars())
                indexList.add(stars.indexOf(etoile));

            asterismToIndex.put(as, indexList);
        }
    }

    /**
     * Retourne la liste des étoiles du catalogue.
     *
     * @return la liste des étoiles du catalogue
     */
    public List<Star> stars() {
        return Collections.unmodifiableList(stars);
    }

    /**
     * Retourne l'ensemble des astérismes du catalogue.
     *
     * @return l'ensemble des astérismes du catalogue
     */
    public Set<Asterism> asterisms() {
        Set<Asterism> set = new HashSet<Asterism>();
        set.addAll(asterisms);
        return set;
    }

    /**
     * @param asterism l'astérisme donné
     * @return retourne la liste des index — dans le catalogue — des étoiles
     * constituant l'astérisme donné
     * @throws IllegalArgumentException si l'astérisme donné ne fait pas partie du catalogue
     */
    public List<Integer> asterismIndices(Asterism asterism) {
        if (!asterisms.contains(asterism)) {
            throw new IllegalArgumentException();
        } else {
            List<Integer> ls = List.copyOf(asterismToIndex.get(asterism));
            return ls;
        }
    }

    /**
     * Retourne l'ensemble des limites des constellations
     *
     * @return l'ensemble des limites des constellations
     */
    public Set<Boundaries> boundaries() {
        Set<Boundaries> set = new HashSet<>();
        set.addAll(boundaries);
        return set;
    }

    /**
     * représente un bâtisseur de catalogue d'étoiles
     */
    public static final class Builder {

        private List<Star> starsBuilder;
        private List<Asterism> asterismsBuilder;
        private List<Boundaries> boundariesBuilder;

        /**
         * Constructeur par défaut qui initialise le bâtisseur
         */
        public Builder() {
            this.starsBuilder = new ArrayList<>();
            this.asterismsBuilder = new ArrayList<>();
            this.boundariesBuilder = new ArrayList<>();
        }

        /**
         * Ajoute l'étoile donnée au catalogue en cours de construction
         *
         * @param star étoile
         * @return le bâtisseur
         */
        public Builder addStar(Star star) {
            starsBuilder.add(star);
            return this;
        }

        /**
         * @return retourne une vue non modifiable — mais pas immuable — sur les
         * étoiles du catalogue en cours de construction
         */
        public List<Star> stars() {
            return Collections.unmodifiableList(starsBuilder);
        }

        /**
         * Ajoute l'astérisme donné au catalogue en cours de construction
         *
         * @param asterism l'astérisme
         * @return le bâtisseur
         */
        public Builder addAsterism(Asterism asterism) {
            asterismsBuilder.add(asterism);
            return this;
        }

        /**
         * Ajoute les limites des constellations donné au catalogue
         * en cours de construction
         *
         * @param boundaries les limites de constellation
         * @return le bâtisseur
         */
        public Builder addBoundaries(Boundaries boundaries) {
            boundariesBuilder.add(boundaries);
            return this;
        }

        /**
         * demande au chargeur loader d'ajouter au catalogue les étoiles et/ou
         * astérismes et/ou limites de constellationsqu'il obtient depuis le
         * flot d'entrée inputStream, et retourne le bâtisseur,
         * ou lève IOException en cas d'erreur d'entrée/sortie
         *
         * @param inputStream Flot d'entrée
         * @param loader      chargeur loader
         * @return retourne le bâtisseur
         * @throws IOException en cas d'erreur d'entrée/sortie
         */
        public Builder loadFrom(InputStream inputStream, Loader loader)
                throws IOException {
            loader.load(inputStream, this);
            return this;
        }


        /**
         * @return Retourne le catalogue contenant les étoiles, astérismes et les limites des constellations
         * ajoutés jusqu'alors au bâtisseur.
         */
        public StarCatalogue build() {
            return new StarCatalogue(starsBuilder, asterismsBuilder, boundariesBuilder);
        }

    }

    /**
     * représente un chargeur de catalogue d'étoiles et d'astérismes.
     */
    public interface Loader {

        /**
         * charge les étoiles et/ou astérismes du flot d'entrée inputStream et
         * les ajoute au catalogue en cours de construction du bâtisseur
         * builder, ou lève IOException en cas d'erreur d'entrée/sortie.
         *
         * @param inputStream Flot d'entrée
         * @param builder     bâtisseur
         * @throws IOException en cas d'erreur d'entrée/sortie
         */
        void load(InputStream inputStream, Builder builder) throws IOException;

    }
}