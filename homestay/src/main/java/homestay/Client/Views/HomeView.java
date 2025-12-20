package homestay.Client.Views;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import homestay.Client.Controllers.RoomController;
import homestay.Client.Helper.SessionManager;
import homestay.Client.Helper.TableMapper;
import homestay.Client.Views.Room.RoomView;


public class HomeView extends Frame {

    // Màu sắc chủ đạo
    final Color COLOR_SIDEBAR = new Color(220, 222, 225); 
    final Color COLOR_BG = Color.WHITE;
    private Components.IViewCheck currentView; 

    private void roomSetup(RoomView view){
        RoomController ctrl = new RoomController();
        view.addRefreshListener((actionEvent) -> {
            try {
                view.setRoomData(TableMapper.mapRoomListToTableData(ctrl.getRooms()));
            } catch (Exception e) {
                Components.showError(view, e.getMessage());
            }
        });

        view.onOpen(()->{
            try {
                view.setRoomData(TableMapper.mapRoomListToTableData(ctrl.getRooms()));
            } catch (Exception e) {
                Components.showError(view, e.getMessage());
            }
        });
    }

    public HomeView() {
        // ====================================================================
        // PHẦN 1: KHỞI TẠO (INITIALIZATION)
        // Tạo đối tượng, set màu sắc, font chữ, layout, trạng thái ban đầu
        // ====================================================================
        // 1.1. Cấu hình Frame chính
        setTitle("Homestay Dashboard");
        setSize(1100, 700);
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);

        // 1.2. Khởi tạo Dashboard & Main Content và room
        DashboardView dashboard = new DashboardView();
        dashboard.showDashboard();
        
        RoomView room = new RoomView();
        this.roomSetup(room);
        CustomerView customer = new CustomerView();

        StatisticsReportView statisticsReport = new StatisticsReportView();
        
        UtilityBillingMonthlyReportView utilityBilling = new UtilityBillingMonthlyReportView();

        CardLayout card = new CardLayout();
        Panel pnlMain = new Panel(card);

        pnlMain.add(dashboard.pnlMain, "Dashboard");
        pnlMain.add(room, "Room");
        pnlMain.add(customer, "Customer");
        pnlMain.add(statisticsReport, "StatisticsReport");
        pnlMain.add(utilityBilling, "UtilityBillingMonthlyReport");
        
        ScrollPane scrollPane = new ScrollPane(); // Container cuộn cho nội dung chính

        // 1.3. Khởi tạo Sidebar (Cột trái)
        Panel pnlSidebar = new Panel();
        pnlSidebar.setLayout(new BorderLayout());
        pnlSidebar.setBackground(COLOR_SIDEBAR);
        pnlSidebar.setPreferredSize(new Dimension(200, 700));

        // 1.4. Khởi tạo Logo & Menu Panel
        Label lblLogo = new Label("Home", Label.CENTER);
        lblLogo.setFont(new Font("Arial", Font.BOLD, 24));
        
        Panel pnlMenu = new Panel(new GridLayout(10, 1, 0, 10));
        Panel pnlBottomMenu = new Panel(new GridLayout(2, 1));

        // 1.5. Khởi tạo các Nút bấm (Buttons)
        // Lưu ý: Tạo biến cho tất cả các nút để dễ xử lý logic
        Button btnHome = Components.createMenuItem("Home");
        Button btnRoom = Components.createMenuItem("Quản lý Phòng");
        Button btnCustomer = Components.createMenuItem("Quản lý Khách Hàng");
        Button btnLogout = Components.createMenuItem("Log Out");
        Button btnStatistics = Components.createMenuItem("Báo cáo Thống kê");
        Button btnUtilityBilling = Components.createMenuItem("Báo cáo Điện Nước");

        // 1.6. Thiết lập trạng thái ban đầu
        btnHome.setEnabled(false); // Mặc định đang ở Home nên disable nút Home

        // ====================================================================
        // PHẦN 2: XỬ LÝ LOGIC (LOGIC & EVENTS)
        // Gán sự kiện click, xử lý ẩn hiện, luồng dữ liệu
        // ====================================================================

        // 2.1. Logic nút Home
        btnHome.addActionListener(e -> {
            Components.IViewCheck resultView = Components.switchView(card, pnlMain, currentView, dashboard, "Dashboard");
            if (resultView == dashboard) {
                currentView = resultView;
                Components.updateMenuState(
                    btnHome, 
                    btnRoom, btnCustomer, btnStatistics, btnUtilityBilling
                );
                // validate để cập nhật lại giao diện
                validate();
            }
        });

        // 2.2. Logic nút Menu
        btnRoom.addActionListener(e -> {
            Components.IViewCheck resultView  = Components.switchView(card, pnlMain, currentView, room, "Room");
            if(resultView == room){
                currentView = resultView;
                Components.updateMenuState(
                    btnRoom, 
                    btnHome, btnCustomer, btnStatistics, btnUtilityBilling
                );
                room.onOpen();
                validate();
            }
        });
        
        // 2.3. Logic nút Customer
        btnCustomer.addActionListener(e -> {
            Components.IViewCheck resultView = Components.switchView(card, pnlMain, currentView, customer, "Customer");
            if(resultView == customer){
                currentView = resultView;
                Components.updateMenuState(
                    btnCustomer, 
                    btnHome, btnRoom, btnStatistics, btnUtilityBilling
                );
                validate();
            }
        });


        // 2.4. Logic nút Logout
        btnLogout.addActionListener(e -> {
            SessionManager.clearSession();
            System.exit(0);
        });

        // 2.5. Logic đóng cửa sổ
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });

        // 2.6. Logic nút Statistics Report
        btnStatistics.addActionListener(e -> {
            Components.IViewCheck resultView = Components.switchView(card, pnlMain, currentView, statisticsReport, "StatisticsReport");
            if (resultView == statisticsReport) {
                currentView = resultView;
                Components.updateMenuState(
                    btnStatistics, 
                    btnHome, btnRoom, btnCustomer, btnUtilityBilling
                );
                validate();
            }
        });

        // 2.7. Logic nút Utility Billing Report
        btnUtilityBilling.addActionListener(e -> {
            Components.IViewCheck resultView = Components.switchView(card, pnlMain, currentView, utilityBilling, "UtilityBillingMonthlyReport");
            if (resultView == utilityBilling) {
                currentView = resultView;
                Components.updateMenuState(
                    btnUtilityBilling, 
                    btnHome, btnRoom, btnCustomer, btnStatistics
                );
                validate();
            }
        });

        // ====================================================================
        // PHẦN 3: THÊM VÀO VIEW (ADD TO VIEW)
        // Lắp ráp các thành phần vào nhau để hiển thị lên màn hình
        // ====================================================================

        // 3.1. Lắp ráp Menu (Sidebar)
        pnlMenu.add(lblLogo);
        pnlMenu.add(btnHome);
        pnlMenu.add(btnRoom);
        pnlMenu.add(btnCustomer);
        pnlMenu.add(btnStatistics);
        pnlMenu.add(btnUtilityBilling);
        
        pnlBottomMenu.add(btnLogout);

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
}
