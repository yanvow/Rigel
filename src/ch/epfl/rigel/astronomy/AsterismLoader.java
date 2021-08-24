package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Chargeur de catalogue d'astérismes
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public enum AsterismLoader implements StarCatalogue.Loader {
    INSTANCE;

    private final static Charset c = StandardCharsets.US_ASCII;

    /**
     * Ajoute au bâtisseur de catalogue toutes les étoiles obtenues du catalogue
     * d'asterismes
     *
     * @param inputStream
     *            flux d'entréé
     * @param builder
     *            bâtisseur
     * @throws IOException
     *             en cas d'erreur d'entrée/sortie.
     */
    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder)
            throws IOException {

        BufferedReader br = new BufferedReader(
                new InputStreamReader(inputStream, c));

        Map<Integer, Star> map = new HashMap<>();
        for (Star s : builder.stars()) {
            map.put(s.hipparcosId(), s);
        }

        String str;
        while ((str = br.readLine()) != null) {

            String tab[] = str.split(",");

            List<Star> list = new ArrayList<>();

            for (String t : tab) {
                list.add(map.get(Integer.parseInt(t)));
            }

            builder.addAsterism(new Asterism(list));
        }
        br.close();
    }
}