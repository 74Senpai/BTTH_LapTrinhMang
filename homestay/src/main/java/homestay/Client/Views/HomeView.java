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

import homestay.Client.Controllers.ClientSocketController;
import homestay.Client.Controllers.ContractController;
import homestay.Client.Controllers.DienNuocController;
import homestay.Client.Controllers.HoaDonController;
import homestay.Client.Controllers.RoomController; // Thêm controller mới
import homestay.Client.Controllers.ThongKeController;
import homestay.Client.Helper.SessionManager;
import homestay.Client.Helper.TableMapper;
import homestay.DTOs.HoaDonDTO;
import homestay.DTOs.HopDongDTO;
import homestay.DTOs.PhongDTO;
import homestay.DTOs.ThongKeDTO;

public class HomeView extends Frame {

    final Color COLOR_SIDEBAR = new Color(220, 222, 225);
    final Color COLOR_BG = Color.WHITE;
    private Components.IViewCheck currentView;

    // --- SETUP ROOM ---
    private void roomSetup(RoomView view) {
        RoomController controller = new RoomController();

        // 1. Logic tải và làm mới dữ liệu
        Runnable refresh = () -> {
            try {
                PhongDTO.ListPhong list = controller.getRooms();
                Object[][] data = list.getRooms().stream().map(r -> new Object[]{
                    r.maPhong(),
                    r.tenPhong(),
                    r.tenTrangThai(),
                    r.giaThueNgay(),
                    r.giaThueThang(),
                    r.soDienHienTai(),
                    r.soNuocHienTai()
                }).toArray(Object[][]::new);
                view.setRoomData(data);
            } catch (Exception e) {
                Components.showError(view, "Lỗi tải dữ liệu: " + e.getMessage());
            }
        };

        view.setOnRefresh(refresh);
        refresh.run();

        // 2. Logic thêm phòng mới
        view.setOnAddRoom(rowData -> {
            try {
                // Gọi hàm thêm (giờ là kiểu void, ném lỗi nếu thất bại)
                controller.handleAddRoom(rowData);

                Components.showInfo(view, "Thêm phòng thành công!");
                refresh.run(); // Refresh để lấy ID mới cấp từ Server và cập nhật lại bảng
            } catch (Exception e) {
                // e.getMessage() sẽ chứa lỗi như "Tên phòng không được để trống" hoặc lỗi từ Server
                Components.showError(view, e.getMessage());
            }
        });

        // 3. Logic cập nhật phòng
        view.setOnUpdateRoom((id, rowData) -> {
            try {
                controller.handleUpdateRoom(id, rowData);

                Components.showInfo(view, "Cập nhật phòng thành công!");
                refresh.run();
            } catch (Exception e) {
                Components.showError(view, e.getMessage());
                refresh.run(); // Refresh lại để đưa dữ liệu bảng về trạng thái đúng trong DB
            }
        });

        // 4. Logic xóa phòng
        view.setOnDeleteRoom(id -> {
            // Thêm xác nhận trước khi xóa để đảm bảo an toàn
            try {
                controller.handleDeleteRoom(id);

                Components.showInfo(view, "Xóa phòng thành công!");
                refresh.run();
            } catch (Exception e) {
                // Hiển thị lỗi cụ thể (VD: "Không thể xóa phòng đang có khách thuê")
                Components.showError(view, e.getMessage());
            }
        });
    }

    // --- SETUP DASHBOARD ---
    private void dashboardSetup(DashboardView view) {
        ThongKeController controller = new ThongKeController();
        Runnable refresh = () -> {
            try {
                ThongKeDTO.BaoCaoTongHop data = controller.getBaoCaoTongHop();
                view.updateData(data);
            } catch (Exception e) {
                Components.showError(view, "Lỗi tải thống kê: " + e.getMessage());
            }
        };
        view.setOnRefresh(refresh);
        refresh.run();
    }

    // --- SETUP CONTRACT ---
    private void contractSetup(ContractView view) {
        ContractController controller = new ContractController();
        RoomController roomCtrl = new RoomController();

        // 1. Hàm Refresh dữ liệu
        Runnable refresh = () -> {
            try {
                var emptyRooms = roomCtrl.getEmptyRooms().getRooms();
                view.setRoomList(TableMapper.mapRoomsToComboList(emptyRooms));

                HopDongDTO.ListHopDong list = controller.getContracts();
                view.setContractData(TableMapper.mapContractListToTableData(list.getContracts()));
            } catch (Exception e) {
                Components.showError(view, "Lỗi tải dữ liệu: " + e.getMessage());
            }
        };

        view.setOnRefresh(refresh);
        refresh.run();

        // 2. Xử lý THÊM hợp đồng
        view.setOnAddContract(rowData -> {
            try {
                controller.handleAddContract(rowData);
                Components.showInfo(view, "Thêm hợp đồng thành công!");
                refresh.run();
            } catch (Exception e) {
                // Hiển thị thông báo lỗi cụ thể (VD: "Tên không được để trống", "Phòng đã có người thuê")
                Components.showError(view, e.getMessage());
            }
        });

        // 3. Xử lý CẬP NHẬT hợp đồng
        view.setOnUpdateContract((id, rowData) -> {
            try {
                controller.handleUpdateContract(id, rowData);
                Components.showInfo(view, "Cập nhật hợp đồng thành công!");
                refresh.run();
            } catch (Exception e) {
                Components.showError(view, e.getMessage());
            }
        });

        // 4. Xử lý XÓA hợp đồng
        view.setOnDeleteContract(id -> {
            try {
                controller.handleDeleteContract(id);
                Components.showInfo(view, "Xóa hợp đồng thành công!");
                refresh.run();
            } catch (Exception e) {
                Components.showError(view, e.getMessage());
            }
        });
    }

    // --- SETUP UTILITY (ĐIỆN NƯỚC) ---
    private void utilityBillingSetup(UtilityBillingView view) {
        DienNuocController controller = new DienNuocController();

        // Hàm refresh dữ liệu
        Runnable refresh = () -> {
            try {
                view.setData(controller.getAllDienNuoc().getRecords());
            } catch (Exception e) {
                Components.showError(view, "Lỗi tải dữ liệu: " + e.getMessage());
            }
        };

        view.setOnRefresh(refresh);
        refresh.run();

        // Xử lý sự kiện THÊM
        view.setOnAdd(rowData -> {
            try {
                // Gọi hàm và chờ thực thi, nếu có lỗi nó sẽ nhảy thẳng xuống catch
                controller.handleAddDienNuoc(rowData);

                Components.showInfo(view, "Thêm thành công!");
                refresh.run(); // Làm mới bảng sau khi thêm thành công
            } catch (Exception e) {
                // Hiển thị thông báo lỗi chi tiết từ Controller ném ra
                Components.showError(view, e.getMessage());
                refresh.run();
            }
        });

        // Xử lý sự kiện CẬP NHẬT
        view.setOnUpdate((id, rowData) -> {
            try {
                controller.handleUpdateDienNuoc(id, rowData);

                Components.showInfo(view, "Cập nhật thành công!");
                refresh.run(); // Làm mới bảng sau khi cập nhật thành công
            } catch (Exception e) {
                // Hiển thị thông báo lỗi chi tiết (VD: "Chỉ số điện mới không được để trống")
                Components.showError(view, e.getMessage());
                refresh.run();
            }
        });
    }

    // --- SETUP HOA DON ---
    private void hoaDonSetup(HoaDonView view) {
        HoaDonController controller = new HoaDonController();

        // 1. Logic tải và làm mới dữ liệu
        Runnable refresh = () -> {
            try {
                // Lấy danh sách hóa đơn từ server
                HoaDonDTO.ListHoaDon list = controller.getHoaDons();
                // Đổ dữ liệu vào bảng trong View
                view.setHoaDonData(list.dsHoaDon());
            } catch (Exception e) {
                Components.showError(view, "Lỗi tải danh sách hóa đơn: " + e.getMessage());
            }
        };

        // Gán sự kiện cho nút Tải lại (Refresh)
        view.setOnRefresh(refresh);
        // Chạy lần đầu khi khởi động ứng dụng
        refresh.run();

        // 2. Logic thêm hóa đơn mới
        view.setOnAdd(rowData -> {
            try {
                // Controller sẽ validate và gửi request, nếu lỗi sẽ ném Exception
                controller.handleAddHoaDon(rowData);

                Components.showInfo(view, "Tạo hóa đơn thành công!");
                refresh.run();
            } catch (Exception e) {
                // Hiển thị lỗi chi tiết (VD: "Hợp đồng này đã bị hủy, không thể tạo hóa đơn")
                Components.showError(view, e.getMessage());
                refresh.run(); // Trả lại trạng thái bảng cũ
            }
        });

        // 3. Logic cập nhật hóa đơn (Sửa phụ phí hoặc trạng thái)
        view.setOnUpdate((id, rowData) -> {
            try {
                controller.handleUpdateHoaDon(id, rowData);

                Components.showInfo(view, "Cập nhật hóa đơn thành công!");
                refresh.run();
            } catch (Exception e) {
                // Hiển thị lỗi chi tiết (VD: "Cập nhật thất bại! Hóa đơn đã được thanh toán")
                Components.showError(view, e.getMessage());
                refresh.run();
            }
        });

        // 4. Logic xóa hóa đơn
        view.setOnDelete(id -> {
            // Thêm xác nhận trước khi xóa
            try {
                controller.handleDeleteHoaDon(id);

                Components.showInfo(view, "Xóa hóa đơn thành công!");
                refresh.run();
            } catch (Exception e) {
                // Hiển thị lỗi chi tiết (VD: "Không thể xóa hóa đơn đã thanh toán")
                Components.showError(view, e.getMessage());
            }
        });
    }

    public HomeView() {
        // 1. Cấu hình Frame
        setTitle("Homestay Management System");
        setSize(1200, 750);
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);

        // 2. Khởi tạo Views & CardLayout
        DashboardView dashboard = new DashboardView();
        dashboardSetup(dashboard);

        RoomView room = new RoomView();
        roomSetup(room);

        ContractView contract = new ContractView();
        contractSetup(contract);

        UtilityBillingView utility = new UtilityBillingView();
        utilityBillingSetup(utility);

        HoaDonView invoice = new HoaDonView(); // View Hóa đơn mới
        hoaDonSetup(invoice);

        CardLayout card = new CardLayout();
        Panel pnlMain = new Panel(card);
        pnlMain.add(dashboard.pnlMain, "Dashboard");
        pnlMain.add(room, "Room");
        pnlMain.add(contract, "Contract");
        pnlMain.add(utility, "Utility");
        pnlMain.add(invoice, "Invoice"); // Thêm vào CardLayout

        // 3. Sidebar & Buttons
        Panel pnlSidebar = new Panel(new BorderLayout());
        pnlSidebar.setBackground(COLOR_SIDEBAR);
        pnlSidebar.setPreferredSize(new Dimension(220, 700));

        Label lblLogo = new Label("HOMESTAY", Label.CENTER);
        lblLogo.setFont(new Font("Arial", Font.BOLD, 24));

        Panel pnlMenu = new Panel(new GridLayout(10, 1, 0, 5));
        Button btnHome = Components.createMenuItem("Tổng quan");
        Button btnRoom = Components.createMenuItem("Quản lý Phòng");
        Button btnContract = Components.createMenuItem("Hợp đồng / Khách");
        Button btnUtility = Components.createMenuItem("Điện & Nước");
        Button btnInvoice = Components.createMenuItem("Hóa đơn & Doanh thu"); // Nút mới
        Button btnLogout = Components.createMenuItem("Đăng xuất");

        // 4. Logic chuyển trang
        btnHome.addActionListener(e -> switchTab(card, pnlMain, dashboard, "Dashboard", btnHome, btnRoom, btnContract, btnUtility, btnInvoice));
        btnRoom.addActionListener(e -> switchTab(card, pnlMain, room, "Room", btnRoom, btnHome, btnContract, btnUtility, btnInvoice));
        btnContract.addActionListener(e -> switchTab(card, pnlMain, contract, "Contract", btnContract, btnHome, btnRoom, btnUtility, btnInvoice));
        btnUtility.addActionListener(e -> switchTab(card, pnlMain, utility, "Utility", btnUtility, btnHome, btnRoom, btnContract, btnInvoice));
        btnInvoice.addActionListener(e -> switchTab(card, pnlMain, invoice, "Invoice", btnInvoice, btnHome, btnRoom, btnContract, btnUtility));

        btnLogout.addActionListener(e -> {
            SessionManager.clearSession();
            ClientSocketController.kill();
            System.exit(0);
        });
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                ClientSocketController.kill();
                System.exit(0);
            }
        });

        // 5. Lắp ráp
        pnlMenu.add(lblLogo);
        pnlMenu.add(btnHome);
        pnlMenu.add(btnRoom);
        pnlMenu.add(btnContract);
        pnlMenu.add(btnUtility);
        pnlMenu.add(btnInvoice);

        pnlSidebar.add(pnlMenu, BorderLayout.NORTH);
        pnlSidebar.add(btnLogout, BorderLayout.SOUTH);

        ScrollPane scroll = new ScrollPane();
        scroll.add(pnlMain);
        add(pnlSidebar, BorderLayout.WEST);
        add(scroll, BorderLayout.CENTER);

        btnHome.setEnabled(false); // Mặc định ở Dashboard
        setLocationRelativeTo(null);
    }

    // Helper rút gọn logic chuyển tab
    private void switchTab(CardLayout card, Panel pnlMain, Components.IViewCheck targetView, String name, Button active, Button... others) {
        Components.IViewCheck result = Components.switchView(card, pnlMain, currentView, targetView, name);
        if (result == targetView) {
            currentView = result;
            Components.updateMenuState(active, others);
            validate();
        }
    }
}
