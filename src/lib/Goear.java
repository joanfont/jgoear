package lib;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import search.SearchTableModel;

public class Goear extends Thread {

    public static final String GOEAR_SEARCH = "http://www.goear.com/search";
    public static final String GOEAR_URL = "http://www.goear.com/action/sound/get";
    public static final String GOEAR_PLAY = "http://www.goear.com/listen";

    private final ArrayList<Song> songs;
    private final String query;
    private SearchTableModel tableModel;

    public Goear(String query) {
        this.query = query;
        this.songs = new ArrayList();
    }

    public Goear(String query, SearchTableModel stb) {
        songs = new ArrayList();
        this.query = query;
        this.tableModel = stb;
    }

    public void search() {
        String LIST_OF_ELEMENTS = "ol.board_list";
        String LIST_ITEM = "li.board_item";
        String META = ".meta";
        String SONG_ID = ".who";
        String SONG_ARTIST = ".description";
        String STATS = ".stats";
        String SONG_BRITATE = ".kbps";
        String SONG_LENGTH = ".length";
        int n, page, itemsPerPage, listOfElementsSize;
        boolean exit;
        String criteria = Utils.slugify(query);
        String baseURL = GOEAR_SEARCH + "/" + criteria;

        String songID, songName, songArtist, songBritate, songLength, songURL;
        Song song;

        Document searchResults;
        Elements listOfElements;
        n = 0;
        page = 1;

        try {
            itemsPerPage = Integer.parseInt(new SettingsFile().getItems());
            do {
                searchResults = Jsoup.connect(baseURL).get();
                if (n % 10 == 0) {
                    page++;
                    searchResults = Jsoup.connect(baseURL + "/" + page).get();
                }
                listOfElements = searchResults.select(LIST_OF_ELEMENTS + " " + LIST_ITEM);
                listOfElementsSize = listOfElements.size();
                exit = (listOfElementsSize == 0);
                if (!exit) {
                    for (Element e : listOfElements) {
                        songID = e.getElementsByTag("a").select(SONG_ID).attr("href").split("/")[4];
                        songName = e.getElementsByTag("div").select("h4").select("a").text();
                        songArtist = e.getElementsByTag("ul").select(META).select("li").select(SONG_ARTIST).text();
                        songBritate = e.getElementsByTag("ul").select(STATS).select(SONG_BRITATE).text();
                        songLength = e.getElementsByTag("ul").select(STATS).select(SONG_LENGTH).text();
                        songURL = GOEAR_URL + "/" + songID;
                        song = new Song(songID, songName, songArtist, songBritate, songLength, songURL);

                        songs.add(n, song);

                        if (tableModel != null) {
                            tableModel.addSong(song);
                        }
                        n++;
                        exit = (n == itemsPerPage);
                        if (exit) {
                            break;
                        }

                    }
                }
            } while (!exit);

        } catch (IOException ex) {

        }
    }

    public static Song getSongObjectFromURL(String url) {
        Song song = null;
        try {
            Document d = Jsoup.connect(url).get();
            String songID = url.split("/")[4];

            String songName = d.select("#soundtitle").text();
            String songArtist = "";
            String songBritate = "";
            String songLength = "";
            String songURL = GOEAR_URL + "/" + songID;

            song = new Song(songID, songName, songArtist, songBritate, songLength, songURL);

        } catch (IOException ex) {
        }

        return song;
    }

    public void clearSongs() {
        songs.clear();
    }

    public ArrayList getSongs() {
        return this.songs;
    }

    public Song getSong(int idx) {
        return this.songs.get(idx);
    }

    @Override
    public void run() {
        this.search();
    }
}
