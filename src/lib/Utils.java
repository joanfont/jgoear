package lib;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.InetAddress;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class Utils {

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) {
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static String slugify(String input) {
        return input.replaceAll(" ", "-");
    }

    public static String normalize(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\P{ASCII}");
        return pattern.matcher(normalized).replaceAll("");
    }

    public static String capitalize(String s) {
        for (int i = 0; i < s.length(); i++) {

            if (i == 0) {
                s = String.format("%s%s",
                        Character.toUpperCase(s.charAt(0)),
                        s.substring(1));
            }
            if (!Character.isLetterOrDigit(s.charAt(i))) {
                if (i + 1 < s.length()) {
                    s = String.format("%s%s%s",
                            s.subSequence(0, i + 1),
                            Character.toUpperCase(s.charAt(i + 1)),
                            s.substring(i + 2));
                }
            }

        }

        return s;

    }

    public static boolean isOSX() {
        return System.getProperty("os.name").toLowerCase().startsWith("mac os x");
    }
}
