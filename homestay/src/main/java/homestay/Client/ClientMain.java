package homestay.Client;

import javax.swing.JOptionPane;

import homestay.Client.Controllers.ClientSocketController;
import homestay.Client.Controllers.LoginController;
import homestay.Client.Helper.SessionManager;
import homestay.Client.Views.HomeView;
import homestay.Client.Views.LoginView;

public class ClientMain {

    public static void main(String[] args) {
        ClientSocketController.serverURL("localhost", 8000);
        try {
            ClientSocketController.openConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Không thể kết nối tới server!", "Thông báo",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        if (SessionManager.getSession() == null) {
            LoginView loginView = new LoginView();
            LoginController controller = new LoginController();

            loginView.addLoginListener((actionEvent) -> {
                try {
                    boolean isSuccess = controller.login(loginView.getUsername(), loginView.getPassword());

                    if (isSuccess) {
                        JOptionPane.showMessageDialog(loginView,
                                "Đăng nhập thành công!", "Thông báo",
                                JOptionPane.INFORMATION_MESSAGE);

                        loginView.dispose();
                        new HomeView().setVisible(true);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(loginView,
                            "Đăng nhập thất bại: " + e.getMessage(),
                            "Lỗi đăng nhập",
                            JOptionPane.ERROR_MESSAGE);

                    System.err.println("Login Error: " + e.getMessage());
                }
            });

            loginView.setVisible(true);
        } else {
            new HomeView().setVisible(true);
        }
    }
}
