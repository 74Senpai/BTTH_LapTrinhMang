package homestay.Client.Views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class UtilityBillingMonthlyReportView extends JFrame {

    private JComboBox<String> cmbMonth, cmbYear;
    private JTable tblReport;
    private JLabel lblTotal;
    private JButton btnGenerate, btnExport;

    public UtilityBillingMonthlyReportView() {
        setTitle("Báo cáo điện nước theo tháng");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {

        JPanel panelMain = new JPanel(new BorderLayout(20, 20));
        panelMain.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===================== PANEL TRÊN =====================
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 8));

        pnlTop.add(new JLabel("Tháng:"));
        cmbMonth = new JComboBox<>();
        for (int i = 1; i <= 12; i++) cmbMonth.addItem(i + "");
        pnlTop.add(cmbMonth);

        pnlTop.add(new JLabel("Năm:"));
        cmbYear = new JComboBox<>();
        for (int y = 2020; y <= 2035; y++) cmbYear.addItem(y + "");
        pnlTop.add(cmbYear);

        btnGenerate = new JButton("Tạo báo cáo");
        btnExport = new JButton("Xuất Excel");

        pnlTop.add(btnGenerate);
        pnlTop.add(btnExport);

        // ===================== TABLE =====================
        tblReport = new JTable(
                new DefaultTableModel(
                        new Object[]{"STT", "Phòng", "Số điện", "Số nước", "Tiền điện", "Tiền nước", "Tổng tiền"},
                        0
                )
        );
        tblReport.setFont(new Font("Arial", Font.PLAIN, 13));
        tblReport.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(tblReport);

        // ===================== SUMMARY =====================
        lblTotal = new JLabel("Tổng thu tháng: 0 VNĐ", SwingConstants.LEFT);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 15));
        lblTotal.setForeground(new Color(0, 130, 0));

        panelMain.add(pnlTop, BorderLayout.NORTH);
        panelMain.add(scroll, BorderLayout.CENTER);
        panelMain.add(lblTotal, BorderLayout.SOUTH);

        add(panelMain);
    }

    // ===================== GETTER =====================
    public String getMonth() { return cmbMonth.getSelectedItem().toString(); }
    public String getYear() { return cmbYear.getSelectedItem().toString(); }
    public JTable getTable() { return tblReport; }

    public void setTotal(String text) {
        lblTotal.setText("Tổng thu tháng: " + text + " VNĐ");
    }

    // ===================== LISTENER OVERRIDE =====================
    public void addGenerateListener(ActionListener listener) {
        btnGenerate.addActionListener(listener);
    }

    public void addExportListener(ActionListener listener) {
        btnExport.addActionListener(listener);
    }
}
