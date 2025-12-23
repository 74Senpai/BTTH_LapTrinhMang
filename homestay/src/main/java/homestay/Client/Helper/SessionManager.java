package homestay.Client.Helper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SessionManager {

    private static final String SESSION_FILE = "session.txt";
    private static String session = null;

    private SessionManager() {
    }

    static {
        loadSessionFromFile();
    }

    private static void loadSessionFromFile() {
        try {
            if (!Files.exists(Paths.get(SESSION_FILE))) {
                Files.createFile(Paths.get(SESSION_FILE));
            }

            session = new String(Files.readAllBytes(Paths.get(SESSION_FILE))).trim();
            if (session.isEmpty()) {
                session = null;
            }

        } catch (IOException e) {
            System.err.println("Không thể đọc hoặc tạo session file: " + e.getMessage());
            session = null;
        }
    }

    public static String getSession() {
        return session;
    }

    public static void setSession(String token) {
        session = token;
        saveSessionToFile();
    }

    private static void saveSessionToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SESSION_FILE))) {
            writer.write(session != null ? session : "");
        } catch (IOException e) {
            System.err.println("Không thể lưu session ra file: " + e.getMessage());
        }
    }

    public static void clearSession() {
        session = null;
        saveSessionToFile();
    }
}
