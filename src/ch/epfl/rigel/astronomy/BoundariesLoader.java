package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Chargeur de catalogue des limites de Constellation
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public enum BoundariesLoader implements StarCatalogue.Loader {
    INSTANCE;

    private final static Charset c = StandardCharsets.US_ASCII;

    /**
     * Ajoute au bâtisseur de catalogue toutes les points limite des constellations
     *
     * @param inputStream flux d'entréé
     * @param builder     bâtisseur
     * @throws IOException en cas d'erreur d'entrée/sortie.
     */
    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder)
            throws IOException {

        BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, c));

        String newName = "";
        String oldName = "";
        double ra;
        double dec;
        List<EquatorialCoordinates> list = new ArrayList<>();
        boolean b = true;

        String str;
        while ((str = bf.readLine()) != null) {

            if (!(str.substring(0, 1).equals("#"))) {

                String[] tab = str.split(" ");

                newName = b ? oldName : tab[2];

                if (!oldName.equals(newName)) {
                    Boundaries boundaries = new Boundaries(oldName, list);
                    builder.addBoundaries(boundaries);
                    oldName = newName;
                    list.clear();
                }
                ra = Angle.ofHr(Double.parseDouble(tab[0]));
                dec = Angle.ofDeg(Double.parseDouble(tab[1]));
                list.add(EquatorialCoordinates.of(ra, dec));
                b = false;
            }
        }
        bf.close();
    }
}