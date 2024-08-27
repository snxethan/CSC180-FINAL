package csc180.townsend.ethan.finalcsc180.Controller.Scraper;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SongScraper {

    private static final String URL = "https://kworb.net/spotify/listeners.html";

    public static String request(){
        //starts fetching info from URL
        HttpGet request = new HttpGet(URL);
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            return responseBody;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
//
//    public static List<String> getTop200Songs() throws IOException {
//        List<String> topSongs = new ArrayList<>();
//        Document doc = (Document) Jsoup.connect(URL).get();
//        Elements rows = doc.select("table.chart tr");
//
//        for (Element row : rows) {
//            Elements columns = row.select("td");
//            if (columns.size() > 1) {
//                String artistAndTitle = columns.get(1).text();
//                topSongs.add(artistAndTitle);
//            }
//        }
//        return topSongs;
//    }
}