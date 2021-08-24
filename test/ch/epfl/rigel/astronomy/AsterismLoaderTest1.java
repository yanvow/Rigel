/*package ch.epfl.rigel.astronomy;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class AsterismLoaderTest1 {
    private static final String HYG_CATALOGUE_NAME = "/hygdata_v3.csv";
    private static final String ASTERISM_CATALOGUE_NAME = "/asterisms.txt";

    @Test
    void asterismDatabaseIsCorrectlyInstalled() throws IOException {
        try (InputStream asterismStream = getClass()
                .getResourceAsStream(ASTERISM_CATALOGUE_NAME)) {
            assertNotNull(asterismStream);
        }
    }

   @Test
    void load() throws IOException {
        StarCatalogue.Builder builder = new StarCatalogue.Builder();
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            builder = builder.loadFrom(hygStream, HygDatabaseLoader.INSTANCE);
        }
        try (InputStream asterismStream = getClass()
                .getResourceAsStream(ASTERISM_CATALOGUE_NAME)) {
            builder = builder.loadFrom(asterismStream, AsterismLoader.INSTANCE);
            StarCatalogue catalogue = builder.build();
            Asterism asterismTest = catalogue.asterisms().get(0);
            Asterism asterism95 = catalogue.asterisms().get(94);
            //check that the asterisms work
            assertEquals(7607, asterismTest.stars().get(0).hipparcosId());
            assertEquals(24436, asterism95.stars().get(0).hipparcosId());
            assertEquals(catalogue.stars().get(1213).hipparcosId(), asterism95.stars().get(3).hipparcosId());
            //check that asterismIndices and asterisms work
            assertEquals(7607, catalogue.stars().get(catalogue.asterismIndices(asterismTest).get(0)).hipparcosId());
            assertEquals(1019, catalogue.asterismIndices(asterism95).get(0));
        }
    }
}
*/