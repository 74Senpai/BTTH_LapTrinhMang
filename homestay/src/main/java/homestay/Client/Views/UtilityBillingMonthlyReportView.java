package homestay.Client.Views;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.time.Year;
import java.util.List;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import homestay.DTOs.DienNuocDTO;

/**
 * View báo cáo điện nước theo tháng
 */
public class UtilityBillingMonthlyReportView extends javax.swing.JPanel
        implements Components.IViewCheck {

    private JTable tblReport;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbMonth;
    private JComboBox<String> cmbYear;
    private Label lblTotal;

    private Button btnGenerate;
    private Button btnExport;
    private Button btnRefresh;

    // Đơn giá giả định (Có thể lấy từ config hoặc server)
    private final double GIA_DIEN = 3500;
    private final double GIA_NUOC = 12000;

    public UtilityBillingMonthlyReportView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(248, 249, 250));

        // ================= NORTH: TITLE =================
        Panel pnlHeader = new Panel(new BorderLayout());
        Label lblTitle = new Label("BÁO CÁO CHI PHÍ ĐIỆN NƯỚC", Label.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        add(pnlHeader, BorderLayout.NORTH);

        // ================= TOP: FILTER BAR =================
        Panel pnlTop = new Panel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlTop.setBackground(Color.WHITE);

        pnlTop.add(new Label("Tháng:"));
        cmbMonth = new JComboBox<>();
        for (int i = 1; i <= 12; i++) {
            cmbMonth.addItem(String.valueOf(i));
        }
        pnlTop.add(cmbMonth);

        pnlTop.add(new Label("Năm:"));
        cmbYear = new JComboBox<>();
        int currentYear = Year.now().getValue();
        for (int y = currentYear - 3; y <= currentYear + 1; y++) {
            cmbYear.addItem(String.valueOf(y));
        }
        cmbYear.setSelectedItem(String.valueOf(currentYear));
        pnlTop.add(cmbYear);

        // Khởi tạo các nút bấm (Tránh lỗi NullPointerException)
        btnGenerate = new Button("Xem báo cáo");
        btnExport = new Button("Xuất Excel");
        btnRefresh = new Button("Tải lại");

        // Trang trí nút
        btnGenerate.setBackground(new Color(0, 123, 255));
        btnGenerate.setForeground(Color.WHITE);

        pnlTop.add(btnGenerate);
        pnlTop.add(btnExport);
        pnlTop.add(btnRefresh);

        add(pnlTop, BorderLayout.BEFORE_FIRST_LINE);

        // ================= CENTER: TABLE =================
        String[] columns = {
            "STT", "Mã Phòng", "Tiêu thụ Điện", "Tiêu thụ Nước", "Thành tiền Điện", "Thành tiền Nước", "Tổng cộng"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblReport = new JTable(tableModel);
        tblReport.setRowHeight(35);
        tblReport.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblReport.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        // Căn giữa dữ liệu bảng
        Components.centerTable(tblReport);

        JScrollPane scrollPane = new JScrollPane(tblReport);
        add(scrollPane, BorderLayout.CENTER);

        // ================= SOUTH: FOOTER =================
        Panel pnlBottom = new Panel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        lblTotal = new Label("Tổng doanh thu dịch vụ: 0 ₫");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotal.setForeground(new Color(220, 53, 69)); // Màu đỏ nhấn mạnh

        pnlBottom.add(lblTotal);
        add(pnlBottom, BorderLayout.SOUTH);
    }

    /**
     * Cập nhật dữ liệu từ List DTO nhận được từ Server
     */
    public void updateReportData(List<DienNuocDTO.View> list) {
        tableModel.setRowCount(0);
        double grandTotal = 0;
        NumberFormat cur = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        int stt = 1;
        for (DienNuocDTO.View dn : list) {
            double tienDien = dn.soDienTieuThu() * GIA_DIEN;
            double tienNuoc = dn.soNuocTieuThu() * GIA_NUOC;
            double tong = tienDien + tienNuoc;
            grandTotal += tong;

            tableModel.addRow(new Object[]{
                stt++,
                "Phòng " + dn.maPhong(),
                dn.soDienTieuThu() + " kWh",
                dn.soNuocTieuThu() + " m³",
                cur.format(tienDien),
                cur.format(tienNuoc),
                cur.format(tong)
            });
        }
        lblTotal.setText("Tổng doanh thu dịch vụ: " + cur.format(grandTotal));
    }

    // Các phương thức lấy giá trị filter
    public int getMonth() {
        return Integer.parseInt(cmbMonth.getSelectedItem().toString());
    }

    public int getYear() {
        return Integer.parseInt(cmbYear.getSelectedItem().toString());
    }

    // Gán sự kiện cho các nút
    public void addGenerateListener(ActionListener l) {
        btnGenerate.addActionListener(l);
    }

    public void addRefreshListener(ActionListener l) {
        btnRefresh.addActionListener(l);
    }

    public void addExportListener(ActionListener l) {
        btnExport.addActionListener(l);
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public boolean confirmBeforeSwitch() {
        return true;
    }
}
