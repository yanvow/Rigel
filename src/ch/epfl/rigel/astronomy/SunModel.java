package ch.epfl.rigel.astronomy;

import static ch.epfl.rigel.math.Angle.TAU;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

/**
 * Modèle du Soleil
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public enum SunModel implements CelestialObjectModel<Sun> {
    SUN(Angle.ofDeg(279.557208), Angle.ofDeg(283.112438), 0.016705);

    private double epsiG;
    private double omegG;
    private double e;

    /**
     * Constructeur
     * 
     * @param epsiG
     *            Longitude du Soleil à J2010
     * @param omegG
     *            Longitude du Soleil au périgée
     * @param e
     *            Excentricité de l'orbite Soleil/Terre
     */
    SunModel(double epsiG, double omegG, double e) {

        this.epsiG = epsiG;
        this.omegG = omegG;
        this.e = e;
    }

    /**
     * Représente le soleil à un instant donné
     */
    @Override
    public Sun at(double daysSinceJ2010,
            EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        double tetha0 = Angle.ofDeg(.533128);

        double TROPIC_YEAR = TAU / 365.242191;

        double meanAno = TROPIC_YEAR * daysSinceJ2010 + epsiG - omegG;

        double trueAno = meanAno + 2 * e * sin(meanAno);

        double eclipLon = Angle.normalizePositive(trueAno + omegG);

        double eclipLat = 0d;

        EclipticCoordinates eclipCoord = EclipticCoordinates.of(eclipLon,
                eclipLat);

        EquatorialCoordinates equaCoord = eclipticToEquatorialConversion
                .apply(eclipCoord);

        double angularSize = tetha0
                * ((1 + e * cos(trueAno)) / (1 - pow(e, 2)));

        return new Sun(eclipCoord, equaCoord, (float) angularSize,
                (float) meanAno);
    }
}