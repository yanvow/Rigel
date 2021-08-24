package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Classe étant un bean Java Fx contenant l'instant d'observation, c-à-d le
 * triplet (date, heure, fuseau horaire) d'observation.
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public final class DateTimeBean {

    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>(null);
    private final ObjectProperty<LocalTime> time = new SimpleObjectProperty<>(null);
    private final ObjectProperty<ZoneId> zone = new SimpleObjectProperty<>((null));

    /**
     * retourne l'instant d'observation sous la forme d'une valeur de type
     * ZonedDateTime
     *
     * @return l'instant d'observation sous la forme d'une valeur de type
     * ZonedDateTime
     */
    public ZonedDateTime getZonedDateTime() {
        return ZonedDateTime.of(getDate(), getTime(), getZone());
    }

    /**
     * Modifie l'instant d'observation pour qu'il soit égal à la valeur de type
     * ZonedDateTime qu'on lui passe en argument.
     *
     * @param zonedDateTime l'instant d'observation
     */
    public void setZonedDateTime(ZonedDateTime zonedDateTime) {
        setDate(zonedDateTime.toLocalDate());
        setTime(zonedDateTime.toLocalTime());
        setZone(zonedDateTime.getZone());
    }

    /**
     * méthode donnant accès à la propriété date elle-même,
     *
     * @return a propriété date elle-même,
     */
    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    /**
     * méthode donnant accès au contenu de la propriété date
     *
     * @return le contenu de la propriété date
     */
    public LocalDate getDate() {
        return date.get();
    }

    /**
     * méthode permettant de modifier le contenu de la propriété date
     *
     * @param date la date
     */
    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    /**
     * méthode donnant accès à la propriété time elle-même,
     *
     * @return a propriété time elle-même,
     */
    public ObjectProperty<LocalTime> timeProperty() {
        return time;
    }

    /**
     * méthode donnant accès au contenu de la propriété time
     *
     * @return le contenu de la propriété time
     */
    public LocalTime getTime() {
        return time.get();
    }

    /**
     * méthode permettant de modifier le contenu de la propriété time
     *
     * @param time le time
     */
    public void setTime(LocalTime time) {
        this.time.set(time);
    }

    /**
     * méthode donnant accès à la propriété zone elle-même,
     *
     * @return a propriété zone elle-même,
     */
    public ObjectProperty<ZoneId> zoneProperty() {
        return zone;
    }

    /**
     * méthode donnant accès au contenu de la propriété zone
     *
     * @return le contenu de la propriété zone
     */
    public ZoneId getZone() {
        return zone.get();
    }

    /**
     * méthode permettant de modifier le contenu de la propriété zone
     *
     * @param zone la zone
     */
    public void setZone(ZoneId zone) {
        this.zone.set(zone);
    }
}