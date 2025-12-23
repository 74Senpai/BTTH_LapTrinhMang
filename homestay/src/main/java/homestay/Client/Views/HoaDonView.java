package homestay.Client.Views;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import homestay.DTOs.HoaDonDTO;

public class HoaDonView extends javax.swing.JPanel implements Components.IViewCheck {

    private JTable tblHoaDon;
    private DefaultTableModel tableModel;
    private Label lblTotal;
    private Button btnRefresh;

    public HoaDonView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(248, 249, 250));

        // 1. Tiêu đề
        Label lblTitle = new Label("QUẢN LÝ HÓA ĐƠN & DOANH THU", Label.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(lblTitle, BorderLayout.NORTH);

        // 2. Thanh công cụ (Chỉ giữ lại nút Refresh cho đỡ nhác)
        Panel pnlTop = new Panel(new FlowLayout(FlowLayout.LEFT));
        btnRefresh = new Button("Tải lại danh sách");
        btnRefresh.setBackground(new Color(40, 167, 69));
        btnRefresh.setForeground(Color.WHITE);
        pnlTop.add(btnRefresh);
        add(pnlTop, BorderLayout.BEFORE_FIRST_LINE);

        // 3. Bảng hiển thị (Thay đổi cột cho khớp HoaDonDTO.View)
        String[] columns = {"STT", "Mã HĐ", "Khách hàng", "Phòng", "Tiền phòng", "Phụ phí", "Tổng tiền", "Ngày thanh toán"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tblHoaDon = new JTable(tableModel);
        tblHoaDon.setRowHeight(35);
        Components.centerTable(tblHoaDon); // Dùng lại helper cũ của bạn
        add(new JScrollPane(tblHoaDon), BorderLayout.CENTER);

        // 4. Footer tổng thu
        lblTotal = new Label("Tổng doanh thu: 0 ₫");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotal.setForeground(new Color(220, 53, 69));
        Panel pnlBottom = new Panel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        pnlBottom.add(lblTotal);
        add(pnlBottom, BorderLayout.SOUTH);
    }

    /**
     * Hàm quan trọng nhất: Đổ dữ liệu từ DTO vào bảng
     */
    public void setHoaDonData(List<HoaDonDTO.View> list) {
        tableModel.setRowCount(0);
        double totalRevenue = 0;
        NumberFormat cur = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        int stt = 1;
        for (HoaDonDTO.View hd : list) {
            totalRevenue += hd.tongTien();
            tableModel.addRow(new Object[]{
                stt++,
                "HĐ-" + hd.maThanhToan(),
                hd.tenKhachHang(),
                hd.tenPhong(),
                cur.format(hd.tienPhong()),
                cur.format(hd.tienChiPhiPhu()),
                cur.format(hd.tongTien()),
                hd.ngayThanhToan()
            });
        }
        lblTotal.setText("Tổng doanh thu hệ thống: " + cur.format(totalRevenue));
    }

    public void addRefreshListener(java.awt.event.ActionListener l) {
        btnRefresh.addActionListener(l);
    }

    @Override
    public boolean confirmBeforeSwitch() {
        return true;
    }
}
