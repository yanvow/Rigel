package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Est un bean JavaFX contenant les paramètres déterminant la portion du ciel
 * visible sur l'image.
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public class ViewingParametersBean {

    private final ObjectProperty<Double> fieldOfViewDeg = new SimpleObjectProperty<>();
    private final ObjectProperty<HorizontalCoordinates> center = new SimpleObjectProperty<>();

    /**
     * @return la propriété fieldOfViewDeg
     */
    public ObjectProperty<Double> fieldOfViewDegProperty() {
        return fieldOfViewDeg;
    }

    /**
     * le contenu de fieldOfViewDeg
     *
     * @return le contenu de fieldOfViewDeg
     */
    public Double getFieldOfViewDeg() {
        return fieldOfViewDeg.get();
    }


    /**
     * Modifie le contenu de fieldOfViewDeg
     *
     * @param fieldOfViewDeg le fieldOfViewDeg
     */
    public void setFieldOfViewDeg(Double fieldOfViewDeg) {
        this.fieldOfViewDeg.set(fieldOfViewDeg);
    }

    /**
     * @return la propriété center
     */
    public ObjectProperty<HorizontalCoordinates> centerProperty() {
        return center;
    }


    /**
     * le contenu de center
     *
     * @return le contenu de center
     */
    public HorizontalCoordinates getCenter() {
        return center.get();
    }

    /**
     * Modifie le contenu de center
     *
     * @param center le center
     */
    public void setCenter(HorizontalCoordinates center) {
        this.center.set(center);
    }
}