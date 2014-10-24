/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tag;

import java.io.File;
import java.io.IOException;
import lib.Song;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import lib.SettingsFile;

/**
 *
 * @author joanfont
 */
public class Tags {

    private AudioFile file;
    private Tag tag;
    private Song song;

    public Tags(String fileName) {
        try {
            this.file = AudioFileIO.read(new File(fileName));
            this.tag = this.file.getTagOrCreateAndSetDefault();
        } catch (CannotReadException ex) {
        } catch (IOException ex) {
        } catch (TagException ex) {
        } catch (ReadOnlyFileException ex) {
        } catch (InvalidAudioFrameException ex) {
        }
    }

    public Tags(Song song) {
        try {
            this.song = song;
            SettingsFile sf = new SettingsFile();
            String savePath = sf.getSavePath();
            String fileName = savePath+File.separator+song.generateFileName();
            this.file = AudioFileIO.read(new File(fileName));
            this.tag = this.file.getTagOrCreateAndSetDefault();
        } catch (CannotReadException ex) {
        } catch (IOException ex) {
        } catch (TagException ex) {
        } catch (ReadOnlyFileException ex) {
        } catch (InvalidAudioFrameException ex) {
        }

    }

    public void setAll() {
        this.setTitle(song.getSongName());
        this.setArtist(song.getSongArtist());
        this.setAlbum(song.getSongAlbum());
    }

    public void setTitle(String title) {
        try {
            this.tag.setField(FieldKey.TITLE, title);
            this.file.commit();
        } catch (CannotWriteException ex) {
        } catch (KeyNotFoundException ex) {
        } catch (FieldDataInvalidException ex) {
        }
    }

    public void setArtist(String artist) {
        try {
            this.tag.setField(FieldKey.ARTIST, artist);
            this.file.commit();
        } catch (CannotWriteException ex) {
        } catch (KeyNotFoundException ex) {
        } catch (FieldDataInvalidException ex) {
        }
    }

    public void setAlbum(String album) {
        try {
            this.tag.setField(FieldKey.ALBUM, album);
            this.file.commit();
        } catch (CannotWriteException ex) {
        } catch (KeyNotFoundException ex) {
        } catch (FieldDataInvalidException ex) {
        }
    }
}
