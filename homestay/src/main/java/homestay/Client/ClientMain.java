package homestay.Client;

import homestay.Client.Controllers.LoginController;
import homestay.Client.Helper.SessionManager;
import homestay.Client.Views.HomeView;
import homestay.Client.Views.LoginView;

public class ClientMain {

    public static void main(String[] args) {
        if (SessionManager.getSession() == null) {
            LoginView loginView = new LoginView();
            LoginController controller = new LoginController();
            loginView.addLoginListener((actionEvent) -> {
                boolean isSuccess = controller.login(loginView.getUsername(), loginView.getPassword());
                if (isSuccess) {
                    loginView.dispose();
                    new HomeView().setVisible(true);
                }
            });
            loginView.setVisible(true);
        }else{
            new HomeView().setVisible(true);
        }
    }
}
