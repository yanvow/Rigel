package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.math.Angle.toDeg;

/**
 * Coordonnées sphérique
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
abstract class SphericalCoordinates {

    private final double lon;
    private final double lat;

    /**
     * @param lon
     *            la longitude
     * @param lat
     *            latitude
     */
    SphericalCoordinates(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    /**
     * @return retourne la longitude
     */
    double lon() {
        return lon;
    }

    /**
     * @return retourne la longitude en degrés
     */
    double lonDeg() {
        return toDeg(lon());
    }

    /**
     * @return retourne la latitude
     */
    double lat() {
        return lat;
    }

    /**
     * @return retourne la latitude en degrés
     */
    double latDeg() {
        return toDeg(lat());
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
}