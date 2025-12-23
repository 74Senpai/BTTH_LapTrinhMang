package homestay.Client.Views;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.text.NumberFormat;
import java.util.Locale;

import homestay.DTOs.ThongKeDTO;

public class DashboardView extends Panel implements Components.IViewCheck {

    final Color COLOR_CARD_BLUE = new Color(100, 120, 140);
    final Color COLOR_SUCCESS = new Color(40, 167, 69);
    final Color COLOR_WARNING = new Color(255, 193, 7);
    final Color COLOR_DANGER = new Color(220, 53, 69);

    Panel pnlMain = new Panel();
    Panel pnlOverviewCards = new Panel(new GridLayout(1, 4, 15, 0));
    Label lblWelcome = new Label("Chào mừng bạn quay trở lại!");

    // Thêm nút Tải lại
    Button btnRefresh = new Button("Tải lại dữ liệu");

    private Runnable onRefresh;

    public DashboardView() {
        setLayout(new java.awt.BorderLayout());
        add(pnlMain, java.awt.BorderLayout.CENTER);
        setupLayout();
        setupEvents();
    }

    public void setOnRefresh(Runnable onRefresh) {
        this.onRefresh = onRefresh;
    }

    private void setupEvents() {
        btnRefresh.addActionListener(e -> {
            if (onRefresh != null) {
                onRefresh.run();
            }
        });
    }

    private void setupLayout() {
        pnlMain.setLayout(new GridBagLayout());
        pnlMain.setBackground(new Color(248, 249, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // --- Header Panel (Chứa Welcome và Nút Tải lại) ---
        Panel pnlHeader = new Panel(new BorderLayout());

        lblWelcome.setFont(new Font("Arial", Font.BOLD, 22));
        pnlHeader.add(lblWelcome, BorderLayout.WEST);

        // Trang trí nút tải lại
        btnRefresh.setBackground(Color.WHITE);
        btnRefresh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        Panel pnlActions = new Panel(new FlowLayout(FlowLayout.RIGHT));
        pnlActions.add(btnRefresh);
        pnlHeader.add(pnlActions, BorderLayout.EAST);

        gbc.gridy = 0;
        pnlMain.add(pnlHeader, gbc);

        // --- Overview Cards Container ---
        gbc.gridy = 1;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        pnlMain.add(pnlOverviewCards, gbc);

        // Filler để đẩy nội dung lên đầu
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        pnlMain.add(new Panel(), gbc);

        renderCards(0, 0, 0, 0);
    }

    public void updateData(ThongKeDTO.BaoCaoTongHop data) {
        int totalRooms = 0;
        int occupiedRooms = 0;
        int emptyRooms = 0;

        if (data != null && data.dsTrangThai != null) {
            for (ThongKeDTO.TrangThaiPhong tt : data.dsTrangThai) {
                totalRooms += tt.soLuong;
                if (tt.tenTrangThai.equalsIgnoreCase("Đang sử dụng") || tt.tenTrangThai.equalsIgnoreCase("Đang ở")) {
                    occupiedRooms = tt.soLuong;
                } else if (tt.tenTrangThai.equalsIgnoreCase("Trống")) {
                    emptyRooms = tt.soLuong;
                }
            }
            renderCards(totalRooms, occupiedRooms, emptyRooms, data.doanhThuThangNay);
        }
    }

    private void renderCards(int total, int occupied, int empty, double revenue) {
        pnlOverviewCards.removeAll();
        NumberFormat vnFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String revenueStr = vnFormat.format(revenue);

        pnlOverviewCards.add(Components.createCard("Tổng số phòng", total + " Phòng", COLOR_CARD_BLUE));
        pnlOverviewCards.add(Components.createCard("Đang ở", occupied + "", COLOR_SUCCESS));
        pnlOverviewCards.add(Components.createCard("Phòng trống", empty + "", COLOR_WARNING));
        pnlOverviewCards.add(Components.createCard("Doanh thu tháng", revenueStr, COLOR_DANGER));

        pnlOverviewCards.revalidate();
        pnlOverviewCards.repaint();
    }

    @Override
    public boolean confirmBeforeSwitch() {
        return true;
    }
}
