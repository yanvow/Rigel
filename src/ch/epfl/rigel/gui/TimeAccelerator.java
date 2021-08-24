package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * Interface fonctionnelle représentant un « accélérateur de temps »,
 * c'est-à-dire une fonction permettant de calculer le temps simulé — et
 * généralement accéléré — en fonction du temps réel.
 * 
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */

@FunctionalInterface
public interface TimeAccelerator {

    /**
     * Calcule le temps simulé et le retourne sous la forme d'une nouvelle
     * instance de ZonedDateTime.
     * 
     * @param t0
     *            le temps simulé initial , représenté par une valeur de type
     *            ZonedDateTime
     * @param delta
     *            le temps réel écoulé depuis le début de l'animation
     *             représenté par une valeur de type long exprimée en
     *            nanosecondes
     * @return le temps simulé et le retourne sous la forme d'une nouvelle
     *         instance de ZonedDateTime.
     */
    ZonedDateTime adjust(ZonedDateTime t0, long delta);

    /**
     * retourne un accélérateur continu en fonction du facteur d'accélération
     * alpha
     * 
     * @param alpha
     *            facteur d'accélération entier
     * @return un accélérateur continu en fonction du facteur d'accélération
     */
    static TimeAccelerator continuous(int alpha) {
        return (t, d) -> (t.plusNanos(alpha * d));
    }

    /*
     * static TimeAccelerator discrete(int epsi, Duration S) { return (t, d) ->
     * (t.plusNanos(Math.floorDiv(epsi * d, (int) 1e9) * S.toNanos())); }
     */

    /**
     * retourne un accélérateur discret en fonction d'une fréquence d'avancement
     * epsi et du pas S.
     * 
     * @param epsi
     *            la fréquence d'avancement (entière)
     * @param S
     *            le pas
     * @returnun un accélérateur discret en fonction d'une fréquence
     *           d'avancement epsi et du pas S.
     */
    static TimeAccelerator discrete(int epsi, Duration S) {
        return (t, d) -> (t
                .plus(S.multipliedBy((long) (1E-9 * Math.floor(epsi * d)))));
    }
}