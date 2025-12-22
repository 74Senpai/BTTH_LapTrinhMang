package homestay.Server.Services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogService {

    private static final String LOG_FILE = "server-log.txt";
    private static final SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static synchronized void writeLog(String action, String username, Date time) {

        String logLine = String.format(
                "[%s] | action=%s | user=%s",
                sdf.format(time),
                action,
                username
        );

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(LOG_FILE, true))) {

            writer.write(logLine);
            writer.newLine();

        } catch (IOException e) {
            System.err.println("❌ Không thể ghi log: " + e.getMessage());
        }
    }
}
