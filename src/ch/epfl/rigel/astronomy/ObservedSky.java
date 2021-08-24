package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;


/**
 * Représente un ensemble d'objets célestes projetés dans le plan par une
 * projection stéréographique à un instant et un endroit d'observation donnés.
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public final class ObservedSky {

    private final Sun sun;
    private final Moon moon;
    private final List<Planet> lisPlanet;
    private final List<Star> lisStar;

    private final StarCatalogue starCatalogue;

    EquatorialToHorizontalConversion equToHor;
    StereographicProjection proj;

    private final CartesianCoordinates projSun;
    private final CartesianCoordinates projMoon;
    private CartesianCoordinates[] projTraceSun;
    private CartesianCoordinates[] projTraceMoon;
    private final double[] projPlanets;
    private final double[] projStars;

    private final static int INTERVAL_MINUTE = 2;
    private final static int MIUTES_IN_A_DAY = 1440;

    /**
     * représente un ensemble d'objets célestes projetés dans le plan par une
     * projection stéréographique à un instant et un endroit d'observation
     * donnés.
     *
     * @param when      l'instant d'observation (donné par un couple date/heure
     *                  « zoné »)
     * @param where     la position d'observation (donnée par ses coordonnées
     *                  géographiques)
     * @param proj      la projection stéréographique à utiliser
     * @param catalogue le catalogue contenant les étoiles et les astérismes
     */
    public ObservedSky(ZonedDateTime when, GeographicCoordinates where,
                       StereographicProjection proj, StarCatalogue catalogue) {

        this.proj = proj;

        // Soleil
        this.sun = SunModel.SUN.at(Epoch.J2010.daysUntil(when),
                new EclipticToEquatorialConversion(when));
        // Lune
        this.moon = MoonModel.MOON.at(Epoch.J2010.daysUntil(when),
                new EclipticToEquatorialConversion(when));

        // Liste des planètes
        lisPlanet = new ArrayList<>();
        for (PlanetModel pl : PlanetModel.ALL) {
            if (pl != PlanetModel.EARTH) {
                this.lisPlanet.add(pl.at(Epoch.J2010.daysUntil(when),
                        new EclipticToEquatorialConversion(when)));
            }
        }

        // Liste des étoiles
        this.starCatalogue = catalogue;
        this.lisStar = catalogue.stars();

        // Les projections
        equToHor = new EquatorialToHorizontalConversion(
                when, where);

        // Projection du Soleil
        HorizontalCoordinates sunHorCoord = equToHor.apply(sun.equatorialPos());
        this.projSun = proj.apply(sunHorCoord);

        // Projection de la Lune
        HorizontalCoordinates moonHorCoord = equToHor
                .apply(moon.equatorialPos());
        this.projMoon = proj.apply(moonHorCoord);

        // Projection des planètes
        this.projPlanets = new double[14];

        for (int i = 0; i < projPlanets.length; i += 2) {
            HorizontalCoordinates planetHorCoord = equToHor
                    .apply(lisPlanet.get(i / 2).equatorialPos());
            CartesianCoordinates planetCarCoord = proj.apply(planetHorCoord);
            this.projPlanets[i] = planetCarCoord.x();
            this.projPlanets[i + 1] = planetCarCoord.y();
        }

        // Projection des étoiles
        this.projStars = new double[lisStar.size() * 2];

        for (int i = 0; i < projStars.length; i += 2) {
            HorizontalCoordinates starHorCoord = equToHor
                    .apply(lisStar.get(i / 2).equatorialPos());
            CartesianCoordinates starCarCoord = proj.apply(starHorCoord);
            this.projStars[i] = starCarCoord.x();
            this.projStars[i + 1] = starCarCoord.y();
        }

        // Projection des traces du Soleil et de la Lune
        projTraceSun = new CartesianCoordinates[(MIUTES_IN_A_DAY / INTERVAL_MINUTE)];
        projTraceMoon = new CartesianCoordinates[(MIUTES_IN_A_DAY / INTERVAL_MINUTE)];

        for (int i = 1; i < MIUTES_IN_A_DAY + 1; i += INTERVAL_MINUTE) {

            EquatorialToHorizontalConversion equToHorDay = new EquatorialToHorizontalConversion(
                    when.plusMinutes(i), where);

            HorizontalCoordinates sunTraceHorCoord = equToHorDay.apply(sun.equatorialPos());
            projTraceSun[i / INTERVAL_MINUTE] = proj.apply(sunTraceHorCoord);

            HorizontalCoordinates moonTraceHorCoord = equToHorDay.apply(moon.equatorialPos());
            projTraceMoon[i / INTERVAL_MINUTE] = proj.apply(moonTraceHorCoord);
        }
    }

    /**
     * retourne le Soleil sous la forme d'une instance de Sun
     *
     * @return le Soleil sous la forme d'une instance de Sun
     */
    public Sun sun() {
        return sun;
    }

    /**
     * retourne la position du Soleil dans le plan, sous la forme d'une instance
     * de CartesianCoordinates
     *
     * @return la position du Soleil dans le plan, sous la forme d'une instance
     * de CartesianCoordinates
     */
    public CartesianCoordinates sunPosition() {
        return projSun;
    }

    /**
     * retourne les positions de la trace du Soleil dans le plan, sous la forme d'une tableau
     * de CartesianCoordinates
     *
     * @return les positions de la trace du Soleil dans le plan, sous la forme d'une tableau
     * de CartesianCoordinates
     */
    public CartesianCoordinates[] sunTracePosition() {
        return projTraceSun;
    }

    /**
     * retourne la Lune sous la forme d'une instance de Moon
     *
     * @return la Lune sous la forme d'une instance de Moon
     */
    public Moon moon() {
        return moon;
    }

    /**
     * retourne la position de la Lune dans le plan, sous la forme d'une
     * instance de CartesianCoordinates
     *
     * @return la position de la Lune dans le plan, sous la forme d'une instance
     * de CartesianCoordinates
     */
    public CartesianCoordinates moonPosition() {
        return projMoon;
    }

    /**
     * retourne les positions de la trace de la Lune dans le plan, sous la forme d'une tableau
     * de CartesianCoordinates
     *
     * @return les positions de la trace de la Lune dans le plan, sous la forme d'une tableau
     * de CartesianCoordinates
     */
    public CartesianCoordinates[] moonTracePosition() {
        return projTraceMoon;
    }

    /**
     * retourne la liste des sept planètes extraterrestres du système solaire
     *
     * @return la liste des sept planètes extraterrestres du système solaire
     */
    public List<Planet> planets() {
        return List.copyOf(lisPlanet);
    }

    /**
     * retourne les coordonnées cartésiennes des sept planètes extraterrestres
     * du système solaire dans un tableau de double.
     *
     * @return les coordonnées cartésiennes des sept planètes extraterrestres
     * dans un tableau de double.
     */
    public double[] planetPosition() {
        return projPlanets;
    }

    /**
     * retourne la liste des étoiles
     *
     * @return la liste des étoiles
     */
    public List<Star> stars() {
        return lisStar;
    }

    /**
     * retourne les coordonnées cartésiennes des étoiles dans un tableau de
     * double.
     *
     * @return les coordonnées cartésiennes des étoiles dans un tableau de
     * double.
     */
    public double[] starPosition() {
        return projStars;
    }

    /**
     * retourne l'ensemble des astérismes du catalogue
     *
     * @return l'ensemble des astérismes du catalogue
     */
    public Set<Asterism> asterismSet() {
        return starCatalogue.asterisms();
    }

    /**
     * Retourne la liste des index — dans le catalogue — des étoiles constituant
     * l'astérisme donné
     *
     * @param asterism l'astérisme donné
     * @return retourne la liste des index — dans le catalogue — des étoiles
     * constituant l'astérisme donné
     * @throws IllegalArgumentException si l'astérisme donné ne fait pas partie du catalogue
     */
    public List<Integer> asterismIndices(Asterism asterism) {
        return starCatalogue.asterismIndices(asterism);
    }

    /**
     * retourne l'ensemble des limites des constellation du catalogue
     *
     * @return l'ensemble des limites des constellation du catalogue
     */
    public Set<Boundaries> boundariesSet() {
        return starCatalogue.boundaries();
    }

    /**
     * retourne les positions des limites de la constellation mise en argumrent
     * dans le plan, sous la forme d'une tableau
     *
     * @param boundaries une constellation
     * @return le tableau des positions des limites de la constellation choisie
     */
    public CartesianCoordinates[] bundariesPosition(Boundaries boundaries) {

        List<EquatorialCoordinates> list = boundaries.getPoints();

        CartesianCoordinates boundariePositions[] = new CartesianCoordinates[list.size()];

        int i = 0;

        for (EquatorialCoordinates equa : list) {
            HorizontalCoordinates boundHorCoord = equToHor.apply(equa);
            CartesianCoordinates boundCarCoord = proj.apply(boundHorCoord);
            boundariePositions[i] = boundCarCoord;
            i++;
        }
        return boundariePositions;
    }

    /**
     * Etant donné les coordonnées d'un point du plan et une distance maximale,
     * retourne l'objet céleste le plus proche de ce point, pour peu qu'il se
     * trouve à une distance inférieure à la distance maximale
     *
     * @param coord   les coordonnées cartésiennes d'un point du plan
     * @param distMax la distance maximale
     * @return l'objet céleste le plus proche de coord pour peu qu'il se trouve
     * à une distance inférieure à la distance maximale
     */
    public Optional<CelestialObject> objectClosestTo(CartesianCoordinates coord,
                                                     double distMax) {

        double x1 = coord.x();
        double y1 = coord.y();

        // Création d'une liste contenant tous les CelestialObject
        List<CelestialObject> allCelestial = new ArrayList<>();

        allCelestial.add(sun);
        allCelestial.add(moon);
        allCelestial.addAll(lisPlanet);
        allCelestial.addAll(lisStar);

        // // Création d'un tableau contenant les projections
        double[] all = new double[4 + projPlanets.length + projStars.length];
        all[0] = projSun.x();
        all[1] = projSun.y();
        all[2] = projMoon.x();
        all[3] = projMoon.y();

        for (int i = 4; i < projPlanets.length + 4; i++) {
            all[i] = projPlanets[i - 4];
        }

        for (int i = projPlanets.length + 4; i < projStars.length
                + projPlanets.length + 4; i++) {
            all[i] = projStars[i - (projPlanets.length + 4)];
        }

        // Calcul de l'index de l'objet céleste le plus proche
        double min = Double.MAX_VALUE;
        Optional<Integer> index = Optional.empty();
        for (int i = 0; i < all.length; i += 2) {
            double value = sqrt(pow(all[i] - x1, 2) + pow(all[i + 1] - y1, 2));
            if ((value < distMax) && (value < min)) {
                min = value;
                index = Optional.of(i);
            }
        }

       /* String nameOfCloserOne = allCelestial.get(index.get() / 2).name();
         String result =FindType(nameOfCloserOne);

         switch(result){
             case "P" :
               Planet planet = (Planet) allCelestial.get(index.get() / 2);
               planet.khra();
               break;

             case "S" :
                 Star star = (Star) allCelestial.get(index.get() / 2);
                 int id = star.hipparcosId();
                 System.out.println("the id of "+ star.name() + " is:" + id);
                 break;
         }*/

        // Return l'objet céleste le plus proche dans un Optional
        return index.isPresent()
                ? Optional.of(allCelestial.get(index.get() / 2))
                : Optional.empty();
    }

    private String FindType(String name) {

        for (Planet planet : lisPlanet) {

            if (planet.name().equals(name))
                return "P";
        }

        for (Star star : lisStar) {

            if (star.name().equals(name))
                return "S";
        }
        return null;
    }
}