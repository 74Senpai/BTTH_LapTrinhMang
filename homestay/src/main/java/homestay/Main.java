package homestay;

import homestay.Client.ClientMain;
import homestay.Client.Helper.SessionManager;
import homestay.Server.ServerMain;

public class Main {
    public static void main(String[] args) {
        
        SessionManager.clearSession();

        Thread serverThread = new Thread(() -> {
            ServerMain.main(args);
        });
        serverThread.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.err.println("Có lỗi xảy ra: "+e.getMessage());
        }

        ClientMain.main(args);
    }
}
