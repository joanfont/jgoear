package lib;

import java.io.*;
import java.net.*;
import java.util.*;
import static lib.Goear.GOEAR_PLAY;
import org.jsoup.Jsoup;
import tag.Tags;

public class Download extends Observable implements Runnable {

    private static final int MAX_BUFFER_SIZE = 1024;
    public static final String STATUSES[] = {"Downloading",
        "Paused", "Complete", "Cancelled", "Error", "Pending"};
    public static final int DOWNLOADING = 0;
    public static final int PAUSED = 1;
    public static final int COMPLETE = 2;
    public static final int CANCELLED = 3;
    public static final int ERROR = 4;
    public static final int PENDING = 5;
    private final Song s;
    private final URL url;
    private int size;
    private int downloaded;
    private int status;

    public Download(Song song, URL url) {
        this.s = song;
        this.url = url;
        size = -1;
        downloaded = 0;
        status = PENDING;
    }

    public String getUrl() {
        return url.toString();
    }

    public Song getSong() {
        return this.s;
    }

    public int getSize() {
        return size;
    }

    public float getProgress() {
        return ((float) downloaded / size) * 100;
    }

    public int getStatus() {
        return status;
    }

    public void cancel() {
        status = CANCELLED;
        stateChanged();
    }

    private void error() {
        status = ERROR;
        stateChanged();
    }

    public void download() {
        status = DOWNLOADING;
        Thread thread = new Thread(this);
        thread.start();
    }


    @Override
    public void run() {
        RandomAccessFile file = null;
        InputStream stream = null;
        try {
            Jsoup.connect(GOEAR_PLAY + "/" + s.getSongID()).post();

            HttpURLConnection connection
                    = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            connection.setRequestProperty("Range",
                    "bytes=" + downloaded + "-");

            connection.connect();

            if (connection.getResponseCode() / 100 != 2) {
                error();
            }

            int contentLength = connection.getContentLength();
            if (contentLength < 1) {
                error();
            }

            if (size == -1) {
                size = contentLength;
                stateChanged();
            }

            SettingsFile sf = new SettingsFile();
            String path = sf.getSavePath();
            String fn = s.toString() + "." + Song.FILE_EXT;

            file = new RandomAccessFile(path + File.separator + fn, "rw");
            file.seek(downloaded);

            stream = connection.getInputStream();
            while (status == DOWNLOADING) {

                byte buffer[];
                if (size - downloaded > MAX_BUFFER_SIZE) {
                    buffer = new byte[MAX_BUFFER_SIZE];
                } else {
                    buffer = new byte[size - downloaded];
                }

                int read = stream.read(buffer);
                if (read == -1) {
                    break;
                }

                file.write(buffer, 0, read);
                downloaded += read;
                stateChanged();
            }

            if (status == DOWNLOADING) {
                status = COMPLETE;
                stateChanged();
            }
        } catch (Exception e) {
            error();
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (Exception e) {
                }
            }

            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                }
            }
            if (status == COMPLETE) {
                Tags t = new Tags(this.s);
                t.setAll();
            }
        }
    }

    private void stateChanged() {
        setChanged();
        notifyObservers();
    }
}
