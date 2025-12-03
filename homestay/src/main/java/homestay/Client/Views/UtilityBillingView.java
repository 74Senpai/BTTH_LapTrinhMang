package homestay.Client.Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

// 1. **UtilityBillingView.java**  
//    - Tính tiền các dịch vụ: Điện, Nước, Internet.  
//    - Hiển thị form nhập **chỉ số cũ và mới**.  
//    - Hiển thị **kết quả tổng tiền**.  
//    - Các nút thao tác: **Tính**, **Lưu**, **In**.


//    ### UtilityBillingView.java

// - **UI Components**
//   - `JComboBox<String> cmbService` : chọn dịch vụ
//   - `JTextField txtOld, txtNew` : nhập chỉ số cũ/mới
//   - `JLabel lblResult` : hiển thị kết quả
//   - `JButton btnCalculate, btnSave, btnPrint` : các thao tác
// - **Validation**
//   - Chỉ số cũ/mới phải là số (`Double`)
//   - Nếu dữ liệu không hợp lệ, hiển thị `JOptionPane`
// - **Hàm override trống**
//   - `addCalculateListener(ActionListener listener)`
//   - `addSaveListener(ActionListener listener)`
//   - `addPrintListener(ActionListener listener)`


public class UtilityBillingView extends JFrame {

    private JComboBox<String> cmbService;
    private JTextField txtOld, txtNew;
    private JLabel lblResult;
    private JButton btnCalculate, btnSave, btnPrint;

    public UtilityBillingView() {
        setTitle("Tính tiền dịch vụ");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        // Panel chính
        JPanel panelMain = new JPanel(new BorderLayout(20, 20));
        panelMain.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel form input
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin dịch vụ"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblService = new JLabel("Dịch vụ:");
        lblService.setFont(new Font("Arial", Font.BOLD, 14));
        cmbService = new JComboBox<>(new String[]{"Điện", "Nước", "Internet"});

        JLabel lblOld = new JLabel("Chỉ số cũ:");
        lblOld.setFont(new Font("Arial", Font.BOLD, 14));
        txtOld = new JTextField();

        JLabel lblNew = new JLabel("Chỉ số mới:");
        lblNew.setFont(new Font("Arial", Font.BOLD, 14));
        txtNew = new JTextField();

        lblResult = new JLabel("Tổng tiền: 0 VNĐ", SwingConstants.CENTER);
        lblResult.setFont(new Font("Arial", Font.BOLD, 16));
        lblResult.setForeground(new Color(0, 128, 255));
        lblResult.setBorder(BorderFactory.createEtchedBorder());

        btnCalculate = new JButton("Tính");
        btnSave = new JButton("Lưu");
        btnPrint = new JButton("In");

        // Sắp xếp GridBag
        gbc.gridx = 0; gbc.gridy = 0; pnlForm.add(lblService, gbc);
        gbc.gridx = 1; pnlForm.add(cmbService, gbc);
        gbc.gridx = 0; gbc.gridy = 1; pnlForm.add(lblOld, gbc);
        gbc.gridx = 1; pnlForm.add(txtOld, gbc);
        gbc.gridx = 0; gbc.gridy = 2; pnlForm.add(lblNew, gbc);
        gbc.gridx = 1; pnlForm.add(txtNew, gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; pnlForm.add(lblResult, gbc);

        // Panel button
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlButtons.add(btnCalculate);
        pnlButtons.add(btnSave);
        pnlButtons.add(btnPrint);

        panelMain.add(pnlForm, BorderLayout.CENTER);
        panelMain.add(pnlButtons, BorderLayout.SOUTH);

        add(panelMain);
    }

    // Getter + validate
    public String getService() { return cmbService.getSelectedItem().toString(); }

    public Double getOldIndex() {
        try { return Double.parseDouble(txtOld.getText().trim()); }
        catch(Exception e){ JOptionPane.showMessageDialog(this,"Chỉ số cũ phải là số!"); return null; }
    }

    public Double getNewIndex() {
        try { return Double.parseDouble(txtNew.getText().trim()); }
        catch(Exception e){ JOptionPane.showMessageDialog(this,"Chỉ số mới phải là số!"); return null; }
    }

    public void setResult(double total) { lblResult.setText("Tổng tiền: " + total + " VNĐ"); }

    // Override listener
    public void addCalculateListener(ActionListener listener) { btnCalculate.addActionListener(listener); }
    public void addSaveListener(ActionListener listener) { btnSave.addActionListener(listener); }
    public void addPrintListener(ActionListener listener) { btnPrint.addActionListener(listener); }
}
