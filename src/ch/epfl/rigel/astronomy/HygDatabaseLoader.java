package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * Chargeur de catalogue HYG
 *
 * @author Nathan Chettrit (313002)
 * @author Yanis Seddik (310086)
 */
public enum HygDatabaseLoader implements StarCatalogue.Loader {
    INSTANCE;

    private final static Charset c = StandardCharsets.US_ASCII;

    /**
     * Ajoute au bâtisseur de catalogue toutes les étoiles obtenues du catalogue
     * HYG
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

        BufferedReader bf = new BufferedReader(
                new InputStreamReader(inputStream, c));

        bf.readLine();

        String str;
        while ((str = bf.readLine()) != null) {

            String tab[] = str.split(",");

            int hip = Integer.parseInt(check(tab[1], "0"));
            String bayer = check(tab[27], "?");
            String con = tab[29];
            String name = check(tab[6], bayer + " " + con);
            double mag = Double.parseDouble(check(tab[13], "0"));
            double ci = Double.parseDouble(check(tab[16], "0"));
            double rarad = Double.parseDouble(tab[23]);
            double decrad = Double.parseDouble(tab[24]);

            builder.addStar(
                    new Star(hip, name, EquatorialCoordinates.of(rarad, decrad),
                            (float) mag, (float) ci));
        }
        
        bf.close();
        
    }

    /**
     * Vérifie si la valeur donnée (tru) est vide, si c'est le cas on retourne
     * la valeur par défault (defult), sinon on retourne la valeur donnée
     *
     * @param tru
     *            vraie valeur
     * @param defult
     *            valeur par défault
     * @return la valeur donné
     */
    private String check(String tru, String defult) {
        return tru.isEmpty() ? defult : tru;
    }
}