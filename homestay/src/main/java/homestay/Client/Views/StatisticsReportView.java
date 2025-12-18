package homestay.Client.Views;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * View báo cáo thống kê
 * CHỈ HIỂN THỊ + GỌI CONTROLLER
 */
public class StatisticsReportView extends javax.swing.JPanel implements Components.IViewCheck {

    // ================== UI ==================
    private JComboBox<String> cmbReportType;
    private JTable tblReport;
    private DefaultTableModel tableModel;

    private Label lblSummary;

    private Button btnGenerate;
    private Button btnExport;

    public StatisticsReportView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(248, 249, 250));

        // ================== TITLE ==================
        Label lblTitle = new Label("BÁO CÁO THỐNG KÊ", Label.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(33, 37, 41));
        add(lblTitle, BorderLayout.NORTH);

        // ================== TOP FILTER ==================
        Panel pnlTop = new Panel(new FlowLayout(FlowLayout.LEFT, 15, 5));

        Label lblType = new Label("Loại báo cáo:");
        lblType.setFont(new Font("Arial", Font.PLAIN, 14));

        cmbReportType = new JComboBox<>(new String[] {
                "Doanh thu theo tháng",
                "Phòng theo trạng thái",
                "Khách đang thuê"
        });

        btnGenerate = new Button("Tạo báo cáo");
        btnExport   = new Button("Xuất Excel");

        pnlTop.add(lblType);
        pnlTop.add(cmbReportType);
        pnlTop.add(btnGenerate);
        pnlTop.add(btnExport);

        add(pnlTop, BorderLayout.BEFORE_FIRST_LINE);

        // ================== TABLE ==================
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // View chỉ đọc
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

        // ================== SUMMARY ==================
        lblSummary = new Label("Tổng quan: ---");
        lblSummary.setFont(new Font("Arial", Font.BOLD, 14));
        lblSummary.setForeground(new Color(0, 128, 0));

        Panel pnlBottom = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlBottom.add(lblSummary);

        add(pnlBottom, BorderLayout.SOUTH);
    }

    // =====================================================
    // ================== PUBLIC METHODS ===================
    // =====================================================

    /**
     * Lấy loại báo cáo đang chọn
     */
    public String getSelectedReportType() {
        return cmbReportType.getSelectedItem().toString();
    }

    /**
     * Set cột cho bảng (Controller quyết định)
     */
    public void setTableColumns(String[] columns) {
        tableModel.setColumnIdentifiers(columns);
    }

    /**
     * Load dữ liệu cho bảng
     */
    public void setReportData(Object[][] data) {
        tableModel.setRowCount(0);
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }

    /**
     * Set text tổng quan
     */
    public void setSummary(String text) {
        lblSummary.setText("Tổng quan: " + text);
    }

    /**
     * Hiển thị thông báo
     */
    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    // =====================================================
    // ================== CONTROLLER HOOK ==================
    // =====================================================

    public void addGenerateListener(ActionListener l) {
        btnGenerate.addActionListener(l);
    }

    public void addExportListener(ActionListener l) {
        btnExport.addActionListener(l);
    }

    // =====================================================
    // ================== TAB CHECK ========================
    // =====================================================

    @Override
    public boolean confirmBeforeSwitch() {
        return true; // Không có chỉnh sửa
    }
}
