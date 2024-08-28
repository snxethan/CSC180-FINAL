package csc180.townsend.ethan.finalcsc180.Controller.Scraper;


import csc180.townsend.ethan.finalcsc180.Controller.DatabaseController;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SongScraper {

    private static final String URL = "https://kworb.net/spotify/country/global_daily.html";
    static DatabaseController dbController = new DatabaseController();

    public static String request(){
        //starts fetching info from URL
        HttpGet request = new HttpGet(URL);
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            System.out.println("Scraper Request: " + e.getMessage());
        }
        return null;
    }

    @SuppressWarnings("all")
    public static void webScraping() {
        String response = request();
        if (response == null) {
            System.out.println("Failed to fetch data from the URL.");
            return;
        }

        Document doc = Jsoup.parse(response);

        // Select rows containing artist and track information
        Elements rows = doc.select("tr:has(a[href*='../track'])");

        for (Element row : rows) {
            // Select artist elements within the row
            Elements artistElements = row.select("a[href*='../artist']");
            // Select track element within the row
            Element trackElement = row.selectFirst("a[href*='../track']");

            //ArrayList<Integer> artistIDs = new ArrayList<>();

            //add to database
            for (Element artistElement : artistElements) {
                //checks the database to see if person exists
                int possibleID = dbController.checkArtistExists(artistElement.toString());
                if(possibleID == 0){
                    //add to database, then save the ID to artistIDs
                    dbController.addArtistToDatabase(artistElement.toString());
                    //artistIDs.add(dbController.checkArtistExists(artistElement.toString()));
                } else {
                    //save ID's to artistIDs
                    //artistIDs.add(dbController.checkArtistExists(artistElement.toString()));
                }
            }

            // Print the track
        }
    }

    public static List<Song> getTopSongs(){
        //FIXME: FILTER OUT DUPLICATES (there should be none)
        String response = request();
        if (response == null) {
            System.out.println("Failed to fetch data from the URL.");
            return null;
        }

        Document doc = Jsoup.parse(response);

        // Select rows containing artist and track information
        Elements rows = doc.select("tr:has(a[href*='../track'])");

        List<Song> topSongs = new ArrayList<>();
        for (Element row : rows) {
            if (topSongs.size() >= 100) break;
            Element trackElement = row.selectFirst("a[href*='../track']");
            Elements artistElements = row.select("a[href*='../artist']");
            if (trackElement != null) {
                List<String> artists = new ArrayList<>();
                for (Element artistElement : artistElements) {
                    artists.add(artistElement.text());
                }
                topSongs.add(new Song(trackElement.text(), artists));
            }
        }
        return topSongs;
    }

    public static List<String> getArtists() {
        //FIXME: FILTER OUT DUPLICATES
        String response = request();
        if (response == null) {
            System.out.println("Failed to fetch data from the URL.");
            return null;
        }

        Document doc = Jsoup.parse(response);

        // Select rows containing artist and track information
        Elements rows = doc.select("tr:has(a[href*='../artist'])");

        List<String> artists = new ArrayList<>();
        for (Element row : rows) {
            // Select artist elements within the row
            Elements artistElements = row.select("a[href*='../artist']");
            for (Element artistElement : artistElements) {
                //artists.add(artistElement.text());
                //checks the database to see if person exists
                int possibleID = dbController.checkArtistExists(artistElement.text());
                if(possibleID == 0){
                    //add to database, then save the ID to artistIDs
                    dbController.addArtistToDatabase(artistElement.text());
                    artists.add(artistElement.text());
                    //artistIDs.add(dbController.checkArtistExists(artistElement.toString()));
                } else {
                    //save ID's to artistIDs
                    //artistIDs.add(dbController.checkArtistExists(artistElement.toString()));
                }
            }
        }
        return dbController.returnAllArtists();
    }

    public static class Song {
        private final String title;
        private final List<String> artists;

        public Song(String title, List<String> artists) {
            this.title = title;
            this.artists = artists;
        }

        public String getTitle() {
            return title;
        }

        public List<String> getArtists() {
            return artists;
        }
    }
}