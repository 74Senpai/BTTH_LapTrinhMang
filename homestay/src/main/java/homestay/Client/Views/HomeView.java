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

import javax.swing.JOptionPane;

import homestay.Client.Controllers.ClientSocketController;
import homestay.Client.Controllers.ContractController;
import homestay.Client.Controllers.RoomController;
import homestay.Client.Helper.SessionManager;
import homestay.Client.Helper.TableMapper;
import homestay.DTOs.HopDongDTO;
import homestay.DTOs.PhongDTO;

public class HomeView extends Frame {

    // Màu sắc chủ đạo
    final Color COLOR_SIDEBAR = new Color(220, 222, 225);
    final Color COLOR_BG = Color.WHITE;
    private Components.IViewCheck currentView;

    private void roomSetup(RoomView view) {

        RoomController controller = new RoomController();
        Runnable refresh = (() -> {
            try {
                PhongDTO.ListPhong list = controller.getRooms();
                Object[][] data = list.getRooms().stream().map(r -> new Object[]{
                    r.maPhong(), r.tenPhong(), r.tenTrangThai(),
                    r.giaThueNgay(), r.giaThueThang(), r.soDienHienTai(), r.soNuocHienTai()
                }).toArray(Object[][]::new);

                view.setRoomData(data);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Lỗi tải dữ liệu: " + e.getMessage());
            }
        });
        view.setOnRefresh(refresh);
        refresh.run();

        view.setOnAddRoom(rowData -> {
            PhongDTO.View result = controller.handleAddRoom(rowData);
            if (result.maPhong() != -1) {
                view.updateRoomIdAtSelectedRow(result.maPhong());
            } else {
                JOptionPane.showMessageDialog(view, "Thêm thất bại!");
            }
        });

        view.setOnUpdateRoom((id, rowData) -> {
            boolean success = controller.handleUpdateRoom(id, rowData);
            if (!success) {
                JOptionPane.showMessageDialog(view, "Cập nhật thất bại!");
            }
        });

        view.setOnDeleteRoom(id -> {
            try {
                boolean success = controller.handleDeleteRoom(id);
            } catch (Exception e) {
                Components.showError(view, e.getMessage());
            }
        });

    }

    private void contractSetup(ContractView view) {
        ContractController controller = new ContractController();
        RoomController roomCtrl = new RoomController();

        Runnable loadMetaData = () -> {
            try {
                // Lấy danh sách phòng trống từ server
                var emptyRooms = roomCtrl.getEmptyRooms().getRooms();
                // Map sang String[] dùng Helper
                String[] roomDisplay = TableMapper.mapRoomsToComboList(emptyRooms);
                // Bơm vào View
                view.setRoomList(roomDisplay);
            } catch (Exception e) {
                System.err.println("Lỗi load metadata phòng: " + e.getMessage());
            }
        };
        // 1. Logic làm mới dữ liệu
        Runnable refresh = () -> {
            try {
                loadMetaData.run();
                HopDongDTO.ListHopDong list = controller.getContracts();

                // Sử dụng TableMapper để chuyển đổi List DTO sang Object[][] cho JTable
                Object[][] data = homestay.Client.Helper.TableMapper.mapContractListToTableData(list.getContracts());

                view.setContractData(data);
            } catch (Exception e) {
                Components.showError(view, "Lỗi tải dữ liệu hợp đồng: " + e.getMessage());
            }
        };

        // Gán sự kiện Refresh cho nút Tải lại
        view.setOnRefresh(refresh);
        refresh.run(); // Chạy ngay lần đầu để load dữ liệu

        view.setOnAddContract(data -> { if(controller.handleAddContract(data)) refresh.run(); });
        // 2. Logic thêm hợp đồng
        view.setOnAddContract(rowData -> {
            boolean success = controller.handleAddContract(rowData);
            if (success) {
                // Vì tạo hợp đồng thường sinh mã ID tự động từ DB, 
                // nên refresh lại toàn bộ để lấy mã mới nhất và thông tin đồng bộ
                refresh.run();
            } else {
                Components.showError(view, "Thêm hợp đồng thất bại! Vui lòng kiểm tra lại thông tin.");
            }
        });

        // 3. Logic cập nhật hợp đồng
        view.setOnUpdateContract((id, rowData) -> {
            boolean success = controller.handleUpdateContract(id, rowData);
            if (!success) {
                Components.showError(view, "Cập nhật hợp đồng thất bại!");
            }
        });

        view.setOnDeleteContract((id)-> {
            try {
                controller.handleDeleteContract(id);
            } catch (Exception e) {
                Components.showError(view, "Xóa hợp đồng thất bại!");
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

        RoomView room = new RoomView();
        this.roomSetup(room);
        ContractView customer = new ContractView();
        this.contractSetup(customer);

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
            Components.IViewCheck resultView = Components.switchView(card, pnlMain, currentView, room, "Room");
            if (resultView == room) {
                currentView = resultView;
                Components.updateMenuState(
                        btnRoom,
                        btnHome, btnCustomer, btnStatistics, btnUtilityBilling
                );
                validate();
            }
        });

        // 2.3. Logic nút Customer
        btnCustomer.addActionListener(e -> {
            Components.IViewCheck resultView = Components.switchView(card, pnlMain, currentView, customer, "Customer");
            if (resultView == customer) {
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
            ClientSocketController.kill();
            System.exit(0);
        });

        // 2.5. Logic đóng cửa sổ
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                ClientSocketController.kill();
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
