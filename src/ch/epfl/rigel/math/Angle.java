package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

/**
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public class Angle {

    /**
     * Représente la constante τ = 2 * PI
     */
    public final static double TAU = 2 * Math.PI;

    private static final double HR_PER_RAD = 24d / TAU;
    private static final double DEG_PER_SEC = 1d / 3600;
    private static final double DEG_PER_MIN = 1d / 60;

    private static final RightOpenInterval INTERVAL_ZERO_TAU = RightOpenInterval
            .of(0, TAU);
    private static final RightOpenInterval INTERVAL_ZERO_60 = RightOpenInterval
            .of(0, 60);

    /**
     * Réduit l'angle donné à l'intervalle [0,τ[.
     *
     * @param rad
     *            angle en radian
     * @return la réduction
     */
    public static double normalizePositive(double rad) {
        return INTERVAL_ZERO_TAU.reduce(rad);
    }

    /**
     * Convertit les secondes d'arc , en radian.
     *
     * @param sec
     *            angle en seconde
     * @return angle en radian
     */
    public static double ofArcsec(double sec) {
        return ofDeg(sec * DEG_PER_SEC);
    }

    /**
     * Convertit l'angle donné en degrès minute et seconde, en radian.
     *
     * @param deg
     *            angle en degrès
     * @param min
     *            angle en minute, entre 0 (inclus) et 60 (exclus)
     * @param sec
     *            angle en seconde, entre 0 (inclus) et 60 (exclus)
     * @return angle en radian
     * @throws IllegalArgumentException
     *             si les minutes données ne sont pas comprises entre 0 (inclus)
     *             et 60 (exclus), ou si les secondes ne sont pas comprises
     *             entre 0 (inclus) et 60 (exclus).
     */
    public static double ofDMS(int deg, int min, double sec)
            throws IllegalArgumentException {
        
        Preconditions.checkArgument(deg>=0);

        return ofDeg(deg
                + Preconditions.checkInInterval(INTERVAL_ZERO_60, min)
                        * DEG_PER_MIN
                + Preconditions.checkInInterval(INTERVAL_ZERO_60, sec)
                        * DEG_PER_SEC);
    }

    /**
     * Convertit l'angle donné en degrès, en radian.
     *
     * @param deg
     *            angle en degrès
     * @return angle en radian
     */
    public static double ofDeg(double deg) {
        return Math.toRadians(deg);
    }

    /**
     * Convertit l'angle donné en radian, en degrès.
     *
     * @param rad
     *            angle en radian
     * @return angle en degrès
     */
    public static double toDeg(double rad) {
        return Math.toDegrees(rad);
    }

    /**
     * Convertit l'angle en heures donné, en radian.
     *
     * @param hr
     *            angle en heure
     * @return angle en radian
     */
    public static double ofHr(double hr) {
        return hr / HR_PER_RAD;
    }

    /**
     * Convertit l'angle donnée en radian, en heure.
     *
     * @param rad
     *            angle en radian
     * @return angle en heure
     */
    public static double toHr(double rad) {
        return rad * HR_PER_RAD;
        }
    
}