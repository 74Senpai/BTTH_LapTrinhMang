package homestay.Server;

import homestay.Server.Controllers.ServerController;

public class ServerMain{
    public static void main(String[] args) {
        ServerController svController = new ServerController(8000);
        svController.startServer();
        svController.startListening();
        svController.stopServer();
    }
}
