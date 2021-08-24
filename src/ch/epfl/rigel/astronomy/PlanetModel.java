package ch.epfl.rigel.astronomy;

import static ch.epfl.rigel.math.Angle.TAU;
import static java.lang.Math.PI;
import static java.lang.Math.asin;
import static java.lang.Math.atan;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.log10;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;

import java.util.List;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

/**
 * Modèle de Planète
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public enum PlanetModel implements CelestialObjectModel<Planet> {

    MERCURY("Mercure", 0.24085, 75.5671, 77.612, 0.205627, 0.387098, 7.0051,
            48.449, 6.74, -0.42),

    VENUS("Vénus", 0.615207, 272.30044, 131.54, 0.006812, 0.723329, 3.3947,
            76.769, 16.92, -4.40),

    EARTH("Terre", 0.999996, 99.556772, 103.2055, 0.016671, 0.999985, 0, 0, 0,
            0),

    MARS("Mars", 1.880765, 109.09646, 336.217, 0.093348, 1.523689, 1.8497,
            49.632, 9.36, -1.52),

    JUPITER("Jupiter", 11.857911, 337.917132, 14.6633, 0.048907, 5.20278,
            1.3035, 100.595, 196.74, -9.40),

    SATURN("Saturne", 29.310579, 172.398316, 89.567, 0.053853, 9.51134, 2.4873,
            113.752, 165.60, -8.88),

    URANUS("Uranus", 84.039492, 356.135400, 172.884833, 0.046321, 19.21814,
            0.773059, 73.926961, 65.80, -7.19),

    NEPTUNE("Neptune", 165.84539, 326.895127, 23.07, 0.010483, 30.1985, 1.7673,
            131.879, 62.20, -6.87);

    /**
     * liste constituée des huit modèles de planètes
     */
    public static List<PlanetModel> ALL = List.of(MERCURY, VENUS, EARTH, MARS,
            JUPITER, SATURN, URANUS, NEPTUNE);

    private String frenchName;
    private double Tp;
    private double epsi;
    private double omega;
    private double e;
    private double a;
    private double i;
    private double Omega;
    private double tetha0;
    private double V0;

    /**
     * Constructeur
     *
     * @param frenchName
     *            nom français
     * @param Tp
     *            Période de révolution
     * @param epsi
     *            Longitude à J2010
     * @param omega
     *            Longitude au périgée
     * @param e
     *            Excentricité de l'orbite
     * @param a
     *            Demi grand-axe de l'orbite
     * @param i
     *            Inclinaison de l'orbite à l'écliptique
     * @param Omega
     *            Longitude du nœud ascendant
     * @param tetha0
     *            Taille angulaire des planètes à 1 UA
     * @param V0
     *            Magnitude des planètes à 1 UA
     */
    PlanetModel(String frenchName, double Tp, double epsi, double omega,
            double e, double a, double i, double Omega, double tetha0,
            double V0) {

        this.frenchName = frenchName;
        this.Tp = Tp;
        this.epsi = Angle.ofDeg(epsi);
        this.omega = Angle.ofDeg(omega);
        this.e = e;
        this.a = a;
        this.i = Angle.ofDeg(i);
        this.Omega = Angle.ofDeg(Omega);
        this.tetha0 = Angle.ofArcsec(tetha0);
        this.V0 = V0;
    }

    /**
     * Représente une planète à un instant donné
     */
    @Override
    public Planet at(double daysSinceJ2010,
            EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        double TROPIC_YEAR = TAU / 365.242191;

        double meanAno = TROPIC_YEAR * daysSinceJ2010 / Tp + epsi - omega;

        double trueAno = meanAno + 2 * e * sin(meanAno);

        double r = (a * (1 - pow(e, 2))) / (1 + e * cos(trueAno));

        double l = trueAno + omega;

        double phi = asin(sin(l - Omega) * sin(i));

        double rPrime = r * cos(phi);

        double lPrime = atan2(sin(l - Omega) * cos(i), cos(l - Omega)) + Omega;

        // vauleur pour la Terre

        double M = TROPIC_YEAR * daysSinceJ2010 / EARTH.Tp + EARTH.epsi
                - EARTH.omega;
        double V = M + 2 * EARTH.e * sin(M);
        double R = (EARTH.a * (1 - pow(EARTH.e, 2))) / (1 + EARTH.e * cos(V));
        double L = V + EARTH.omega;

        //

        double lambda;
        if (a < 1) {

            lambda = PI + L + atan2(rPrime * sin(L - lPrime),
                    R - rPrime * cos(L - lPrime));

        } else {

            lambda = lPrime
                    + atan2(R * sin(lPrime - L), rPrime - R * cos(lPrime - L));

        }

        double beta = atan((rPrime * tan(phi) * sin(lambda - lPrime))
                / (R * sin(lPrime - L)));

        EclipticCoordinates eclipCoord = EclipticCoordinates
                .of(Angle.normalizePositive(lambda), beta);

        EquatorialCoordinates equaCoord = eclipticToEquatorialConversion
                .apply(eclipCoord);

        //
        double pPow2 = pow(R, 2) + pow(r, 2)
                - 2 * R * r * cos(l - L) * cos(phi);

        double p = sqrt(pPow2);

        double tetha = tetha0 / p;

        double F = (1 + cos(lambda - l)) / 2;

        double m = V0 + 5 * log10((r * p) / sqrt(F));

        return new Planet(frenchName, equaCoord, (float) tetha, (float) m);
    }
}