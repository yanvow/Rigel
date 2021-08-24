package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.*;

import java.time.ZonedDateTime;

/**
 * Classe représentant un « animateur de temps »
 * 
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */

public final class TimeAnimator extends AnimationTimer {

    private final DateTimeBean dateTimeBean;
    private final BooleanProperty running = new SimpleBooleanProperty(false);
    private final ObjectProperty<TimeAccelerator> accelerator = new SimpleObjectProperty<>();

    private Boolean isHandle;
    private long minuteur;
    private ZonedDateTime firstZone;

    public TimeAnimator(DateTimeBean dateTimeBean) {
        this.dateTimeBean = dateTimeBean;

    }

    /**
     * méthode donnant accès à la propriété running elle-même,
     * 
     * @return a propriété running elle-même,
     */
    public BooleanProperty runningProperty() {
        return running;
    }

    /**
     * méthode donnant accès au contenu de la propriété running elle-même,
     * 
     * @return le contenu de la propriété running elle-même,
     */
    public ReadOnlyBooleanProperty getRunning() {
        return SimpleBooleanProperty.readOnlyBooleanProperty(running);
    }

    /**
     * méthode permettant de modifier le contenu de la propriété running
     * 
     * @param running
     *            l'état de l'animateur
     */
    public void setRunning(Boolean b) {
        running.setValue(b);
    }

    /**
     * Un minuteur est démarré
     */
    @Override
    public void start() {
        isHandle = true;
        running.setValue(true);
        super.start();
    }

    /**
     * Un minuteur est stoppé
     */
    @Override
    public void stop() {
        running.setValue(false);
        super.stop();
    }

    /**
     * méthode donnant accès à la propriété date elle-même,
     * 
     * @return a propriété date elle-même,
     */
    public ObjectProperty<TimeAccelerator> acceleratorProperty() {
        return accelerator;
    }

    /**
     * méthode permettant de modifier le contenu de la propriété accelerator
     * 
     * @param accelerator
     *            l'accélérateur
     */
    public void setAccelerator(TimeAccelerator accelerator) {
        this.accelerator.set(accelerator);
    }

    /**
     * méthode donnant accès au contenu de la propriété accelerator elle-même,
     * 
     * @return le contenu de la propriété accelerator elle-même,
     */
    public TimeAccelerator getAccelerator() {
        return accelerator.get();
    }

    /**
     * handle est automatiquement appelée environ 60 fois par secondes afin de
     * faire progresser l'animation. Elle reçoit en argument une valeur
     * représentant le nombre de nanosecondes écoulées depuis un instant de
     * départ non spécifié.
     */
    @Override
    public void handle(long l) {

        if (isHandle) {

            this.minuteur = l;
            this.firstZone = dateTimeBean.getZonedDateTime();
            isHandle = false;

        } else {

            dateTimeBean.setZonedDateTime(
                    getAccelerator().adjust(firstZone, l - minuteur));
        }
    }
}