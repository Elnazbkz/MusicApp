import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ArtistTest {
    private Artist artist;

    @Before
    public void setUp() {
        artist = new Artist("123ABC_%", "John Doe", "New York|NY|USA", "01/01/1990",
                "A talented artist with a passion for music",
                new ArrayList<>(Arrays.asList("Singer", "Songwriter")),
                new ArrayList<>(Arrays.asList("Pop", "Rock")),
                new ArrayList<>(Arrays.asList("2005 - Best New Artist", "2010 - Song of the Year")));
    }

    @Test
    public void testAddArtistWithValidInput() {
        // Use a ByteArrayInputStream to provide input to the Scanner
    	Map<String, String> ValidArtistInfo = new HashMap<>();
    	ValidArtistInfo.put("ID", "768RTYHG_%");
    	ValidArtistInfo.put("Name", "John Doe");
    	ValidArtistInfo.put("Address", "New York|NY|USA");
    	ValidArtistInfo.put("Birthdate", "01/01/1990");
    	ValidArtistInfo.put("Bio", "A talented artist with a passion for music");
    	ValidArtistInfo.put("Occupations", "Singer, Songwriter");
    	ValidArtistInfo.put("Genres", "Pop, Jazz");
    	ValidArtistInfo.put("Awards", "2005 - Best New Artist, 2010 - Song of the Year");
    	
        assertTrue(artist.addArtist(ValidArtistInfo));

        // You can add additional assertions to check if the artist information was added correctly
    }

    @Test
    public void testAddArtistWithInvalidInput() {
    	// Use a ByteArrayInputStream to provide input to the Scanner
    	Map<String, String> ValidArtistInfo = new HashMap<>();
    	ValidArtistInfo.put("ID", "123refds&%");
    	ValidArtistInfo.put("Name", "John Doe");
    	ValidArtistInfo.put("Address", "New York|USA");
    	ValidArtistInfo.put("Birthdate", "1/1/1990");
    	ValidArtistInfo.put("Bio", "A talented artist with a passion for music");
    	ValidArtistInfo.put("Occupations", "Singer, Songwriter");
    	ValidArtistInfo.put("Genres", "Pop, Rock");
    	ValidArtistInfo.put("Awards", "2005 - Best New Artist, 2010 - Song of the Year");
    	
        assertFalse(artist.addArtist(ValidArtistInfo));

        // You can add additional assertions to check for error messages or other criteria for failure
    }

    @Test
    public void TestValidArtistID() {
        // Prepare test data
        Artist foundArtist = Artist.validateArtistID("987ELIBE_%");
        // Check if the artist ID is found
        assertNotNull(foundArtist);
    }

    @Test
    public void TestInValidArtistID() {
        // Prepare test data
        Artist foundArtist = Artist.validateArtistID("011rewqqy+");
        assertNull(foundArtist);
        // Check if the artist ID is not found
    }

    @Test
    public void TestValidaddArtistToTxtFile() {
        Map<String, String> ArtistInfo = new HashMap<>();
        ArtistInfo.put("ID", "786EWQDF_%");
        ArtistInfo.put("Name", "John Doe");
        ArtistInfo.put("Address", "Perth|WA|Australia");
        ArtistInfo.put("Birthdate", "20/05/1963");
        ArtistInfo.put("Bio", "A talented singer and songwriter born in Australia in 1963.");
        ArtistInfo.put("Occupations", "Singer , Songwriter");
        ArtistInfo.put("Genres", "Rock , Classical");
        ArtistInfo.put("Awards", "1987 - Top Artist");

        assertTrue(Artist.addArtistToTxtFile(ArtistInfo));
    }
    
    @Test
    public void TestInValidaddArtistToTxtFile() {
        Map<String, String> ArtistInfo = new HashMap<>();
        ArtistInfo.put("ID", "987ELIBE_%");
        ArtistInfo.put("Name", "John Doe");
        ArtistInfo.put("Address", "Perth|Australia");
        ArtistInfo.put("Birthdate", "20/05/1963");
        ArtistInfo.put("Bio", "A talented singer and songwriter born in Australia in 1963.");
        ArtistInfo.put("Occupations", "Singer , Songwriter");
        ArtistInfo.put("Genres", "Rock , Classical");
        ArtistInfo.put("Awards", "1987 - Top Artist");

        assertFalse(Artist.addArtistToTxtFile(ArtistInfo));
    }
}
