package homestay.Client.Views;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Login View
 * CHỈ HIỂN THỊ + GỌI CONTROLLER
 */
public class LoginView extends Frame {

    // ================= UI =================
    private TextField txtUsername;
    private TextField txtPassword;
    private Label lblMessage;
    private Button btnLogin;

    public LoginView() {
        super("Login - Homestay");
        initUI();
    }

    private void initUI() {
        setSize(420, 360);
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(248, 249, 250));

        // ================= TITLE =================
        Label lblTitle = new Label("HOMESTAY LOGIN", Label.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(33, 37, 41));
        add(lblTitle, BorderLayout.NORTH);

        // ================= FORM PANEL =================
        Panel pnlForm = new Panel(new GridBagLayout());
        pnlForm.setBackground(Color.WHITE);
        add(pnlForm, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 20, 8, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;

        // -------- Username --------
        gbc.gridy = 0;
        Label lblUser = new Label("Tên đăng nhập");
        lblUser.setFont(new Font("Arial", Font.BOLD, 14));
        pnlForm.add(lblUser, gbc);

        gbc.gridy = 1;
        txtUsername = new TextField();
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 16));
        txtUsername.setPreferredSize(new Dimension(0, 32));
        pnlForm.add(txtUsername, gbc);

        // -------- Password --------
        gbc.gridy = 2;
        Label lblPass = new Label("Mật khẩu");
        lblPass.setFont(new Font("Arial", Font.BOLD, 14));
        pnlForm.add(lblPass, gbc);

        gbc.gridy = 3;
        txtPassword = new TextField();
        txtPassword.setEchoChar('*');
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        txtPassword.setPreferredSize(new Dimension(0, 32));
        pnlForm.add(txtPassword, gbc);

        // -------- Message --------
        gbc.gridy = 4;
        lblMessage = new Label("", Label.CENTER);
        lblMessage.setFont(new Font("Arial", Font.PLAIN, 12));
        lblMessage.setForeground(Color.RED);
        pnlForm.add(lblMessage, gbc);

        // -------- Button --------
        gbc.gridy = 5;
        btnLogin = new Button("Đăng nhập");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBackground(new Color(33, 37, 41));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setPreferredSize(new Dimension(0, 36));
        pnlForm.add(btnLogin, gbc);

        // ================= WINDOW =================
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });

        setLocationRelativeTo(null);
    }

    // =====================================================
    // ================= PUBLIC METHODS ===================
    // =====================================================

    public String getUsername() {
        return txtUsername.getText().trim();
    }

    public String getPassword() {
        return txtPassword.getText().trim();
    }

    public void showMessage(String msg) {
        lblMessage.setText(msg);
    }

    // =====================================================
    // ================= CONTROLLER HOOK ===================
    // =====================================================

    public void addLoginListener(ActionListener l) {
        btnLogin.addActionListener(l);
    }

    // =====================================================
    // ================= TEST MAIN =========================
    // =====================================================

    public static void main(String[] args) {
        LoginView view = new LoginView();
        view.setVisible(true);
    }
}
