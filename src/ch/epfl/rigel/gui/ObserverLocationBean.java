package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableObjectValue;


/**
 * Est un bean JavaFX contenant la position de l'observateur, en degrés.
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public class ObserverLocationBean {

    private final DoubleProperty lonDeg = new SimpleDoubleProperty(0);
    private final DoubleProperty latDeg = new SimpleDoubleProperty(0);
    private final ObservableObjectValue<GeographicCoordinates> coordinates = Bindings
            .createObjectBinding(() -> GeographicCoordinates.ofDeg(lonDeg.get(),
                    latDeg.get()), lonDeg, latDeg);

    /**
     * @return la propriété lonDeg
     */
    public DoubleProperty lonDegProprety() {
        return lonDeg;
    }

    /**
     * @return le contenu de la propriété lonDeg
     */
    public Double getLonDeg() {
        return lonDeg.get();
    }

    /**
     * Modifie le contenu de la propriété lonDeg
     */
    public void setLonDeg(Double lonDeg) {
        this.lonDeg.set(lonDeg);
    }

    /**
     * @return la propriété latDeg
     */
    public DoubleProperty latDegProprety() {
        return latDeg;
    }

    /**
     * @return le contenu de la propriété latDeg
     */
    public Double getLatDeg() {
        return latDeg.get();
    }

    /**
     * Modifie le contenu de la propriété latDeg
     */
    public void setLatDeg(Double latDeg) {
        this.latDeg.set(latDeg);
    }

    /**
     * @return le Bindings coordinates
     */
    public ObservableObjectValue<GeographicCoordinates> coordinatesProperty() {
        return coordinates;
    }

    /**
     * Modifie le contenu du Bindings coordinates
     */
    public GeographicCoordinates getCoordinates() {
        return coordinates.get();
    }

    /**
     * Modifie le contenu du Bindings coordinates
     */
    public void setCoordinates(GeographicCoordinates coordinates) {
        lonDeg.set(coordinates.lonDeg());
        latDeg.set(coordinates.latDeg());
    }
}