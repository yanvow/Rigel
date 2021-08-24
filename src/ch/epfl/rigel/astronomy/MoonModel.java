package ch.epfl.rigel.astronomy;

import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

/**
 * Modèle de Lune
 * 
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 * 
 */
public enum MoonModel implements CelestialObjectModel<Moon> {

    MOON(Angle.ofDeg(91.929336), Angle.ofDeg(130.143076),
            Angle.ofDeg(291.682547), Angle.ofDeg(5.145396), 0.0549);

    private double l0;
    private double P0;
    private double N0;
    private double i;
    private double e;

    /**
     * @param l0
     *            Longitude moyenne
     * @param p0
     *            Longitude moyenne au périgée
     * @param n0
     *            Longitude du nœud ascendant
     * @param i
     *            Inclinaison de l'orbite
     * @param e
     *            Excentricité de l'orbite
     */
    private MoonModel(double l0, double p0, double n0, double i, double e) {
        this.l0 = l0;
        this.P0 = p0;
        this.N0 = n0;
        this.i = i;
        this.e = e;
    }

    /**
     * retourne la Lune à un instant donné
     */
    @Override
    public Moon at(double daysSinceJ2010,
            EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        // Calcul de la longitude orbitale :

        double lambdaSun = SunModel.SUN
                .at(daysSinceJ2010, eclipticToEquatorialConversion)
                .eclipticPos().lon();

        double MSun = SunModel.SUN
                .at(daysSinceJ2010, eclipticToEquatorialConversion)
                .meanAnomaly();

        double l = Angle.ofDeg(13.1763966) * daysSinceJ2010 + l0;

        double Mm = l - Angle.ofDeg(0.1114041) * daysSinceJ2010 - P0;

        double Ev = Angle.ofDeg(1.2739) * sin(2 * (l - lambdaSun) - Mm);

        double Ae = Angle.ofDeg(0.1858) * sin(MSun);

        double A3 = Angle.ofDeg(0.37) * sin(MSun);

        double MmPrime = Mm + Ev - Ae - A3;

        double Ec = Angle.ofDeg(6.2886) * sin(MmPrime);

        double A4 = Angle.ofDeg(0.214) * sin(2 * MmPrime);

        double lPrime = l + Ev + Ec - Ae + A4;

        double V = Angle.ofDeg(0.6583) * sin(2 * (lPrime - lambdaSun));

        double lDoublePrime = lPrime + V;

        // Calcul de la position écliptique :

        double N = N0 - Angle.ofDeg(0.0529539) * daysSinceJ2010;
        double NPrime = N - Angle.ofDeg(0.16) * sin(MSun);

        double lambdaM = atan2(sin(lDoublePrime - NPrime) * cos(i),
                cos(lDoublePrime - NPrime)) + NPrime;
        double betaM = asin(sin(lDoublePrime - NPrime) * sin(i));

        EclipticCoordinates eclipCoord = EclipticCoordinates
                .of(Angle.normalizePositive(lambdaM), betaM);

        EquatorialCoordinates equaCoord = eclipticToEquatorialConversion
                .apply(eclipCoord);

        // Phase de la Lune :

        double F = (1 - cos(lDoublePrime - lambdaSun)) / 2;

        // Taille angulaire de la Lune :

        double Rho = (1 - pow(e, 2)) / (1 + e * cos(MmPrime + Ec));
        double theta = Angle.ofDeg(0.5181) / Rho;

        return new Moon(equaCoord, (float) theta, (float) MmPrime, (float) F);
    }
}
