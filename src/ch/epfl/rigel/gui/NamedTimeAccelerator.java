package ch.epfl.rigel.gui;

import java.time.Duration;

import static ch.epfl.rigel.gui.TimeAccelerator.continuous;
import static ch.epfl.rigel.gui.TimeAccelerator.discrete;

/**
 * Représente un accélérateur de temps nommé, c'est-à-dire une paire (nom,
 * accélérateur).
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public enum NamedTimeAccelerator {

    /**
     * Les six membres disponibles.
     */
    TIMES_1("1x", continuous(1)),
    TIMES_30("30x", continuous(30)),
    TIMES_300("300", continuous(300)),
    TIMES_3000("3000", continuous(3000)),
    DAY("jour", discrete(60, Duration.ofHours(24))),
    SIDEREAL_DAY("jour sidéral", discrete(60, Duration.ofHours(23).plusMinutes(56).plusSeconds(4)));

    private final String name;
    private final TimeAccelerator accelerator;

    /**
     * Constructeur privé
     *
     * @param name        le nom
     * @param accelerator l'accélérateur
     */
    NamedTimeAccelerator(String name, TimeAccelerator accelerator) {
        this.name = name;
        this.accelerator = accelerator;
    }

    /**
     * retourne le nom de la paire
     *
     * @return le nom de la paire
     */
    public String getName() {
        return name;
    }

    /**
     * retourne l'accélérateur de la paire
     *
     * @return l'accélérateur de la paire
     */
    public TimeAccelerator getAccelerator() {
        return accelerator;
    }

    /**
     * Redéfinition de la méthode de Object qui retourne le nom de la paire
     *
     * @return le nom de la paire
     */
    @Override
    public String toString() {
        return getName();
    }
}