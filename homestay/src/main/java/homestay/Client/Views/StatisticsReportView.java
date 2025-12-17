package homestay.Client.Views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

// 2. **StatisticsReportView.java**  
//    - Hiển thị **báo cáo thống kê** theo loại: Doanh thu, Phòng trống, Khách hàng.  
//    - Table thống kê dữ liệu chi tiết.  
//    - Hiển thị **tổng quan ngắn gọn**.  
//    - Các nút thao tác: **Tạo báo cáo**, **Xuất Excel**.

// ### StatisticsReportView.java

// - **UI Components**
//   - `JComboBox<String> cmbType` : chọn loại báo cáo
//   - `JLabel lblSummary` : hiển thị tổng quan
//   - `JTable tblReport` : hiển thị dữ liệu chi tiết
//   - `JButton btnGenerate, btnExport` : thao tác tạo và xuất báo cáo
// - **Hàm override trống**
//   - `addGenerateListener(ActionListener listener)`
//   - `addExportListener(ActionListener listener)`


public class StatisticsReportView extends JFrame {

    private JComboBox<String> cmbType;
    private JButton btnGenerate, btnExport;
    private JTable tblReport;
    private JLabel lblSummary;

    public StatisticsReportView() {
        setTitle("Báo cáo thống kê");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        JPanel panelMain = new JPanel(new BorderLayout(15,15));
        panelMain.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        // Panel trên: chọn loại báo cáo + button
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        this.cmbType = new JComboBox<>(new String[]{"Doanh thu", "Phòng trống", "Khách hàng"});
        this.btnGenerate = new JButton("Tạo báo cáo");
        this.btnExport = new JButton("Xuất Excel");
        pnlTop.add(new JLabel("Loại báo cáo:")); pnlTop.add(this.cmbType);
        pnlTop.add(btnGenerate); pnlTop.add(this.btnExport);

        // Label tổng quan
        this.lblSummary = new JLabel("Tổng quan: ", SwingConstants.LEFT);
        this.lblSummary.setFont(new Font("Arial", Font.BOLD, 14));
        this.lblSummary.setForeground(new Color(0, 128, 0));

        // Table
        this.tblReport = new JTable(new DefaultTableModel(new Object[]{"STT","Tên","Giá trị"},0));
        this.tblReport.setRowHeight(25);
        this.tblReport.setFont(new Font("Arial", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(this.tblReport);

        panelMain.add(pnlTop, BorderLayout.NORTH);
        panelMain.add(scroll, BorderLayout.CENTER);
        panelMain.add(this.lblSummary, BorderLayout.SOUTH);

        add(panelMain);
    }

    public String getSelectedReportType() { return cmbType.getSelectedItem().toString(); }

    public JTable getTable() { return this.tblReport; }

    public void setSummary(String text) { lblSummary.setText("Tổng quan: " + text); }

    // Override listener
    public void addGenerateListener(ActionListener listener) { btnGenerate.addActionListener(listener); }
    public void addExportListener(ActionListener listener) { btnExport.addActionListener(listener); }
}

