package homestay.Client.Views;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionListener;
import java.time.Year;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * View báo cáo điện nước theo tháng
 * CHỈ HIỂN THỊ + GỌI CONTROLLER
 */
public class UtilityBillingMonthlyReportView extends javax.swing.JPanel
        implements Components.IViewCheck {

    // ================= UI =================
    private JTable tblReport;
    private DefaultTableModel tableModel;

    private JComboBox<String> cmbMonth;
    private JComboBox<String> cmbYear;

    private Label lblTotal;

    private Button btnGenerate;
    private Button btnExport;
    private Button btnRefresh;

    public UtilityBillingMonthlyReportView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(248, 249, 250));

        // ================= TITLE =================
        Label lblTitle = new Label("BÁO CÁO ĐIỆN NƯỚC THEO THÁNG", Label.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(33, 37, 41));
        add(lblTitle, BorderLayout.NORTH);

        // ================= TOP FILTER =================
        Panel pnlTop = new Panel(new FlowLayout(FlowLayout.LEFT, 15, 5));

        pnlTop.add(new Label("Tháng:"));
        cmbMonth = new JComboBox<>();
        for (int i = 1; i <= 12; i++) {
            cmbMonth.addItem(String.valueOf(i));
        }
        pnlTop.add(cmbMonth);

        pnlTop.add(new Label("Năm:"));
        cmbYear = new JComboBox<>();
        int currentYear = Year.now().getValue();
        for (int y = currentYear - 5; y <= currentYear + 1; y++) {
            cmbYear.addItem(String.valueOf(y));
        }
        pnlTop.add(cmbYear);

        btnGenerate = new Button("Tạo báo cáo");
        btnExport   = new Button("Xuất Excel");
        btnRefresh  = new Button("Tải lại");

        pnlTop.add(btnGenerate);
        pnlTop.add(btnExport);
        pnlTop.add(btnRefresh);

        add(pnlTop, BorderLayout.BEFORE_FIRST_LINE);

        // ================= TABLE =================
        String[] columns = {
                "STT",
                "Phòng",
                "Điện (kWh)",
                "Nước (m³)",
                "Tiền điện (₫)",
                "Tiền nước (₫)",
                "Tổng (₫)"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho sửa
            }
        };

        tblReport = new JTable(tableModel);
        tblReport.setRowHeight(32);
        tblReport.setFont(new Font("Arial", Font.PLAIN, 14));
        tblReport.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        tblReport.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        Components.centerTable(tblReport);

        JScrollPane scrollPane = new JScrollPane(tblReport);
        add(scrollPane, BorderLayout.CENTER);

        // ================= FOOTER =================
        Panel pnlBottom = new Panel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        lblTotal = new Label("Tổng thu tháng: 0 ₫");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 15));
        lblTotal.setForeground(new Color(0, 130, 0));

        pnlBottom.add(lblTotal);
        add(pnlBottom, BorderLayout.SOUTH);
    }

    // =====================================================
    // ================= PUBLIC METHODS ===================
    // =====================================================

    /**
     * Controller đổ dữ liệu báo cáo
     */
    public void setReportData(Object[][] data) {
        tableModel.setRowCount(0);
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }

    /**
     * Set tổng tiền
     */
    public void setTotalAmount(String total) {
        lblTotal.setText("Tổng thu tháng: " + total + " ₫");
    }

    /**
     * Clear bảng
     */
    public void clear() {
        tableModel.setRowCount(0);
        lblTotal.setText("Tổng thu tháng: 0 ₫");
    }

    public String getSelectedMonth() {
        return cmbMonth.getSelectedItem().toString();
    }

    public String getSelectedYear() {
        return cmbYear.getSelectedItem().toString();
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    // =====================================================
    // ================= CONTROLLER HOOK ===================
    // =====================================================

    public void addGenerateListener(ActionListener l) {
        btnGenerate.addActionListener(l);
    }

    public void addExportListener(ActionListener l) {
        btnExport.addActionListener(l);
    }

    public void addRefreshListener(ActionListener l) {
        btnRefresh.addActionListener(l);
    }

    // =====================================================
    // ================= TAB CHECK =========================
    // =====================================================

    @Override
    public boolean confirmBeforeSwitch() {
        return true;
    }
}
