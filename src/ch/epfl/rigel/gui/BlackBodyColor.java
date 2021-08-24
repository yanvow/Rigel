package ch.epfl.rigel.gui;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Couleur d'un corps noir
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public final class BlackBodyColor {

    private BlackBodyColor(){}

    private final static String BBR_CATALOGUE_NAME = "/bbr_color.txt";

    private final static Charset c = StandardCharsets.US_ASCII;

    private final static  ClosedInterval INTERVAL_TEMPERATURE = ClosedInterval
            .of(1000, 40000);

    private final static Map<Integer, Color> colorMap = map();

    /**
     * retourne la couleur correspondante à une température exprimée en degrés
     * Kelvin
     *
     * @param kelvTemp
     *            la température
     * @return la couleur
     * @throws IllegalArgumentException
     *             si la température n'est pas dans la plage couverte par le
     *             fichier de référence
     * @throws IOException
     */
    public static Color colorForTemperature(int kelvTemp) throws IllegalArgumentException {

        Preconditions.checkInInterval(INTERVAL_TEMPERATURE, kelvTemp);

        while (kelvTemp % 100 != 0){
            if( kelvTemp % 100 >= 50){
                kelvTemp += 1;
            }else{
                kelvTemp -= 1;
            }
        }
        return colorMap.get(kelvTemp);
    }

    /**
     *  crée une map reliant les températures à leur couleur
     *
     * @return la map
     */
    private static Map<Integer, Color> map() {

        BufferedReader br = new BufferedReader(
                new InputStreamReader(BlackBodyColor.class.getResourceAsStream(BBR_CATALOGUE_NAME), c));

        Map<Integer, Color> map = new HashMap<>();

        String str;
        try {
            while ((str = br.readLine()) != null ) {

                if (!(str.substring(0, 1).equals("#"))
                        && str.substring(10, 15).equals("10deg")) {

                    if(str.charAt(1) == ' '){
                        map.put(Integer.valueOf(str.substring(2, 6)), Color.web(str.substring(80, 87)));
                    }else{
                        map.put(Integer.valueOf(str.substring(1, 6)), Color.web(str.substring(80, 87)));
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return map;
    }
}