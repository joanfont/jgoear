package lib;

import java.util.Observable;

public class Song extends Observable {

    private final String songID;
    private final String songName;
    private final String songArtist;
    private final String songAlbum;
    private final String songBritate;
    private final String songURL;
    private final String songLength;
    public static final String FILE_EXT = "mp3";

    public Song(String songID, String songName, String songArtist, String songBritate, String songLength, String songURL) {
        this.songID = songID;
        this.songName = Utils.capitalize(songName);
        this.songArtist = Utils.capitalize(songArtist);
        this.songAlbum = "";
        this.songBritate = songBritate;
        this.songLength = songLength;
        this.songURL = songURL;

    }


    public String getSongID() {
        return songID;
    }

    public String getSongName() {
        return songName;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public String getSongAlbum() {
        return songAlbum;
    }

    public String getSongBritate() {
        return songBritate;
    }

    public String getSongLength() {
        return songLength;
    }

    public String getSongURL() {
        return songURL;
    }

    public String generateFileName() {
        return this.songName + " - " + this.songArtist + "." + FILE_EXT;
    }

    @Override
    public String toString() {
        return this.getSongName() + " - " + this.getSongArtist();
    }
}
