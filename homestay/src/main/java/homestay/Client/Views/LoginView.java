package homestay.Client.Views;

import java.awt.*;
import java.awt.event.*;

public class LoginView extends Frame {

    private Label lblMessage;

    // private LoginController controller;
    private TextField txtUsername;
    private TextField txtPassword;
    
    public LoginView() {
        super("Login - Homestay");

        //Cấu hình Frame chính đầy fullwindowscreen
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        setSize(d.width, d.height);
        setExtendedState(Frame.MAXIMIZED_BOTH);

        setLayout(new BorderLayout());

        //Tạo Background Panel (Lớp đáy chứa ảnh)
        BackgroundPanel bgPanel = new BackgroundPanel(
                "D:\\box\\javaM\\BTTH_LapTrinhMang\\homestay\\src\\main\\java\\homestay\\Client\\Views\\homestay2.jpg");

        bgPanel.setLayout(new GridBagLayout()); // Căn giữa login box
        add(bgPanel, BorderLayout.CENTER);

        // ----------- LOGIN BOX -----------
        Panel box = new Panel();
        box.setBackground(Color.WHITE);
        box.setPreferredSize(new Dimension(560, 520));
        box.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Cho bảng full box
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 50, 2, 50);
        
        // ------- TITLE -------
        Label lblTitle = new Label("Log in", Label.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 34));
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        box.add(lblTitle, gbc);

        // ------- SUBTITLE -------
        Label lblSub = new Label("Welcome back! Please sign in.", Label.CENTER);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSub.setForeground(Color.GRAY);
        gbc.gridy = 1;
        box.add(lblSub, gbc);

        // ------- USERNAME -------
        Label lblUsername = new Label("Username :");
        lblUsername.setFont(new Font("SansSerif", Font.BOLD, 15));
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        box.add(lblUsername, gbc);

        txtUsername = new TextField();
        txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 20));
        txtUsername.setPreferredSize(new Dimension(0, 35));
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 50, 10, 50);
        box.add(txtUsername, gbc);

        // ------- PASSWORD -------
        Label lblPass = new Label("Password :");
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 15));
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 50, 2, 50);
        gbc.fill = GridBagConstraints.NONE;
        box.add(lblPass, gbc);

        txtPassword = new TextField();
        txtPassword.setEchoChar('*');
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 20));
        txtPassword.setPreferredSize(new Dimension(0, 35));
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 50, 10, 50);
        box.add(txtPassword, gbc);
        
        //------- MESSAGE LABEL -------
        lblMessage = new Label();
        lblMessage.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblMessage.setForeground(Color.RED);
        gbc.gridy = 6;
        box.add(lblMessage, gbc);

        // ------- LOGIN BUTTON -------
        Button btnLogin = new Button("Log in");
        btnLogin.setBackground(Color.BLACK);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnLogin.setPreferredSize(new Dimension(0, 40));
        gbc.gridy = 7;
        box.add(btnLogin, gbc);

        btnLogin.addActionListener(e ->{
            String Username = txtUsername.getText().toString();
            String Password = txtPassword.getText().toString();
            if(Username.isEmpty() || Password.isEmpty()){
                showMessage("Mật khẩu hoặc tên đăng nhập không được để trống!");
                return;
            } else {
                showMessage("Đăng nhập thành công!");
            }
            //Controller xử lý đăng nhập
            
            // controller = new LoginController();
            // controller.handleLogin(username, password); 
        });
        
        // Đưa box vào giữa background
        bgPanel.add(box);
        


        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
        
        
        setLocationRelativeTo(null);
    }
    public void showMessage(String msg) {
        
        lblMessage.setText(msg);
    }

    public static void main(String[] args) {
        
        LoginView loginView = new LoginView();
        loginView.setVisible(true);
    }

}