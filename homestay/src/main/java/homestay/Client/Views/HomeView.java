package homestay.Client.Views;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.ScrollPane;

public class HomeView extends Frame {

    public HomeView() {
        // ====================================================================
        // PHẦN 1: KHỞI TẠO (INITIALIZATION)
        // Tạo đối tượng, set màu sắc, font chữ, layout, trạng thái ban đầu
        // ====================================================================
        // 1.1. Cấu hình Frame chính
        setTitle("Homestay Dashboard");
        setSize(1100, 700);
        setLayout(new BorderLayout());

        // 1.2. Khởi tạo Dashboard và các giao diện khác ở đây
        DashboardView dashboard = new DashboardView();
        dashboard.showDashboard();
        
        CardLayout card = new CardLayout();
        Panel pnlMain = new Panel(card);
        pnlMain.add(dashboard.pnlMain, "Dashboard");
        
        ScrollPane scrollPane = new ScrollPane(); // Container cuộn cho nội dung chính

        // 1.3. Khởi tạo Sidebar (Cột trái)
        Panel pnlSidebar = new Panel();
        pnlSidebar.setLayout(new BorderLayout());
        pnlSidebar.setPreferredSize(new Dimension(200, 700));

        // 1.4. Khởi tạo Logo & Menu Panel
        Label lblLogo = new Label("Home", Label.CENTER);
        lblLogo.setFont(new Font("Arial", Font.BOLD, 24));
        
        Panel pnlMenu = new Panel(new GridLayout(10, 1, 0, 10));
        Panel pnlBottomMenu = new Panel(new GridLayout(2, 1));

        // 1.5. Khởi tạo các Nút bấm (Buttons)
        // Lưu ý: Tạo biến cho tất cả các nút để dễ xử lý logic
        Button btnHome = Components.createMenuItem("Home");

        // 1.6. Thiết lập trạng thái ban đầu
        btnHome.setEnabled(false); // Mặc định đang ở Home nên disable nút Home

        // ====================================================================
        // PHẦN 2: XỬ LÝ LOGIC (LOGIC & EVENTS)
        // Gán sự kiện click, xử lý ẩn hiện, luồng dữ liệu
        // ====================================================================

        // Logic các nút ở đây
        btnHome.addActionListener(e -> {
            //Controller ở đây
            card.show(pnlMain, "Dashboard");
            btnHome.setEnabled(false);
            //khi ẩn/hiện component cần validate lại để layout cập nhật
            validate();
        });


        // ====================================================================
        // PHẦN 3: THÊM VÀO VIEW (ADD TO VIEW)
        // Lắp ráp các thành phần vào nhau để hiển thị lên màn hình
        // ====================================================================

        // 3.1. Lắp ráp Menu (Sidebar)
        pnlMenu.add(lblLogo);
        pnlMenu.add(btnHome);
        
        pnlSidebar.add(pnlMenu, BorderLayout.NORTH);
        pnlSidebar.add(pnlBottomMenu, BorderLayout.SOUTH);

        // 3.2. Lắp ráp Main Content
        scrollPane.add(pnlMain);

        // 3.3. Lắp ráp vào Frame chính
        add(pnlSidebar, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        // 3.4. Hiển thị Frame (Luôn để cuối cùng)
        setLocationRelativeTo(null); // Căn giữa màn hình
    }

    public static void main(String[] args) {
        HomeView view = new HomeView();
        view.setVisible(true);
    }
}

