package csc180.townsend.ethan.finalcsc180.Controller.Scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SongScraper {

    private static final String URL = "https://kworb.net/spotify/country/global_weekly.html";

    public static List<String> getTop200Songs() throws IOException {
        List<String> topSongs = new ArrayList<>();
        Document doc = (Document) Jsoup.connect(URL).get();
        Elements rows = doc.select("table.chart tr");

        for (Element row : rows) {
            Elements columns = row.select("td");
            if (columns.size() > 1) {
                String artistAndTitle = columns.get(1).text();
                topSongs.add(artistAndTitle);
            }
        }
        return topSongs;
    }
}